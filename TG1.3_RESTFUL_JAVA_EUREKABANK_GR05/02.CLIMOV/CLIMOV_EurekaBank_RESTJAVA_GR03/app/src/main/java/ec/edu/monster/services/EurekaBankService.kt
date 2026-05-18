package ec.edu.monster.services

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ec.edu.monster.models.DatosCuenta
import ec.edu.monster.models.MovimientoData
import ec.edu.monster.models.ResultadoOperacion
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.math.BigDecimal

object EurekaBankService {
    private const val TAG = "EurekaBankService"
    private const val BASE_URL = "https://javasoto.dr00p3r.top/WS_EurekaBank_RESTJAVA_GR03/api/eurekabank"
    private val client = OkHttpClient()
    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun iniciarSesion(
        usuario: String,
        contrasena: String,
        callback: (success: Boolean, message: String?) -> Unit
    ) {
        val json = """{"usuario":"$usuario","contrasena":"$contrasena"}"""
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/login")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error login", e)
                mainHandler.post { callback(false, "Error: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                Log.d(TAG, "Login response: $bodyStr")
                val resultado = gson.fromJson(bodyStr, ResultadoOperacion::class.java)
                mainHandler.post {
                    callback(resultado.esExito(), resultado.mensaje)
                }
            }
        })
    }

    fun traerCuentasConClientes(
        callback: (List<DatosCuenta>, String?) -> Unit
    ) {
        val request = Request.Builder()
            .url("$BASE_URL/cuentas")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error cuentas", e)
                mainHandler.post { callback(emptyList(), "Error: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                Log.d(TAG, "Cuentas response: $bodyStr")
                try {
                    val type = object : TypeToken<List<DatosCuenta>>() {}.type
                    val lista: List<DatosCuenta> = gson.fromJson(bodyStr, type)
                    mainHandler.post { callback(lista, null) }
                } catch (e: Exception) {
                    mainHandler.post { callback(emptyList(), "Error parseando: ${e.message}") }
                }
            }
        })
    }

    fun traerMovimientos(
        cuenta: String,
        callback: (List<MovimientoData>, String?) -> Unit
    ) {
        val request = Request.Builder()
            .url("$BASE_URL/movimientos?cuenta=$cuenta")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error movimientos", e)
                mainHandler.post { callback(emptyList(), "Error: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                Log.d(TAG, "Movimientos response: $bodyStr")
                try {
                    val type = object : TypeToken<List<MovimientoData>>() {}.type
                    val lista: List<MovimientoData> = gson.fromJson(bodyStr, type)
                    mainHandler.post { callback(lista, null) }
                } catch (e: Exception) {
                    mainHandler.post { callback(emptyList(), "Error parseando: ${e.message}") }
                }
            }
        })
    }

    fun obtenerTiposMovimiento(
        callback: (List<String>, String?) -> Unit
    ) {
        val request = Request.Builder()
            .url("$BASE_URL/tipos")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error tipos", e)
                mainHandler.post { callback(emptyList(), "Error: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                Log.d(TAG, "Tipos response: $bodyStr")
                try {
                    val type = object : TypeToken<List<String>>() {}.type
                    val lista: List<String> = gson.fromJson(bodyStr, type)
                    mainHandler.post { callback(lista, null) }
                } catch (e: Exception) {
                    mainHandler.post { callback(emptyList(), "Error parseando: ${e.message}") }
                }
            }
        })
    }

    fun regMovimiento(
        cuentaOrigen: String,
        cuentaDestino: String?,
        tipo: String,
        importe: Double,
        callback: (ResultadoOperacion?, String?) -> Unit
    ) {
        val json = """{"cuentaOrigen":"$cuentaOrigen","cuentaDestino":"${cuentaDestino ?: ""}","tipo":"$tipo","importe":$importe}"""
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/movimientos")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error reg movimiento", e)
                mainHandler.post { callback(null, "Error: ${e.message}") }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                Log.d(TAG, "Reg movimiento response: $bodyStr")
                try {
                    val resultado = gson.fromJson(bodyStr, ResultadoOperacion::class.java)
                    mainHandler.post { callback(resultado, null) }
                } catch (e: Exception) {
                    mainHandler.post { callback(null, "Error parseando: ${e.message}") }
                }
            }
        })
    }
}
