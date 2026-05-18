package ec.edu.monster.ui.viewmodels

import androidx.lifecycle.ViewModel
import ec.edu.monster.models.DatosCuenta
import ec.edu.monster.services.EurekaBankService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccountsViewModel : ViewModel() {
    private val _cuentas = MutableStateFlow<List<DatosCuenta>>(emptyList())
    val cuentas: StateFlow<List<DatosCuenta>> = _cuentas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCuentas() {
        _loading.value = true
        _error.value = null
        EurekaBankService.traerCuentasConClientes { list, err ->
            _loading.value = false
            if (err != null) {
                _error.value = err
            } else {
                _cuentas.value = list
            }
        }
    }
}
