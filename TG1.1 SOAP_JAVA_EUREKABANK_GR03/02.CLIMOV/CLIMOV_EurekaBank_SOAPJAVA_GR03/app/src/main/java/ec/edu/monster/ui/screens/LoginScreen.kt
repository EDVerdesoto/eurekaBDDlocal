
package ec.edu.monster.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ec.edu.monster.services.EurekaBankService
import ec.edu.monster.ui.theme.EurekaBankSOAPJava
import ec.edu.monster.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onLoginError: (String) -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = EurekaBankSOAPJava.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(EurekaBankSOAPJava.spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo Principal
            Image(
                painter = painterResource(id = R.drawable.sulli_logo),
                contentDescription = "Logo Principal",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 12.dp) // ← Reducido, elegante
            )

// Logo Secundario + Título (juntos)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp) // ← Muy cerca
            ) {
                Image(
                    painter = painterResource(id = R.drawable.soap_java),
                    contentDescription = "Logo SOAP Java",
                    modifier = Modifier
                        .size(180.dp) // ← Reducido para mejor balance
                )

                Text(
                    text = "Sistema de Gestión Bancaria",
                    style = MaterialTheme.typography.titleLarge,
                    color = EurekaBankSOAPJava.primary,
                    modifier = Modifier.padding(horizontal = 16.dp) // ← Centrado con el logo
                )
            }

            // Campo Usuario
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                enabled = !loading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EurekaBankSOAPJava.primary,
                    unfocusedBorderColor = EurekaBankSOAPJava.border
                )
            )

            Spacer(modifier = Modifier.height(EurekaBankSOAPJava.spacingMedium))

            // Campo Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                enabled = !loading,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EurekaBankSOAPJava.primary,
                    unfocusedBorderColor = EurekaBankSOAPJava.border
                )
            )

            Spacer(modifier = Modifier.height(EurekaBankSOAPJava.spacingLarge))

            // Botón Login
            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank()) {
                        onLoginError("Complete todos los campos")
                        return@Button
                    }
                    loading = true
                    mensaje = null

                    EurekaBankService.iniciarSesion(usuario, contrasena) { success, msg ->
                        loading = false
                        mensaje = msg
                        if (success) {
                            onLoginSuccess()
                        } else {
                            onLoginError(msg ?: "Error desconocido")
                        }
                    }
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EurekaBankSOAPJava.primary,
                    disabledContainerColor = EurekaBankSOAPJava.border
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = EurekaBankSOAPJava.onPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar Sesión", color = EurekaBankSOAPJava.onPrimary)
                }
            }

            // Mensaje de error o éxito
            mensaje?.let {
                Spacer(modifier = Modifier.height(EurekaBankSOAPJava.spacingMedium))
                Text(
                    text = it,
                    color = if (it.contains("exitoso")) EurekaBankSOAPJava.success else EurekaBankSOAPJava.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(
        onLoginSuccess = {},
        onLoginError = {}
    )
}