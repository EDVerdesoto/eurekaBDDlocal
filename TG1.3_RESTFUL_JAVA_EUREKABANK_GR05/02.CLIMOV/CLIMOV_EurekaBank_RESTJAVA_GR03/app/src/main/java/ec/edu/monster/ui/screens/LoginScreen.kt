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
import androidx.hilt.navigation.compose.hiltViewModel
import ec.edu.monster.ui.theme.EurekaBankRESTJava
import ec.edu.monster.R
import ec.edu.monster.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onLoginError: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    
    val loading by viewModel.loading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold(
        containerColor = EurekaBankRESTJava.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(EurekaBankRESTJava.spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.sulli_logo),
                contentDescription = "Logo Principal",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 12.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rest_java),
                    contentDescription = "Logo REST Java",
                    modifier = Modifier.size(180.dp)
                )

                Text(
                    text = "Sistema de Gestión Bancaria",
                    style = MaterialTheme.typography.titleLarge,
                    color = EurekaBankRESTJava.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                enabled = !loading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EurekaBankRESTJava.primary,
                    unfocusedBorderColor = EurekaBankRESTJava.border
                )
            )

            Spacer(modifier = Modifier.height(EurekaBankRESTJava.spacingMedium))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                enabled = !loading,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EurekaBankRESTJava.primary,
                    unfocusedBorderColor = EurekaBankRESTJava.border
                )
            )

            Spacer(modifier = Modifier.height(EurekaBankRESTJava.spacingLarge))

            Button(
                onClick = {
                    if (usuario.isBlank() || contrasena.isBlank()) {
                        onLoginError("Complete todos los campos")
                        return@Button
                    }
                    viewModel.login(usuario, contrasena)
                },
                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EurekaBankRESTJava.primary,
                    disabledContainerColor = EurekaBankRESTJava.border
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = EurekaBankRESTJava.onPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Iniciar Sesión", color = EurekaBankRESTJava.onPrimary)
                }
            }

            mensaje?.let {
                Spacer(modifier = Modifier.height(EurekaBankRESTJava.spacingMedium))
                Text(
                    text = it,
                    color = if (it.contains("exitoso", ignoreCase = true)) EurekaBankRESTJava.success else EurekaBankRESTJava.error,
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
