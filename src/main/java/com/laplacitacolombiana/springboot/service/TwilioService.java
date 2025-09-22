package com.laplacitacolombiana.springboot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account_sid}")
    String accountSid = System.getenv("TWILIO_ACCOUNT_SID");

    @Value("${twilio.auth_token}")
    String authToken = System.getenv("TWILIO_AUTH_TOKEN");

    @Value("${twilio.whatsapp_from}")
    private String fromNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void enviarConfirmacion(String numeroCliente, String nombre, String idTransaccion, BigDecimal total) {
        String cuerpo = String.format(
                "Hola %s 👋, gracias por tu compra!\n\n" +
                        "ID Transacción: %s\n" +
                        "Total: $%.2f\n\n" +
                        "Tu factura: http://127.0.0.1:5501/src/pages/factura.html?id=%s",
                nombre, idTransaccion, total, idTransaccion
        );

        String telefono = numeroCliente.startsWith("+") ? numeroCliente : "+" + numeroCliente;
        String toNumber = "whatsapp:" + telefono;
        System.out.println("Número al que voy a enviar: " + toNumber);

        try {
            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    cuerpo
            ).create();

            System.out.println("Mensaje enviado con SID: " + message.getSid());
            System.out.println("Estado del mensaje: " + message.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}