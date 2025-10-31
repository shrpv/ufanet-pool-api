package ru.ufanet.pool_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ufanet.pool_api.config.BookingConfig;
import ru.ufanet.pool_api.dto.BookingDTO;
import ru.ufanet.pool_api.dto.CancelBookingRequest;
import ru.ufanet.pool_api.dto.CreateBookingRequest;
import ru.ufanet.pool_api.dto.TimeSlotDTO;
import ru.ufanet.pool_api.exception.BadRequestException;
import ru.ufanet.pool_api.mapper.BookingMapper;
import ru.ufanet.pool_api.model.Booking;
import ru.ufanet.pool_api.repository.BookingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingConfig bookingConfig;

    private final BookingRepository bookingRepository;

    @Override
    public List<TimeSlotDTO> getAvailableTimeSlots(LocalDate date) {
        if (LocalDate.now().isAfter(date)) {
            return List.of();
        }
        Map<LocalDateTime, Integer> reservedTimeSlots = bookingRepository.findReservedTimeSlots(date);
        List<TimeSlotDTO> result = new ArrayList<>();

        for (LocalDateTime dateTime = LocalDateTime.of(date, bookingConfig.getStartTime());
             dateTime.isBefore(LocalDateTime.of(date, bookingConfig.getEndTime().plusHours(1)));
             dateTime = dateTime.plusHours(1)
        ) {
            if (LocalDateTime.now().isAfter(dateTime)) {
                continue;
            }

            int availableSlots = bookingConfig.getCapacity() - reservedTimeSlots.getOrDefault(dateTime, 0);

            if (availableSlots > 0) {
                result.add(new TimeSlotDTO(dateTime, availableSlots));
            }
        }
        return result;
    }

    @Override
    public List<BookingDTO> getBookings(LocalDate date) {
        return bookingRepository.findByDate(date).stream().map(BookingMapper::toDTO).toList();
    }

    @Transactional
    @Override
    public Map<String, String> createBooking(CreateBookingRequest request) {
        LocalDateTime dateTime = request.getDateTime();
        if (dateTime.getMinute() != 0 || dateTime.getSecond() != 0 || dateTime.getNano() != 0) {
            throw new BadRequestException("Minutes, seconds, nanos should be 0");
        }

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Booking time is unavailable");
        }

        LocalTime startTime = bookingConfig.getStartTime();
        LocalTime endTime = bookingConfig.getEndTime();
        if (dateTime.toLocalTime().isBefore(startTime) || dateTime.toLocalTime().isAfter(endTime)) {
            throw new BadRequestException("Pool is not working this time");
        }

        if (bookingRepository.exists(new Booking(request.getClientId(), request.getDateTime()))) {
            throw new BadRequestException("Booking already exists");
        }

        bookingRepository.lock(dateTime.hashCode());

        if (bookingRepository.count(dateTime) >= bookingConfig.getCapacity()) {
            throw new BadRequestException("All booking time slots are reserved");
        }

        long clientId = request.getClientId();
        long id = bookingRepository.save(new Booking(clientId, dateTime));
        return Map.of("id", String.valueOf(id));
    }

    @Override
    public void cancelBooking(CancelBookingRequest request) {
        LocalDateTime dateTime = request.getDateTime();

        if (dateTime.getMinute() != 0 || dateTime.getSecond() != 0 || dateTime.getNano() != 0) {
            throw new BadRequestException("Minutes, seconds, nanos should be 0");
        }

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Booking time is unavailable");
        }

        bookingRepository.delete(
                request.getClientId(),
                request.getDateTime(),
                request.getReason()
        );
    }
}
