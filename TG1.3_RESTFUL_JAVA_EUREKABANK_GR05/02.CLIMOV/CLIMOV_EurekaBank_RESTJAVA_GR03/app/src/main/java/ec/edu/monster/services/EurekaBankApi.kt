package ec.edu.monster.services

import ec.edu.monster.models.DatosCuenta
import ec.edu.monster.models.MovimientoData
import ec.edu.monster.models.MovimientoRequestDto
import ec.edu.monster.models.ResultadoOperacion
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EurekaBankApi {
    @POST("login")
    suspend fun iniciarSesion(@Body request: Map<String, String>): Response<ResultadoOperacion>

    @GET("cuentas")
    suspend fun traerCuentasConClientes(): Response<List<DatosCuenta>>

    @GET("movimientos")
    suspend fun traerMovimientos(@Query("cuenta") cuenta: String): Response<List<MovimientoData>>

    @GET("tipos")
    suspend fun obtenerTiposMovimiento(): Response<List<String>>

    @POST("movimientos")
    suspend fun regMovimiento(@Body request: MovimientoRequestDto): Response<ResultadoOperacion>
}
