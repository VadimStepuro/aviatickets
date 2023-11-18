package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.models.Ticket;
import com.stepuro.aviatickets.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByFlightClassAndFlight(String flightClass, Flight flight);

    List<Ticket> findAllByFlightDepartureAirportCityAndFlightArrivalAirportCityAndFlightDepartureDateBetween(String departureCity, String arrivalCity, Date startDate, Date endDate);

}
