package com.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSlotRepository extends CrudRepository<BookingSlot, String> {
}
