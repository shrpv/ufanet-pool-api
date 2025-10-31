package ru.ufanet.pool_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientNameDTO {
    private long id;
    private String name;
}
