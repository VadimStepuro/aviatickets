package com.stepuro.aviatickets.security.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JwtRefreshRequest {
    private String token;
}
