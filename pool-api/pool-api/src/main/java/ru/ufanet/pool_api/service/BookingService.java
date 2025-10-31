package ru.ufanet.pool_api.service;

import ru.ufanet.pool_api.dto.BookingDTO;
import ru.ufanet.pool_api.dto.CancelBookingRequest;
import ru.ufanet.pool_api.dto.CreateBookingRequest;
import ru.ufanet.pool_api.dto.TimeSlotDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BookingService {
    List<TimeSlotDTO> getAvailableTimeSlots(LocalDate date);

    List<BookingDTO> getBookings(LocalDate date);

    Map<String, String> createBooking(CreateBookingRequest request);

    void cancelBooking(CancelBookingRequest request);
}
