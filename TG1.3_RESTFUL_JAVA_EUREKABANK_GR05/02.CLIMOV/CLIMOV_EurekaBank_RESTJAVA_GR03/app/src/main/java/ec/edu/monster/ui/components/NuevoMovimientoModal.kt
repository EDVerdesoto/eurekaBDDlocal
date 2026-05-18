package ec.edu.monster.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ec.edu.monster.ui.viewmodels.MovementsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoMovimientoModal(
    cuenta: String,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: MovementsViewModel
) {
    var tipo by remember { mutableStateOf("") }
    var importe by remember { mutableStateOf("") }
    var cuentaDestino by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val tiposDisponibles by viewModel.tiposDisponibles.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadTiposMovimiento()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Movimiento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Cuenta Origen: $cuenta")
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Movimiento") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        tiposDisponibles.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    tipo = selectionOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = importe,
                    onValueChange = { importe = it },
                    label = { Text("Importe") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (tipo.contains("Transferencia", ignoreCase = true)) {
                    OutlinedTextField(
                        value = cuentaDestino,
                        onValueChange = { cuentaDestino = it },
                        label = { Text("Cuenta Destino") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val imp = importe.toDoubleOrNull()
                    if (imp == null) {
                        error = "Importe inválido"
                        return@Button
                    }
                    if (tipo.isBlank()) {
                        error = "Seleccione un tipo de movimiento"
                        return@Button
                    }
                    loading = true
                    viewModel.registrarMovimiento(
                        cuentaOrigen = cuenta,
                        cuentaDestino = if (cuentaDestino.isBlank()) null else cuentaDestino,
                        tipo = tipo,
                        importe = imp
                    ) { success, msg ->
                        loading = false
                        if (success) {
                            onSuccess()
                            onDismiss()
                        } else {
                            error = msg ?: "Error desconocido"
                        }
                    }
                },
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Registrar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !loading) { Text("Cancelar") }
        }
    )
}
