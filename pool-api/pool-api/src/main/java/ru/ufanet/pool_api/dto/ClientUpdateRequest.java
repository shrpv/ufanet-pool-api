package ru.ufanet.pool_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientUpdateRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone is required")
    private String phone;
}
