package ec.edu.monster.servicios;

import ec.edu.monster.model.*; // ¡Importa los POJOs limpios!
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class EuBankService {

    private final RestTemplate restTemplate;
    
    // ¡Asegúrate que esta URL sea la de tu backend REST!
    private final String API_BASE_URL = "https://javasoto.dr00p3r.top/WS_EurekaBank_RESTJAVA_GR03/api/eurekabank";

    public EuBankService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ResultadoOperacion iniciarSesion(String usuario, String contrasena) {
        String url = API_BASE_URL + "/login";
        Map<String, String> requestBody = Map.of("usuario", usuario, "contrasena", contrasena);
        return restTemplate.postForObject(url, requestBody, ResultadoOperacion.class);
    }

    public List<DatosCuenta> traerCuentasConClientes() {
        String url = API_BASE_URL + "/cuentas";
        DatosCuenta[] cuentas = restTemplate.getForObject(url, DatosCuenta[].class);
        return Arrays.asList(cuentas != null ? cuentas : new DatosCuenta[0]);
    }
    
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
        
        // Ordenamiento por fecha ISO (yyyy-MM-dd o yyyy-MM-ddZ)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['Z']");
        listaMovimientos.sort((m1, m2) -> {
            java.time.LocalDate fecha1 = null, fecha2 = null;
            try { if (m1.getFecha() != null) fecha1 = java.time.LocalDate.parse(m1.getFecha(), formatter); } catch (DateTimeParseException e) {}
            try { if (m2.getFecha() != null) fecha2 = java.time.LocalDate.parse(m2.getFecha(), formatter); } catch (DateTimeParseException e) {}

            if (fecha1 != null && fecha2 != null) {
                int resFecha = fecha2.compareTo(fecha1);
                if (resFecha != 0) return resFecha;
                return Integer.compare(m2.getNumero(), m1.getNumero());
            }
            else if (fecha1 == null && fecha2 != null) return 1;
            else if (fecha1 != null && fecha2 == null) return -1;
            else return Integer.compare(m2.getNumero(), m1.getNumero());
        });
        
        return listaMovimientos;
    }
    
    public List<String> obtenerTipoMovimientosPermitidos() {
        String url = API_BASE_URL + "/tipos";
        String[] tipos = restTemplate.getForObject(url, String[].class);
        return Arrays.asList(tipos != null ? tipos : new String[0]);
    }
    
    // ¡CAMBIO! Ahora acepta el POJO 'MovimientoRequest'
    public ResultadoOperacion regMovimiento(MovimientoRequestDto request) throws HttpClientErrorException {
        String url = API_BASE_URL + "/movimientos";
        return restTemplate.postForObject(url, request, ResultadoOperacion.class);
    }
}
