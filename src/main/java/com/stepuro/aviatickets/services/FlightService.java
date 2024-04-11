package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.FlightRequest;
import com.stepuro.aviatickets.models.CityCount;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FlightService{
    List<FlightDto> findAll();

    FlightDto findById(UUID id);

    List<FlightDto> findAllByDepartureAirportAndArrivalAirportWithoutTransfer(AirportDto departureAirportDto, AirportDto arrivalAirportDto);

    List<FlightDto> findShortestFlightsPathByDepartureAndArrivalAirport(FlightRequest flightRequest);

    List<FlightDto> findShortestWeightedFlightsPathByDepartureAndArrivalAirport(FlightRequest flightRequest);

    List<List<FlightDto>> getFlightPathsWhereLengthLessThenPathLength(FlightRequest flightRequest, int pathLength);

    byte[] createTotalFlightGraphImage() throws IOException;

    byte[] createFlightGraphImage(FlightRequest flightRequest) throws IOException;

    List<CityCount> findTop10Cities();

    FlightDto create(FlightDto flightDto);

    FlightDto edit(FlightDto flightDto);

    void delete(UUID id);
}
