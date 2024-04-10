package com.stepuro.aviatickets.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "airplane", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aircompany_id", referencedColumnName = "id")
    private Aircompany aircompany;

    @ManyToOne
    @JoinColumn(name = "airplane_model_id", referencedColumnName = "id")
    private AirplaneModel model;

}
