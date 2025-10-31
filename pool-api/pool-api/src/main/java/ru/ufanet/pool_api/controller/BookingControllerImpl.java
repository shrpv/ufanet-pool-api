package ru.ufanet.pool_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ufanet.pool_api.dto.BookingDTO;
import ru.ufanet.pool_api.dto.CancelBookingRequest;
import ru.ufanet.pool_api.dto.CreateBookingRequest;
import ru.ufanet.pool_api.dto.TimeSlotDTO;
import ru.ufanet.pool_api.service.BookingService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0/pool")
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {
    private final BookingService bookingService;

    @Override
    @GetMapping("/time-slots/available")
    public ResponseEntity<List<TimeSlotDTO>> getAvailableTimeSlots(@RequestParam LocalDate date) {
        return ResponseEntity.ok(bookingService.getAvailableTimeSlots(date));
    }

    @Override
    @GetMapping("/reservations")
    public ResponseEntity<List<BookingDTO>> getBookings(@RequestParam LocalDate date) {
        return ResponseEntity.ok(bookingService.getBookings(date));
    }

    @Override
    @PostMapping("/reservations")
    public ResponseEntity<Map<String, String>> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @Override
    @PostMapping("/reservations/cancel")
    public ResponseEntity<Void> cancelBooking(
            @Valid @RequestBody CancelBookingRequest request
    ) {
        bookingService.cancelBooking(request);
        return ResponseEntity.noContent().build();
    }
}
