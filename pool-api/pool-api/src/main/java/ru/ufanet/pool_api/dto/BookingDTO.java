package ru.ufanet.pool_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class BookingDTO {
    private LocalDateTime time;
    private int count;
    private List<ClientDTO> clients;
}
