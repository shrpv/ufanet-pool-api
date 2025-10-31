package ru.ufanet.pool_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BookingDetails {
    private List<Client> clients;
    private int count;
    private LocalDateTime time;
}
