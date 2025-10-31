package ru.ufanet.pool_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Booking {
    private long clientId;
    private LocalDateTime time;
}
