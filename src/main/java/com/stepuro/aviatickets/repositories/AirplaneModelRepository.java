package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Airplane;
import com.stepuro.aviatickets.models.AirplaneModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AirplaneModelRepository extends JpaRepository<AirplaneModel, UUID> {
    AirplaneModel findFirstByName(String name);
}
