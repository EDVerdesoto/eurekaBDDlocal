package ec.edu.monster;

import ec.edu.monster.servicios.EuBankService;
import ec.edu.monster.vista.login;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootApplication
public class ClicescEuBankRESTJavaApp {
    public static void main(String[] args) {
        // Configurar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar Spring Boot en modo headless (sin servidor web)
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ClicescEuBankRESTJavaApp.class)
                .headless(false)
                .run(args);

        // Obtener el servicio del contexto de Spring
        EuBankService euBankService = context.getBean(EuBankService.class);

        // Mostrar el login en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            login loginForm = new login(euBankService);
            loginForm.setVisible(true);
        });
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
