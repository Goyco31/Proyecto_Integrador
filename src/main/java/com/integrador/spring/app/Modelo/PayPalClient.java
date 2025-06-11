package com.integrador.spring.app.Modelo;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.stereotype.Component;

@Component
public class PayPalClient {

    // Reemplaza con tus credenciales reales de PayPal Developer
    private static final String CLIENT_ID = "TU_CLIENT_ID";
    private static final String CLIENT_SECRET = "TU_CLIENT_SECRET";

    private final PayPalEnvironment environment;
    private final PayPalHttpClient client;

    public PayPalClient() {
        this.environment = new PayPalEnvironment.Sandbox(CLIENT_ID, CLIENT_SECRET);
        this.client = new PayPalHttpClient(environment);
    }

    public PayPalHttpClient getClient() {
        return client;
    }
}