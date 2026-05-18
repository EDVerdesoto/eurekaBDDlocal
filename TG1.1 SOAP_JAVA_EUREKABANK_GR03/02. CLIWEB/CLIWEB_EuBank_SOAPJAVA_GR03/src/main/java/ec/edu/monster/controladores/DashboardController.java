package ec.edu.monster.controladores;

import ec.edu.monster.servicios.EuBankService;
import ec.edu.monster.ws.eurekabank.DatosCuenta;
import ec.edu.monster.ws.eurekabank.MovimientoData;
import ec.edu.monster.ws.eurekabank.ResultadoOperacion;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final EuBankService euBankService;

    public DashboardController(EuBankService euBankService) {
        this.euBankService = euBankService;
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("Usuario") == null) {
            return "redirect:/login";
        }
        model.addAttribute("pageTitle", "Panel de Control");
        return "dashboard/index";
    }

    @GetMapping("/get-cuentas")
    @ResponseBody
    public Object getCuentas() {
        try {
            return euBankService.traerCuentasConClientes();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return response;
        }
    }

    @GetMapping("/get-movimientos")
    @ResponseBody
    public Object getMovimientos(@RequestParam String cuenta) {
        try {
            return euBankService.traerMovimientos(cuenta);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return response;
        }
    }

    @GetMapping("/get-todos-movimientos")
    @ResponseBody
    public Object getTodosMovimientos() {
        try {
            return euBankService.traerTodosMovimientos();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return response;
        }
    }

    @GetMapping("/get-tipos-movimiento")
    @ResponseBody
    public Object getTiposMovimiento() {
        try {
            return euBankService.obtenerTipoMovimientosPermitidos();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            return response;
        }
    }

    @PostMapping("/crear-movimiento")
    @ResponseBody
    public Map<String, Object> crearMovimiento(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        try {
            String tipo = (String) payload.get("tipo");
            String cuentaOrigen = (String) payload.get("cuentaOrigen");
            String cuentaDestino = (String) payload.get("cuentaDestino");
            Number importeNum = (Number) payload.get("importe");
            double importe = importeNum != null ? importeNum.doubleValue() : 0.0;

            ResultadoOperacion resultado = euBankService.regMovimiento(tipo, cuentaOrigen, cuentaDestino, importe);
            int codigo = resultado != null ? resultado.getCodigo() : -1;
            if (codigo == 1 || codigo == 0) {
                response.put("success", true);
            } else {
                response.put("success", false);
            }
            response.put("message", resultado != null ? resultado.getMensaje() : "Error desconocido");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}
