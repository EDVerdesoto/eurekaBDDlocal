package ec.edu.monster.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ec.edu.monster.services.EurekaBankService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoMovimientoModal(
    cuenta: String,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var tipo by remember { mutableStateOf("") }
    var importe by remember { mutableStateOf("") }
    var cuentaDestino by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var tiposDisponibles by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        EurekaBankService.obtenerTiposMovimiento { list, _ ->
            tiposDisponibles = list
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Movimiento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Cuenta Origen: $cuenta")
                
                // Selector de tipo (simplificado)
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo de Movimiento") },
                    modifier = Modifier.fillMaxWidth()
                )

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
                    loading = true
                    EurekaBankService.regMovimiento(
                        cuentaOrigen = cuenta,
                        cuentaDestino = if (cuentaDestino.isBlank()) null else cuentaDestino,
                        tipo = tipo,
                        importe = imp
                    ) { result, err ->
                        loading = false
                        if (err != null) {
                            error = err
                        } else if (result?.esExito() == true) {
                            onSuccess()
                            onDismiss()
                        } else {
                            error = result?.mensaje ?: "Error desconocido"
                        }
                    }
                },
                enabled = !loading
            ) {
                Text("Registrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
