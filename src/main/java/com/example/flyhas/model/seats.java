package com.yourcompany.flyhas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber; 
    private boolean booked;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private String userEmail; 
}
