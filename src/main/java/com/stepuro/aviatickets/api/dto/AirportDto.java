package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AirportDto {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;
    @NotBlank
    private String city;

    @PositiveOrZero
    private int capacity;

    @Positive
    private int runwayNumber;

}
