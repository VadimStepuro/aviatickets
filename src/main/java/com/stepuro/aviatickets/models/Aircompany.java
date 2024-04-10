package com.stepuro.aviatickets.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "aircompany", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Aircompany {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "country")
    private String country;

}
