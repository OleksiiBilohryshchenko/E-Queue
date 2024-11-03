package com.example;

import com.example.repository.BookingSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer {

    private final BookingSlotRepository bookingSlotRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup(){
        log.info("DataInitializer.onStartup()");
        initializeBookingSlots();
    }

    private void initializeBookingSlots() {
        if(isBookingInitialized()){
            log.info("Booking slots are already initialized");
        }else {


            final LocalTime startTime = LocalTime.of(8, 0);
            final LocalTime endTime = LocalTime.of(20, 0);

            for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
                final BookingSlot bookingSlot = new BookingSlot(time.toString(), time, time.plusMinutes(30));
                bookingSlotRepository.save(bookingSlot);
            }

            stringRedisTemplate.opsForValue().set("booking.initialized", "true");

            log.info("Booking slots initialized");
        }
    }

    private boolean isBookingInitialized() {
        return stringRedisTemplate.hasKey("booking.initialized");
    }

}
