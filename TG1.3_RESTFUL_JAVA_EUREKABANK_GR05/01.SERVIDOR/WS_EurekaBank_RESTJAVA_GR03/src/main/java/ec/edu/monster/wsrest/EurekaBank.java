/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package ec.edu.monster.wsrest;

import ec.edu.monster.model.*;
import ec.edu.monster.service.EurekaService;
import ec.edu.monster.service.Login;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Arrays;
import java.util.List;

/**
 * REST Web Service
 *
 * @author leona
 */

@Path("eurekabank")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EurekaBank {

    private static final String DEFAULT_COD_EMPLEADO = "0001";
    private final EurekaService eurekaService = new EurekaService();
    private final Login loginService = new Login();

    // ==============================================================
    // 1. TRAER MOVIMIENTOS (opcional: ?cuenta=00100001)
    // ==============================================================
    @GET
    @Path("movimientos")
    public Response traerMovimientos(@QueryParam("cuenta") String cuenta) {
        try {
            List<MovimientoData> lista = eurekaService.leerMovimientos(cuenta);
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(500)
                    .entity(ResultadoOperacion.error("Error al obtener movimientos: " + e.getMessage()))
                    .build();
        }
    }


    @POST
    @Path("movimientos")
    public Response registrarMovimiento(MovimientoRequestDto req) {
        try {
            // Validaciones
            if (req.getCuentaOrigen() == null || req.getCuentaOrigen().trim().isEmpty()) {
                return Response.status(400).entity(ResultadoOperacion.error("Cuenta origen requerida")).build();
            }
            if (req.getImporte() <= 0) {
                return Response.status(400).entity(ResultadoOperacion.error("Importe debe ser > 0")).build();
            }
            if (req.getTipo() == null || req.getTipo().trim().isEmpty()) {
                return Response.status(400).entity(ResultadoOperacion.error("Tipo requerido")).build();
            }

            String tipoUpper = req.getTipo().toUpperCase();

            switch (tipoUpper) {
                case "DEPOSITO":
                    if (req.getCuentaDestino() != null && !req.getCuentaDestino().trim().isEmpty()) {
                        return Response.status(400).entity(ResultadoOperacion.error("Depósito no debe tener cuenta destino")).build();
                    }
                    eurekaService.registrarDeposito(req.getCuentaOrigen(), req.getImporte(), DEFAULT_COD_EMPLEADO);
                    break;

                case "RETIRO":
                    if (req.getCuentaDestino() != null && !req.getCuentaDestino().trim().isEmpty()) {
                        return Response.status(400).entity(ResultadoOperacion.error("Retiro no debe tener cuenta destino")).build();
                    }
                    eurekaService.registrarRetiro(req.getCuentaOrigen(), req.getImporte(), DEFAULT_COD_EMPLEADO);
                    break;

                case "TRANSFERENCIA":
                    if (req.getCuentaDestino() == null || req.getCuentaDestino().trim().isEmpty()) {
                        return Response.status(400).entity(ResultadoOperacion.error("Cuenta destino requerida")).build();
                    }
                    eurekaService.registrarTransferencia(req.getCuentaOrigen(), req.getCuentaDestino(), req.getImporte(), DEFAULT_COD_EMPLEADO);
                    break;

                default:
                    return Response.status(400).entity(ResultadoOperacion.error("Tipo inválido: " + req.getTipo())).build();
            }

            return Response.ok(ResultadoOperacion.exito()).build();

        } catch (RuntimeException e) {
            return Response.status(400).entity(ResultadoOperacion.error(e.getMessage())).build();
        } catch (Exception e) {
            return Response.status(500).entity(ResultadoOperacion.error("Error interno del servidor")).build();
        }
    }

    // ==============================================================
    // 3. LOGIN
    // ==============================================================
    public static class LoginRequest {

        public String usuario;
        public String contrasena;
    }

    @POST
    @Path("login")
    public Response login(LoginRequest req) {
        try {
            if (req.usuario == null || req.usuario.trim().isEmpty()
                    || req.contrasena == null || req.contrasena.trim().isEmpty()) {
                return Response.status(400).entity(ResultadoOperacion.error("Usuario y contraseña requeridos")).build();
            }

            boolean ok = loginService.IniciarSesion(req.usuario, req.contrasena);
            return Response.ok(
                    new ResultadoOperacion(ok ? 1 : -1, ok ? "Login exitoso" : "Credenciales inválidas")
            ).build();

        } catch (Exception e) {
            return Response.status(500).entity(ResultadoOperacion.error("Error en login")).build();
        }
    }

    // ==============================================================
    // 4. TRAER CUENTAS CON CLIENTES (formato DatosCuenta)
    // ==============================================================
    @GET
    @Path("cuentas")
    public Response traerCuentasConClientes() {
        try {
            List<DatosCuenta> lista = eurekaService.leerCuentasConClientes();
            return Response.ok(lista).build();
        } catch (Exception e) {
            return Response.status(500).entity(ResultadoOperacion.error("Error al obtener cuentas")).build();
        }
    }

    // ==============================================================
    // 5. TIPOS DE MOVIMIENTO PERMITIDOS
    // ==============================================================
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("DEPOSITO", "RETIRO", "TRANSFERENCIA");

    @GET
    @Path("tipos")
    public Response obtenerTiposPermitidos() {
        return Response.ok(TIPOS_PERMITIDOS).build();
    }
}
