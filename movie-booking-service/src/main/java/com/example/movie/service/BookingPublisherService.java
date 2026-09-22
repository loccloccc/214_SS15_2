package com.example.movie.service;

import com.example.movie.model.CinemaBookingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class BookingPublisherService {

    private static final Logger log = LoggerFactory.getLogger(BookingPublisherService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BookingPublisherService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String publishBooking(CinemaBookingRequest request) throws Exception {
        String correlationId = UUID.randomUUID().toString();

        log.info("[MovieBookingService] Created booking {}. CorrelationID: {}", request.getCinemaBookingId(), correlationId);

        String json = objectMapper.writeValueAsString(request);

        ProducerRecord<String, String> record = new ProducerRecord<>("booking-events", request.getCinemaBookingId(), json);
        record.headers().add(new RecordHeader("correlationId", correlationId.getBytes(StandardCharsets.UTF_8)));

        kafkaTemplate.send(record);

        return correlationId;
    }
}
