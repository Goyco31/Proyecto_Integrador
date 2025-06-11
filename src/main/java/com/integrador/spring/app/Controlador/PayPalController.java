package com.integrador.spring.app.Controlador;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.integrador.spring.app.DTO.OrderRequest;
import com.integrador.spring.app.Servicio.PayPalService;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @PostMapping("/create-order")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            return payPalService.createOrder(orderRequest.getCurrency(), orderRequest.getAmount());
        } catch (Exception e) {
            return "Error al crear orden: " + e.getMessage();
        }
    }

    @GetMapping("/success")
    public String success() {
        return "Pago exitoso!";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "Pago cancelado.";
    }
}
