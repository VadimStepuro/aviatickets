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
public class AirplaneModelDto {
    private UUID id;

    @NotBlank
    private String name;

    @Positive
    private int weight;

    @Positive
    private int economySeats;

    @PositiveOrZero
    private int businessSeats;

}

