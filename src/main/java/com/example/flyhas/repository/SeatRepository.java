package com.example.flyhas.repository;

import com.example.flyhas.model.Seat;
import com.example.flyhas.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlight(Flight flight);
}
