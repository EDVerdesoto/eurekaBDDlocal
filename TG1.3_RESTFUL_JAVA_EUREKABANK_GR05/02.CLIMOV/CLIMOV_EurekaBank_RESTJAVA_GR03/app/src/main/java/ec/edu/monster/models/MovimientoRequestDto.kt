package ec.edu.monster.models

data class MovimientoRequestDto(
    val cuentaOrigen: String,
    val cuentaDestino: String? = null,
    val tipo: String,
    val importe: Double
)
