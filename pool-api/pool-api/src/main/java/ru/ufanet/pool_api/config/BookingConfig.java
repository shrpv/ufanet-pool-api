package ru.ufanet.pool_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
@ConfigurationProperties(prefix = "booking")
@Getter
@Setter
public class BookingConfig {
    private int capacity;
    private LocalTime startTime;
    private LocalTime endTime;
}