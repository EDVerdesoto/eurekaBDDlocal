package ec.edu.monster.views;

import ec.edu.monster.models.Usuario;
import ec.edu.monster.servicios.EuBankService; 
import java.util.Scanner;
import org.springframework.stereotype.Component; 

@Component 
public class LoginVista {

    private final EuBankService euBankService;
    private final Scanner scanner;

    public LoginVista(EuBankService euBankService) {
        this.euBankService = euBankService;
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    public Usuario mostrarLogin() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         Sistema de Login - EuBank        ║"); // <-- ¡CAMBIO!
        System.out.println("╚══════════════════════════════════════════╝");

        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            System.out.print("\nUsuario: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                mostrarError("El usuario no puede estar vacío.");
                continue;
            }

            System.out.print("Contraseña: ");
            String password = scanner.nextLine().trim();

            if (password.isEmpty()) {
                mostrarError("La contraseña no puede estar vacía.");
                continue;
            }

            System.out.print("\nAutenticando (SOAP)...");
            Usuario usuario = new Usuario(username, password);
            try {
                if (euBankService.iniciarSesion(usuario.getUsername(), usuario.getPassword())) {
                    System.out.println(" ✓");
                    System.out.println("\nAutenticación exitosa. Bienvenido, " + username + "!");
                    usuario.setAutenticado(true); 
                    return usuario;
                } else {
                    System.out.println(" ✗");
                    intentos++;
                    String mensaje = "Autenticación fallida. Intente de nuevo. (Intento " + intentos + "/" + MAX_INTENTOS + ")";
                    mostrarError(mensaje);
                    if (intentos >= MAX_INTENTOS) {
                        mostrarError("Máximo de intentos alcanzado.");
                        return null;
                    }
                    System.out.print("¿Desea intentar de nuevo? (s/n): ");
                    String opcion = scanner.nextLine().trim().toLowerCase();
                    if (!opcion.equals("s")) {
                        return null;
                    }
                }
            } catch (Exception e) {
                System.out.println(" ✗");
                intentos++;
                String mensaje = "Error inesperado: " + e.getMessage() + " (Intento " + intentos + "/" + MAX_INTENTOS + ")";
                mostrarError(mensaje);
                if (intentos >= MAX_INTENTOS) {
                    mostrarError("Máximo de intentos alcanzado.");
                    return null;
                }
                System.out.print("¿Desea intentar de nuevo? (s/n): ");
                String opcion = scanner.nextLine().trim().toLowerCase();
                if (!opcion.equals("s")) {
                    return null;
                }
            }
        }
        return null;
    }


    private void mostrarError(String mensaje) {
        System.out.print("\u001B[31m");
        System.out.println("\n✗ " + mensaje);
        System.out.print("\u001B[0m");
    }


}