/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster;

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
public class CliwebEuBankRESTJavaApp {
    public static void main(String[] args) {
        SpringApplication.run(CliwebEuBankRESTJavaApp.class, args);
    }

    // ¡AÑADE ESTE MÉTODO!
    // Esto crea el "cliente" que usaremos para llamar a la API REST.
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
