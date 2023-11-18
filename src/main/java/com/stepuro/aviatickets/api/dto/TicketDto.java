package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.models.FlightClass;
import com.stepuro.aviatickets.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketDto {
    private UUID id;

    @NotBlank
    private FlightDto flight;

    private FlightClass flightClass;

    @Positive
    private double cost;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public FlightDto getFlight() {
        return flight;
    }

    public void setFlight(FlightDto flight) {
        this.flight = flight;
    }

    public FlightClass getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(FlightClass flightClass) {
        this.flightClass = flightClass;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
