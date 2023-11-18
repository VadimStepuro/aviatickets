package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.PurchasedTicket;
import com.stepuro.aviatickets.models.Ticket;
import com.stepuro.aviatickets.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PurchasedTicketRepository extends JpaRepository<PurchasedTicket, UUID> {
    List<PurchasedTicket> findAllByUser(User user);
    List<PurchasedTicket> findAllByTicket(Ticket ticket);

    long countByTicket(Ticket ticket);
}
