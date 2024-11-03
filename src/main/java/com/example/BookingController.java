package com.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingSlotRepository bookingSlotRepository;

    @GetMapping("/api/booking/slots")
    public List<BookingSlot> getBookingSlots() {
        return StreamSupport.stream(bookingSlotRepository.findAll().spliterator(),false)
                .sorted(Comparator.comparing(BookingSlot::startTime))
                .toList();

    }

}
