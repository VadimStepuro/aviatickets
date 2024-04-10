package com.stepuro.aviatickets.api.dto;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AirplaneDto {

    private UUID id;

    private AircompanyDto aircompany;

    private AirplaneModelDto model;

}
