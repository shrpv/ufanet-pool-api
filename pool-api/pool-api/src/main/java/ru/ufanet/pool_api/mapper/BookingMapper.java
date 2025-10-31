package ru.ufanet.pool_api.mapper;

import ru.ufanet.pool_api.dto.BookingDTO;
import ru.ufanet.pool_api.model.Booking;
import ru.ufanet.pool_api.model.BookingDetails;

public class BookingMapper {
    public static BookingDTO toDTO(BookingDetails booking) {
        return new BookingDTO(
                booking.getTime(),
                booking.getCount(),
                booking.getClients().stream().map(ClientMapper::toDTO).toList()
        );
    }
}

