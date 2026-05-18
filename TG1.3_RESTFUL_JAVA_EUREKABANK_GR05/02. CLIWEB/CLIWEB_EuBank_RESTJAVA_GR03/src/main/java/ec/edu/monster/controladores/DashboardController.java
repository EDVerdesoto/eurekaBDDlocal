package ec.edu.monster.controladores;

import ec.edu.monster.model.MovimientoData;
import ec.edu.monster.model.ResultadoOperacion;
import ec.edu.monster.model.DatosCuenta;
import ec.edu.monster.servicios.EuBankService;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {

    private final EuBankService euBankService;

    public DashboardController(EuBankService euBankService) {
        this.euBankService = euBankService;
    }

    public static class CrearMovimientoRequest {
        private String tipo;
        private String cuentaOrigen;
        private String cuentaDestino;
        private double importe;

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getCuentaOrigen() { return cuentaOrigen; }
        public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }
        public String getCuentaDestino() { return cuentaDestino; }
        public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
        public double getImporte() { return importe; }
        public void setImporte(double importe) { this.importe = importe; }
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("usuario") String usuario,
                                @RequestParam("clave") String clave,
                                HttpSession session,
                                Model model) {
        try {
            ResultadoOperacion resultado = euBankService.iniciarSesion(usuario, clave);
            if (resultado.getCodigo() == 1) {
                session.setAttribute("Usuario", usuario);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", resultado.getMensaje());
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error de conexion: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("Usuario") == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", session.getAttribute("Usuario"));
        return "dashboard/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard/get-cuentas")
    @ResponseBody
    public List<DatosCuenta> getCuentas(HttpSession session) {
        return euBankService.traerCuentasConClientes();
    }

    @GetMapping("/dashboard/get-movimientos")
    @ResponseBody
    public List<MovimientoData> getMovimientos(@RequestParam("cuenta") String cuenta) {
        return euBankService.traerMovimientos(cuenta);
    }

    @GetMapping("/dashboard/get-todos-movimientos")
    @ResponseBody
    public List<MovimientoData> getTodosMovimientos() {
        List<DatosCuenta> cuentas = euBankService.traerCuentasConClientes();
        List<MovimientoData> todos = new java.util.ArrayList<>();
        for (DatosCuenta c : cuentas) {
            try {
                todos.addAll(euBankService.traerMovimientos(c.getCodigo()));
            } catch (Exception e) {}
        }
        return todos;
    }

    @GetMapping("/dashboard/get-tipos-movimiento")
    @ResponseBody
    public List<String> getTiposMovimiento() {
        return euBankService.obtenerTipoMovimientosPermitidos();
    }

    @PostMapping("/dashboard/crear-movimiento")
    @ResponseBody
    public Map<String, Object> crearMovimiento(@RequestBody CrearMovimientoRequest req) {
        try {
            ResultadoOperacion res = euBankService.regMovimiento(req);
            return Map.of("success", res.getCodigo() == 1, "message", res.getMensaje());
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}
