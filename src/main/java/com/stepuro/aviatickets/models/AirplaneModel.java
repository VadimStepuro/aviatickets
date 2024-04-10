package com.stepuro.aviatickets.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "airplane_model", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AirplaneModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private int weight;

    @Column(name = "economy_seats")
    private int economySeats;

    @Column(name = "business_seats")
    private int businessSeats;

}
