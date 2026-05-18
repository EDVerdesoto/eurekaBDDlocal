package ec.edu.monster.controladores; 

import ec.edu.monster.models.Usuario;
import ec.edu.monster.views.DashboardVista;
import ec.edu.monster.views.LoginVista;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component 
public class ConsoleRunner implements CommandLineRunner {

    private final LoginVista loginVista;
    private final DashboardVista dashboardVista;

    public ConsoleRunner(LoginVista loginVista, DashboardVista dashboardVista) {
        this.loginVista = loginVista;
        this.dashboardVista = dashboardVista;
    }

    @Override
    public void run(String... args) throws Exception {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║  Cliente SOAP - EuBank (Consola)          ║"); // <-- ¡CAMBIO!
        System.out.println("╚═══════════════════════════════════════════╝");

        try {
            // ¡Esta lógica es IDÉNTICA a la de tu antiguo main!
            Usuario usuario = loginVista.mostrarLogin();

            if (usuario != null && usuario.isAutenticado()) {
                dashboardVista.mostrarMenuPrincipal(); // <-- ¡CAMBIO!
            } else {
                System.out.println("\nNo se pudo autenticar. Saliendo del sistema...");
            }
        } catch (Exception e) {
            mostrarError("Error crítico: " + e.getMessage());
        }
        
        System.out.println("\nPresione cualquier tecla para salir...");
        System.in.read();
        System.exit(0); // Cierra la aplicación
    }
    
    // (Puedes mover el 'mostrarError' aquí si quieres)
    private void mostrarError(String mensaje) {
         System.out.print("\u001B[31m");
         System.out.println("\n✗ " + mensaje);
         System.out.print("\u001B[0m");
    }
}