package com.stepuro.aviatickets.api.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FlightRequest {
    private AirportDto departureAirport;
    private AirportDto arrivalAirport;
}
