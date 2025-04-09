package com.example.flyhas.controller;

import com.example.flyhas.model.Flight;
import com.example.flyhas.model.Seat;
import com.example.flyhas.repository.FlightRepository;
import com.example.flyhas.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "http://localhost:5173")
public class SeatController {

    private final SeatService seatService;
    private final FlightRepository flightRepository;

    public SeatController(SeatService seatService, FlightRepository flightRepository) {
        this.seatService = seatService;
        this.flightRepository = flightRepository;
    }

    @GetMapping("/flight/{flightId}")
    public List<Seat> getSeatsByFlight(@PathVariable Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));
        return seatService.getSeatsByFlight(flight);
    }

    @PostMapping("/book/{seatId}")
    public Seat bookSeat(@PathVariable Long seatId) {
        return seatService.bookSeat(seatId);
    }

    @PostMapping("/generate/{flightId}")
    public List<Seat> generateSeats(@PathVariable Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        List<Seat> seats = new java.util.ArrayList<>();
        for (char row = 'A'; row <= 'F'; row++) {
            for (int col = 1; col <= 6; col++) {
                Seat seat = new Seat();
                seat.setFlight(flight);
                seat.setSeatNumber(row + String.valueOf(col));
                seat.setBooked(false);
                seats.add(seat);
            }
        }
        return seatService.saveAll(seats);
    }
}
