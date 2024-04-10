package com.stepuro.aviatickets.models;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CityCount {
    private String city;

    private Long count;

}
