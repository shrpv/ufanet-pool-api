package ru.ufanet.pool_api.controller;

import org.springframework.http.ResponseEntity;
import ru.ufanet.pool_api.dto.BookingDTO;
import ru.ufanet.pool_api.dto.CancelBookingRequest;
import ru.ufanet.pool_api.dto.CreateBookingRequest;
import ru.ufanet.pool_api.dto.TimeSlotDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingController {
    ResponseEntity<List<TimeSlotDTO>> getAvailableTimeSlots(LocalDate date);

    ResponseEntity<List<BookingDTO>> getBookings(LocalDate date);

    ResponseEntity<Map<String, String>> createBooking(CreateBookingRequest request);

    ResponseEntity<Void> cancelBooking(CancelBookingRequest request);
}
