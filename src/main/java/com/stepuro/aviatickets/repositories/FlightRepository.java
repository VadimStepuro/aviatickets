package com.stepuro.aviatickets.repositories;

import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {
    List<Flight> findAllByDepartureAirportAndArrivalAirport(Airport departureAirport, Airport arrivalAirport);


    @Query("select new com.stepuro.aviatickets.models.CityCount(f.arrivalAirport.city, count(f.arrivalAirport.city)) " +
            "from Flight AS f GROUP BY f.arrivalAirport.city ORDER BY count(f.arrivalAirport.city) DESC LIMIT 20")
    List<CityCount> countTotalFlightsByArrivalAirport();
}
