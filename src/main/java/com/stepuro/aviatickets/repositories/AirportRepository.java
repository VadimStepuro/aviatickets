package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirportRepository extends JpaRepository<Airport, UUID> {
    Airport findFirstByName(String name);
}
