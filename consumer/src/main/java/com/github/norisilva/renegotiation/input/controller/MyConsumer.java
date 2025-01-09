package com.github.norisilva.renegotiation.input.controller;

import com.github.norisilva.renegotiation.domain.Payment;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Path("/consumer")
public class MyConsumer {

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response handler(byte[] data) throws IOException, ClassNotFoundException {

        int originalSize = data.length;
        LocalDateTime receivedTime = LocalDateTime.now();
        long startTime = System.nanoTime();

        deserializePayment(data);

        long endTime = System.nanoTime();
        long deserializationTime = endTime - startTime;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime = receivedTime.format(formatter);


        System.out.println("Tamanho recebido: " + originalSize);

        System.out.println("Recebi em: " + formattedTime);
        System.out.println("Demorei para deserializar (ns): " + deserializationTime);

        return Response.ok().build();
    }

    public Payment deserializePayment(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            return (Payment) in.readObject();
        }
    }
}