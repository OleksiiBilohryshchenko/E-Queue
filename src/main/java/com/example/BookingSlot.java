package com.example;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalTime;

@RedisHash ("BookingSlot")
public record BookingSlot(@Id String id, LocalTime startTime, LocalTime endTime) {



}
