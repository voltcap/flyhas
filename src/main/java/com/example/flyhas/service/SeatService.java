package com.example.flyhas.service;

import com.example.flyhas.model.Flight;
import com.example.flyhas.model.Seat;
import com.example.flyhas.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsByFlight(Flight flight) {
        return seatRepository.findByFlight(flight);
    }

    public Seat bookSeat(Long seatId) {
        Optional<Seat> optionalSeat = seatRepository.findById(seatId);
        if (optionalSeat.isPresent()) {
            Seat seat = optionalSeat.get();
            if (!seat.isBooked()) {
                seat.setBooked(true);
                return seatRepository.save(seat);
            } else {
                throw new RuntimeException("Seat already booked");
            }
        } else {
            throw new RuntimeException("Seat not found");
        }
    }

    public List<Seat> saveAll(List<Seat> seats) {
        return seatRepository.saveAll(seats);
    }
}
