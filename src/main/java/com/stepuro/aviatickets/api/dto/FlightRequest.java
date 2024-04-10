package com.stepuro.aviatickets.api.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class FlightRequest {
    private AirportDto departureAirport;
    private AirportDto arrivalAirport;
    private Date departureDate;
    private Date arrivalDate;
}
