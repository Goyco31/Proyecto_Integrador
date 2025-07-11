package com.integrador.spring.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;

@Configuration
public class PayPalConfig {
    //obtiene los valores de la cuenta de paypal usada para recibir los pagos de los usuarios
    @Value("${paypal.client.id}")
    private String clienteId;

    @Value("${paypal.client.secret}")
    private String clienteSecret;

    @Value("${paypal.mode}")
    private String modo;

    @Bean
    public APIContext apiContext() {
        return new APIContext(clienteId, clienteSecret, modo);
    }
}
