/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.controladores;

/**
 *
 * @author joela
 */
import ec.edu.monster.servicios.EuBankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final EuBankService euBankService;

    public AuthController(EuBankService euBankService) {
        this.euBankService = euBankService;
    }

    // Muestra la página de login
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // Si ya está en sesión, lo mandamos al dashboard
        if (session.getAttribute("Usuario") != null) {
            return "redirect:/dashboard";
        }
        return "login"; // Muestra login.html
    }

    // Redirige la raíz "/" a "/login"
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // Procesa el formulario de login
    @PostMapping("/login")
    public String handleLogin(@RequestParam(required = false) String usuario,
                              @RequestParam(name = "clave", required = false) String contrasena,
                              HttpSession session,
                              Model model) {
        if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
            model.addAttribute("error", "Debe ingresar usuario y contraseña.");
            return "login";
        }

        try {
            boolean resultado = euBankService.iniciarSesion(usuario, contrasena);

            if (resultado) {
                // Guardamos el usuario en la sesión
                session.setAttribute("Usuario", usuario);
                return "redirect:/dashboard"; // Redirige al dashboard
            } else {
                model.addAttribute("error", "Usuario o contraseña incorrectos");
                return "login"; // Vuelve a mostrar login.html con error
            }
        } catch (Exception ex) {
            model.addAttribute("error", "Error al conectar con el servicio: " + ex.getMessage());
            return "login";
        }
    }

    // Procesa el logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Borra la sesión
        return "redirect:/login";
    }
}
