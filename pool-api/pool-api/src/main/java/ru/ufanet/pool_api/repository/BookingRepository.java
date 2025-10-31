package ru.ufanet.pool_api.repository;

import ru.ufanet.pool_api.model.Booking;
import ru.ufanet.pool_api.model.BookingDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BookingRepository {
    long save(Booking booking);

    Map<LocalDateTime, Integer> findReservedTimeSlots(LocalDate date);

    List<BookingDetails> findByDate(LocalDate date);

    void delete(long clientId, LocalDateTime time, String reason);

    void lock(int key);

    int count(LocalDateTime dateTime);

    boolean exists(Booking booking);
}
