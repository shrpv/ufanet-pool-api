package ru.ufanet.pool_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimeSlotDTO {
    private LocalDateTime time;
    private int count;
}
