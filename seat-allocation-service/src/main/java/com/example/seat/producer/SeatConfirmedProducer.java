package com.example.seat.producer;

import com.example.seat.model.SeatAllocationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SeatConfirmedProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SeatConfirmedProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(SeatAllocationEvent event, String correlationId) throws Exception {
        String json = objectMapper.writeValueAsString(event);

        ProducerRecord<String, String> record = new ProducerRecord<>("seat-confirmed-events", event.getCinemaBookingId(), json);
        record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));

        kafkaTemplate.send(record);
    }
}
