package com.example.seat.consumer;

import com.example.seat.model.CinemaBookingRequest;
import com.example.seat.model.SeatAllocationEvent;
import com.example.seat.producer.SeatConfirmedProducer;
import com.example.seat.service.SeatAllocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class BookingEventConsumer {

    private final SeatAllocationService seatAllocationService;
    private final SeatConfirmedProducer seatConfirmedProducer;
    private final ObjectMapper objectMapper;

    public BookingEventConsumer(SeatAllocationService seatAllocationService,
                                SeatConfirmedProducer seatConfirmedProducer,
                                ObjectMapper objectMapper) {
        this.seatAllocationService = seatAllocationService;
        this.seatConfirmedProducer = seatConfirmedProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "booking-events", groupId = "seat-group")
    public void consume(ConsumerRecord<String, String> record) throws Exception {
        Header header = record.headers().lastHeader("correlationId");
        String correlationId = header != null ? new String(header.value(), StandardCharsets.UTF_8) : "";

        CinemaBookingRequest request = objectMapper.readValue(record.value(), CinemaBookingRequest.class);

        SeatAllocationEvent event = seatAllocationService.allocateSeats(request, correlationId);

        seatConfirmedProducer.publish(event, correlationId);
    }
}
