
package ec.edu.monster.views;

import ec.edu.monster.model.*;
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

        dibujarMarco("  Sistema Bancario - EurekaBank  ");
        System.out.println();

        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            System.out.print("  Usuario: ");
            String username = scanner.nextLine().trim();

            System.out.print("  Contraseña: ");
            String password = scanner.nextLine().trim();

            System.out.print("\n  Autenticando...");
            Usuario usuario = new Usuario(username, password);
            try {
                ResultadoOperacion resultado = euBankService.iniciarSesion(usuario.getUsername(), usuario.getPassword());
                
                if (resultado.getCodigo() == 1) {
                    System.out.println(" [OK]");
                    System.out.println("\n  Bienvenido, " + username + "!");
                    usuario.setAutenticado(true);
                    return usuario;
                } else {
                    System.out.println(" [FALLIDO]");
                    intentos++;
                    mostrarError(resultado.getMensaje() + " (Intento " + intentos + "/" + MAX_INTENTOS + ")");
                }
            } catch (Exception e) {
                System.out.println(" [ERROR]");
                intentos++;
                mostrarError("Error de conexion: " + e.getMessage() + " (Intento " + intentos + "/" + MAX_INTENTOS + ")");
            }
        }
        return null;
    }

    private void mostrarError(String mensaje) {
        System.out.println("  [ERROR] " + mensaje);
        System.out.println();
    }

    private void esperarTecla() {
        System.out.println("\n  Presione Enter para continuar...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
        }
    }

    private void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private void dibujarMarco(String titulo) {
        int ancho = 40;
        System.out.println();
        System.out.println("  " + repiteChar('=', ancho));
        System.out.println("  " + centrarTexto(titulo, ancho));
        System.out.println("  " + repiteChar('=', ancho));
        System.out.println();
    }

    private String repiteChar(char c, int veces) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < veces; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) return texto;
        int espacios = ancho - texto.length();
        int izq = espacios / 2;
        int der = espacios - izq;
        return repiteChar(' ', izq) + texto + repiteChar(' ', der);
    }
}
