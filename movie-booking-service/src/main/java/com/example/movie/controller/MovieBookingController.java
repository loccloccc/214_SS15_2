package com.example.movie.controller;

import com.example.movie.model.CinemaBookingRequest;
import com.example.movie.service.BookingPublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class MovieBookingController {

    private final BookingPublisherService bookingPublisherService;

    public MovieBookingController(BookingPublisherService bookingPublisherService) {
        this.bookingPublisherService = bookingPublisherService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createBooking(@RequestBody CinemaBookingRequest request) throws Exception {
        String correlationId = bookingPublisherService.publishBooking(request);
        return ResponseEntity.ok(Map.of(
                "status", "ACCEPTED",
                "cinemaBookingId", request.getCinemaBookingId(),
                "correlationId", correlationId
        ));
    }
}
