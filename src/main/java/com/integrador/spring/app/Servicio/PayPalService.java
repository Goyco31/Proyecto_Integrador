package com.integrador.spring.app.Servicio;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

        // inyecion del api de pago
        @Autowired
        private APIContext apiContext;

        // metodo para crear el pago
        public Payment crearPago(BigDecimal total, String currency, String method, String intent,
                        String description,
                        String cancelUrl, String successsUrl) throws PayPalRESTException {

                // monto para pagar
                Amount amount = new Amount();
                amount.setCurrency(currency);
                amount.setTotal(String.format("%.2f", total));

                // crea una transacción y se asocia con el monto y una descripción
                Transaction transaction = new Transaction();
                transaction.setDescription(description);
                // vincula el monto con la transacción
                transaction.setAmount(amount);

                // agrega la transacción a una lista
                List<Transaction> transactions = new ArrayList<>();
                transactions.add(transaction);

                // indica el metodo de pago
                Payer payer = new Payer();
                payer.setPaymentMethod(method);

                // crea el objeto Payment que es el núcleo de la operación
                Payment payment = new Payment();
                // Establece la intención del pago: "sale", "authorize" o "order"
                payment.setIntent(intent);
                // Se asocia el pagador
                payment.setPayer(payer);
                // Se asocia la lista de transacciones
                payment.setTransactions(transactions);

                // establece las rutas de redireccionamiento
                RedirectUrls redirectUrls = new RedirectUrls();
                redirectUrls.setCancelUrl(cancelUrl);
                redirectUrls.setReturnUrl(successsUrl);

                // agregan las rutas de redirección al objeto Payment
                payment.setRedirectUrls(redirectUrls);

                // Se realiza la llamada a PayPal para crear el pago usando el contexto
                // autenticado
                return payment.create(apiContext);
        }

        // metodo de ejecutar el pago
        public Payment ejecutarPago(String paymentId, String payerId) throws PayPalRESTException {

                //crea una instancia del objeto Payment con el ID del pago
                Payment payment = new Payment();
                //establece el ID del pago que se desea ejecutar
                payment.setId(paymentId); 

                //representa la acción de confirmar el pago
                PaymentExecution paymentExecution = new PaymentExecution();
                 //establece el ID del pagador
                paymentExecution.setPayerId(payerId);

                // ejecuta el pago usando el APIContext previamente configurado
                return payment.execute(apiContext, paymentExecution);
        }
}