package com.example.seat.service;

import com.example.seat.model.CinemaBookingRequest;
import com.example.seat.model.SeatAllocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeatAllocationService {

    private static final Logger log = LoggerFactory.getLogger(SeatAllocationService.class);

    public SeatAllocationEvent allocateSeats(CinemaBookingRequest request, String correlationId) {
        log.info("[SeatAllocationService] Received SeatRequest for {}. CorrelationID: {}", request.getCinemaBookingId(), correlationId);

        String seatList = String.join(", ", request.getSeatNumbers());
        log.info("[SeatAllocationService] Seat reserved: {}. CorrelationID: {}", seatList, correlationId);

        SeatAllocationEvent event = new SeatAllocationEvent();
        event.setCinemaBookingId(request.getCinemaBookingId());
        event.setMovieCode(request.getMovieCode());
        event.setShowTime(request.getShowTime());
        event.setSeatNumbers(request.getSeatNumbers());
        event.setCustomerEmail(request.getCustomerEmail());
        event.setTotalPrice(request.getTotalPrice());
        event.setStatus("SEAT_RESERVED");

        return event;
    }
}
