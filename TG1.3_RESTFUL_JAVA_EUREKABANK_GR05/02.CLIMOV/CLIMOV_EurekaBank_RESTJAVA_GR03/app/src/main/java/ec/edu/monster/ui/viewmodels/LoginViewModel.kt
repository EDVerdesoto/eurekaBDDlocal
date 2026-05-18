package ec.edu.monster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.edu.monster.services.EurekaBankApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: EurekaBankApi
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(usuario: String, contrasena: String) {
        viewModelScope.launch {
            _loading.value = true
            _mensaje.value = null
            _loginSuccess.value = false
            try {
                val response = api.iniciarSesion(mapOf("usuario" to usuario, "contrasena" to contrasena))
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result?.esExito() == true) {
                        _loginSuccess.value = true
                        _mensaje.value = result.mensaje
                    } else {
                        _mensaje.value = result?.mensaje ?: "Credenciales inválidas"
                    }
                } else {
                    _mensaje.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error de red: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
