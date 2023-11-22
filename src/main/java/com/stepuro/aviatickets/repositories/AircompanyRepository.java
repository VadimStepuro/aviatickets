package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Aircompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AircompanyRepository extends JpaRepository<Aircompany, UUID> {
    Aircompany findFirstByName(String name);
}
