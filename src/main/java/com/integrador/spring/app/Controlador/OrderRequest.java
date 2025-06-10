package com.integrador.spring.app.Controlador;

public class OrderRequest {
    private String amount;
    private String currency;

    // Getters y Setters
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}