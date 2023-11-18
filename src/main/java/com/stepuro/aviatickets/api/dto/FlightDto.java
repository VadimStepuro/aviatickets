package com.stepuro.aviatickets.api.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FlightDto {
    private UUID id;

    @NotBlank
    private AirplaneDto airplane;

    @NotBlank
    private AirportDto arrivalAirport;

    @NotBlank
    private AirportDto departureAirport;

    @NotBlank
    private Date arrivalDate;

    @NotBlank
    private Date departureDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AirplaneDto getAirplane() {
        return airplane;
    }

    public void setAirplane(AirplaneDto airplane) {
        this.airplane = airplane;
    }

    public AirportDto getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(AirportDto arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public AirportDto getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(AirportDto departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
}
