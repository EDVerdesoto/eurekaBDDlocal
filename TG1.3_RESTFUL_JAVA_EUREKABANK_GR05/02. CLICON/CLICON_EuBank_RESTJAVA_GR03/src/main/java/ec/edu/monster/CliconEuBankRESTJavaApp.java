/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster;

import ec.edu.monster.model.Usuario;
import ec.edu.monster.views.DashboardVista;
import ec.edu.monster.views.LoginVista;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author joela
 */
@SpringBootApplication
public class CliconEuBankRESTJavaApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CliconEuBankRESTJavaApp.class);
        app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
        app.setLogStartupInfo(true);
        app.run(args);
    }

    // Configurar RestTemplate
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    // Ejecutar interfaz de consola al iniciar
    @Bean
    public CommandLineRunner run(LoginVista loginVista, DashboardVista dashboardVista) {
        return args -> {
            Usuario usuario = loginVista.mostrarLogin();
            
            if (usuario != null && usuario.isAutenticado()) {
                dashboardVista.mostrarMenuPrincipal();
            } else {
                System.out.println("\n  Demasiados intentos fallidos. Saliendo del sistema.");
                System.exit(1);
            }
        };
    }
}
