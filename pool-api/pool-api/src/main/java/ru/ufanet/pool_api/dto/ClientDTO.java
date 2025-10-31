package ru.ufanet.pool_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientDTO {
    private long id;
    private String name;
    private String email;
    private String phone;
}
