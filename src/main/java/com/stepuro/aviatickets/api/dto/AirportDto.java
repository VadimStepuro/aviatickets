package com.stepuro.aviatickets.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRunwayNumber() {
        return runwayNumber;
    }

    public void setRunwayNumber(int runwayNumber) {
        this.runwayNumber = runwayNumber;
    }
}
