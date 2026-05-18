package ec.edu.monster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.edu.monster.models.MovimientoData
import ec.edu.monster.models.MovimientoRequestDto
import ec.edu.monster.services.EurekaBankApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovementsViewModel @Inject constructor(
    private val api: EurekaBankApi
) : ViewModel() {
    private val _movimientos = MutableStateFlow<List<MovimientoData>>(emptyList())
    val movimientos: StateFlow<List<MovimientoData>> = _movimientos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tiposDisponibles = MutableStateFlow<List<String>>(emptyList())
    val tiposDisponibles: StateFlow<List<String>> = _tiposDisponibles

    fun loadMovimientos(cuenta: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.traerMovimientos(cuenta)
                if (response.isSuccessful) {
                    _movimientos.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTiposMovimiento() {
        viewModelScope.launch {
            try {
                val response = api.obtenerTiposMovimiento()
                if (response.isSuccessful) {
                    _tiposDisponibles.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun registrarMovimiento(
        cuentaOrigen: String,
        cuentaDestino: String?,
        tipo: String,
        importe: Double,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val body = MovimientoRequestDto(
                    cuentaOrigen = cuentaOrigen,
                    cuentaDestino = cuentaDestino?.takeIf { it.isNotBlank() },
                    tipo = tipo,
                    importe = importe
                )
                val response = api.regMovimiento(body)
                if (response.isSuccessful) {
                    val result = response.body()
                    onResult(result?.esExito() == true, result?.mensaje)
                } else {
                    onResult(false, "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }
}
