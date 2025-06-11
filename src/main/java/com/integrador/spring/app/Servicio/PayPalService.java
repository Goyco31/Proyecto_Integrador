package com.integrador.spring.app.Servicio;

import com.integrador.spring.app.Modelo.PayPalClient;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private PayPalClient payPalClient;

    public String createOrder(String currencyCode, String amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .brandName("Mi App SpringBoot")
                .landingPage("BILLING")
                .cancelUrl("http://localhost:8080/paypal/cancel")
                .returnUrl("http://localhost:8080/paypal/success")
                .userAction("PAY_NOW");

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(new PurchaseUnitRequest()
                .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode(currencyCode)
                        .value(amount)));

        orderRequest.purchaseUnits(purchaseUnits);
        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest request = new OrdersCreateRequest()
                .requestBody(orderRequest);

        Order order = payPalClient.getClient().execute(request).result();
        return order.links().stream()
                .filter(link -> "approve".equals(link.rel()))
                .findFirst()
                .map(LinkDescription::href)
                .orElseThrow(() -> new RuntimeException("No se encontró el enlace de aprobación de PayPal"));
    }
}