
package ec.edu.monster.models

import java.math.BigDecimal
import java.util.Hashtable
data class DatosCuenta(
    var codigo: String = "",
    var nombreCliente: String = "",
    var moneda: String = "",
    var saldo: BigDecimal = BigDecimal.ZERO,
    var estado: String = "",
    var emailCliente: String = "",
    var telefonoCliente: String = ""
)