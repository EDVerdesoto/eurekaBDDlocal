package ec.edu.monster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.edu.monster.models.DatosCuenta
import ec.edu.monster.services.EurekaBankApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val api: EurekaBankApi
) : ViewModel() {
    private val _cuentas = MutableStateFlow<List<DatosCuenta>>(emptyList())
    val cuentas: StateFlow<List<DatosCuenta>> = _cuentas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCuentas() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.traerCuentasConClientes()
                if (response.isSuccessful) {
                    _cuentas.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
