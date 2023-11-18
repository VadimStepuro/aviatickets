package com.stepuro.aviatickets.api.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AirplaneDto {

    private UUID id;

    private AircompanyDto aircompany;

    private AirplaneModelDto model;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AircompanyDto getAircompany() {
        return aircompany;
    }

    public void setAircompany(AircompanyDto aircompany) {
        this.aircompany = aircompany;
    }

    public AirplaneModelDto getModel() {
        return model;
    }

    public void setModel(AirplaneModelDto model) {
        this.model = model;
    }

}
