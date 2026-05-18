package ec.edu.monster.ui.viewmodels

import androidx.lifecycle.ViewModel
import ec.edu.monster.models.MovimientoData
import ec.edu.monster.services.EurekaBankService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovementsViewModel : ViewModel() {
    private val _movimientos = MutableStateFlow<List<MovimientoData>>(emptyList())
    val movimientos: StateFlow<List<MovimientoData>> = _movimientos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadMovimientos(cuenta: String) {
        _isLoading.value = true
        _error.value = null
        EurekaBankService.traerMovimientos(cuenta) { list, err ->
            _isLoading.value = false
            if (err != null) {
                _error.value = err
            } else {
                _movimientos.value = list
            }
        }
    }
}
