package com.stepuro.aviatickets.api.dto;

import com.stepuro.aviatickets.models.FlightClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
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

}
