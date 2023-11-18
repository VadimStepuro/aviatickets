package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
    Privilege findByName(String name);
}
