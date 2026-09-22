package com.example.payment.service;

import com.example.payment.model.SeatAllocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessingService {

    private static final Logger log = LoggerFactory.getLogger(PaymentProcessingService.class);

    public void processPayment(SeatAllocationEvent event, String correlationId) {
        log.info("[PaymentService] Processing Payment for {}. CorrelationID: {}", event.getCinemaBookingId(), correlationId);

        if (event.getTotalPrice() > 0) {
            log.info("[PaymentService] Payment success: {} VND. CorrelationID: {}", (long) event.getTotalPrice(), correlationId);
        }
    }
}
