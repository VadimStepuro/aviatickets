package com.stepuro.aviatickets.api.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AircompanyDto {
        private UUID id;
        @NotBlank(message = "field name is mandatory")
        private String name;
        private String country;

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
}
