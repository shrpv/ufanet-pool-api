package ru.ufanet.pool_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {
    @NotNull
    private Long clientId;
    @NotNull
    private LocalDateTime dateTime;
}
