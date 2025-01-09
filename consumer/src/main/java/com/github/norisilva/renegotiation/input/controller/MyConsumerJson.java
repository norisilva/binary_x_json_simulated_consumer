package com.github.norisilva.renegotiation.input.controller;

import com.github.norisilva.renegotiation.domain.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Path("/consumerJson")
public class MyConsumerJson {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response handler(String json) {

        int originalSize = json.length();
        LocalDateTime receivedTime = LocalDateTime.now();
        long startTime = System.nanoTime();

        Payment payment = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            payment = objectMapper.readValue(json, Payment.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        long endTime = System.nanoTime();
        long deserializationTime = endTime - startTime;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTime = receivedTime.format(formatter);

        System.out.println("Tamanho recebido: " + originalSize);
        System.out.println("Recebi em: " + formattedTime);
        System.out.println("Demorei para deserializar (ns): " + deserializationTime);

        return Response.ok().build();
    }
}