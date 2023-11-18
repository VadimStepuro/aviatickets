package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Aircompany;
import com.stepuro.aviatickets.models.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AirplaneRepository extends JpaRepository<Airplane, UUID> {

}
