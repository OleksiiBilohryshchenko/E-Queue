package com.example.controller;

import com.example.BookingSlot;
import com.example.repository.BookingSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingSlotRepository bookingSlotRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @GetMapping("/api/booking/slots")
    public List<BookingSlot> getBookingSlots() {
        return StreamSupport.stream(bookingSlotRepository.findAll().spliterator(),false)
                .sorted(Comparator.comparing(BookingSlot::startTime))
                .toList();

    }

    @PostMapping("/api/booking/book")
    public ResponseEntity<?> bookSlot (@RequestParam String slotId, @AuthenticationPrincipal OAuth2User oAuth2User) {
        final String userId = oAuth2User.getAttribute("id").toString();

        final Boolean hasBooked =stringRedisTemplate.opsForSet().isMember("booked_users", userId);
        if (Boolean.TRUE.equals(hasBooked)){
            log.debug("User {} has already booked a slot", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has already booked a slot");
        }

        stringRedisTemplate.opsForSet().add("booked_users", userId);

        final String slotKey = "slot: " + slotId + " :users";
        stringRedisTemplate.opsForSet().add(slotKey, userId);

        log.info("User {} booked slot {}", userId, slotId);

        return ResponseEntity.ok("Selection successful");

    }

}
