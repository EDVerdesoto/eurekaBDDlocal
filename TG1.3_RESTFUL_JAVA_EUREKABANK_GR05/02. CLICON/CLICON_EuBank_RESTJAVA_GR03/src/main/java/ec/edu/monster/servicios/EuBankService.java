package ec.edu.monster.servicios;

// ¡IMPORTANTE! Importa los modelos POJO que copiaste en el Paso 4

import ec.edu.monster.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class EuBankService {

    private final RestTemplate restTemplate;

    // --- ¡¡CONFIGURACIÓN IMPORTANTE!! ---
    // Esta debe ser la URL base de tu API REST
    // (Asumiendo que tu app se llama 'WS_EurekaBank_REST' y usa '/api')
    private final String API_BASE_URL = "https://javasoto.dr00p3r.top/WS_EurekaBank_RESTJAVA_GR03/api/eurekabank";

    // Inyectamos el RestTemplate que creamos en el 'main'
    public EuBankService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    // --- 1. Login ---
    public ResultadoOperacion iniciarSesion(String usuario, String contrasena) {
        String url = API_BASE_URL + "/login";
        
        // Creamos el objeto que el API espera (LoginRequest)
        Map<String, String> requestBody = Map.of(
            "usuario", usuario,
            "contrasena", contrasena
        );
        
        // Llamamos al POST y esperamos un objeto 'ResultadoOperacion'
        return restTemplate.postForObject(url, requestBody, ResultadoOperacion.class);
    }

    // --- 2. Traer Cuentas ---
    public List<DatosCuenta> traerCuentasConClientes() {
        String url = API_BASE_URL + "/cuentas";
        
        // Llamamos al GET y esperamos un array de DatosCuenta
        DatosCuenta[] cuentas = restTemplate.getForObject(url, DatosCuenta[].class);
        
        return Arrays.asList(cuentas != null ? cuentas : new DatosCuenta[0]);
    }
    
    // --- 3. Traer Movimientos (¡Con Ordenamiento A PRUEBA DE BALAS!) ---
    public List<MovimientoData> traerMovimientos(String cuenta) {
        String url = API_BASE_URL + "/movimientos";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (cuenta != null && !cuenta.isEmpty()) {
            builder.queryParam("cuenta", cuenta);
        }

        MovimientoData[] movimientos = restTemplate.getForObject(builder.toUriString(), MovimientoData[].class);

        List<MovimientoData> listaMovimientos = new ArrayList<>(
            Arrays.asList(movimientos != null ? movimientos : new MovimientoData[0])
        );

        // --- ¡AQUÍ ESTÁ LA MAGIA CORREGIDA! ---

        // 1. Definimos el formato ISO de fecha del servidor (yyyy-MM-dd o yyyy-MM-ddZ)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['Z']");

        // 2. Ordenamos la lista
        listaMovimientos.sort((m1, m2) -> {
            java.time.LocalDate fecha1 = null;
            java.time.LocalDate fecha2 = null;

            // Parsear fecha 1
            try {
                if (m1.getFecha() != null && !m1.getFecha().isEmpty()) {
                    fecha1 = java.time.LocalDate.parse(m1.getFecha(), formatter);
                }
            } catch (DateTimeParseException e) {
                // Fecha invalida, se ignora
            }

            // Parsear fecha 2
            try {
                if (m2.getFecha() != null && !m2.getFecha().isEmpty()) {
                    fecha2 = java.time.LocalDate.parse(m2.getFecha(), formatter);
                }
            } catch (DateTimeParseException e) {
                // Fecha invalida, se ignora
            }

            // Comparacion descendente
            if (fecha1 != null && fecha2 != null) {
                int comparacionFecha = fecha2.compareTo(fecha1);
                if (comparacionFecha != 0) {
                    return comparacionFecha;
                }
                return Integer.compare(m2.getNumero(), m1.getNumero());
            } else if (fecha1 == null && fecha2 != null) {
                return 1;
            } else if (fecha1 != null && fecha2 == null) {
                return -1;
            } else {
                return Integer.compare(m2.getNumero(), m1.getNumero());
            }
        });

        // 3. Devolvemos la lista YA ORDENADA
        return listaMovimientos;
    }
    
    // --- 4. Traer Tipos de Movimiento ---
    public List<String> obtenerTipoMovimientosPermitidos() {
        String url = API_BASE_URL + "/tipos";
        String[] tipos = restTemplate.getForObject(url, String[].class);
        return Arrays.asList(tipos != null ? tipos : new String[0]);
    }
    
    // --- 5. Registrar Movimiento ---
    // (El 'CrearMovimientoRequest' viene del DashboardController)
    public ResultadoOperacion regMovimiento(ec.edu.monster.views.DashboardVista.MovimientoRequest request) throws HttpClientErrorException {        
        String url = API_BASE_URL + "/movimientos";
        
        // El objeto 'request' se convierte a JSON y se envía
        return restTemplate.postForObject(url, request, ResultadoOperacion.class);
    }
}
