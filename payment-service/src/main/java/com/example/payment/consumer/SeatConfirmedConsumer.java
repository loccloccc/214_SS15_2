package com.example.payment.consumer;

import com.example.payment.model.SeatAllocationEvent;
import com.example.payment.service.PaymentProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SeatConfirmedConsumer {

    private final PaymentProcessingService paymentProcessingService;
    private final ObjectMapper objectMapper;

    public SeatConfirmedConsumer(PaymentProcessingService paymentProcessingService, ObjectMapper objectMapper) {
        this.paymentProcessingService = paymentProcessingService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "seat-confirmed-events", groupId = "payment-group")
    public void consume(ConsumerRecord<String, String> record) throws Exception {
        Header header = record.headers().lastHeader("correlationId");
        String correlationId = header != null ? new String(header.value(), StandardCharsets.UTF_8) : "";

        SeatAllocationEvent event = objectMapper.readValue(record.value(), SeatAllocationEvent.class);

        paymentProcessingService.processPayment(event, correlationId);
    }
}
