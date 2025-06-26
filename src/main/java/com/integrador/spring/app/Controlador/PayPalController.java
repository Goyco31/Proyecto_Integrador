package com.integrador.spring.app.Controlador;

import java.math.BigDecimal;
import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.integrador.spring.app.Servicio.PayPalService;
import com.integrador.spring.app.Servicio.RecargaServices;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PayPalController {

    private final PayPalService services_paypal;

    @Autowired
    private RecargaServices service_recarga;

    @PostMapping("/pago/crear")
    public RedirectView crearPago(
            @RequestParam(value = "currency", defaultValue = "USD") String currency,
            @RequestParam("idCompra") Integer idCompra,
            @RequestParam("idUser") Integer idUser,
            @RequestParam("precio") BigDecimal precio) {
        try {
            // Validate currency
            try {
                Currency.getInstance(currency);
            } catch (IllegalArgumentException e) {
                log.error("Invalid currency code: {}", currency);
                return new RedirectView("/pago/error");
            }

            String cancelUrl = "http://localhost:8080/pago/cancel";
            String successUrl = "http://localhost:8080/pago/exito";
            log.info(
                    "Creating payment with total: 10.0, currency: {}, method: paypal, intent: sale, description: Payment descripction, cancelUrl: {}, successUrl: {}",
                    currency, cancelUrl, successUrl);
            Payment payment = services_paypal.crearPago(precio, currency, "paypal", "sale", "Payment descripction",
                    cancelUrl, successUrl);

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    String approvalUrl = links.getHref();
                    service_recarga.recargar(idUser, idCompra);
                    log.info("Approval URL: {}", approvalUrl);
                    return new RedirectView(approvalUrl);
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error en crear el pago", e);
        }
        return new RedirectView("/pago/error");
    }

    @GetMapping("/pago/exito")
    public String pagoExitoso(@RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = services_paypal.ejecutarPago(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                return "pagoExito";
            }
        } catch (PayPalRESTException e) {
            log.error("Error:: ", e);
        }
        return "pagoExito";
    }

    @GetMapping("/pago/cancel")
    public String pagoCancel() {
        return "pagoCancel";
    }

    @GetMapping("/pago/error")
    public String pagoError() {
        return "pagoError";
    }

}
