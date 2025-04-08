package com.example.flyhas.controller;

import com.example.flyhas.model.Flight;
import com.example.flyhas.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:5173") // React için 5173 portunu kullanıyoruz
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    // Tüm uçuşları getir
    @GetMapping
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    // Yeni uçuş ekle
    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    // Uçuş sil
    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightRepository.deleteById(id);
    }
}