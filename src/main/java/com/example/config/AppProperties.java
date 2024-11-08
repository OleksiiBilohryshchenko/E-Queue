package com.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties("app")
public class AppProperties {

    private Duration cycleDuration;
    private Duration bookingRoundDuration;

}
