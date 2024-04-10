package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
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

}
