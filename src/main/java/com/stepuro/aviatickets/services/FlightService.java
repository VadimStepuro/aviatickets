package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.annotations.Loggable;
import com.stepuro.aviatickets.api.dto.*;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.repositories.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;


    @Cacheable(cacheNames = "flights")
    public List<FlightDto> findAll(){
        return flightRepository
                .findAll()
                .stream()
                .map(FlightMapper.INSTANCE::flightToFlightDto)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "flight", key = "#id")
    public FlightDto findById(UUID id) {
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Flight with id " + id + " not found"));
        return FlightMapper.INSTANCE.flightToFlightDto(flight);
    }

    public List<FlightDto> findAllByDepartureAirportAndArrivalAirport(AirportDto departureAirportDto, AirportDto arrivalAirportDto){
        Airport departureAirport = AirportMapper.INSTANCE.airportDtoToAirport(departureAirportDto);
        Airport arrivalAirport = AirportMapper.INSTANCE.airportDtoToAirport(arrivalAirportDto);

        return flightRepository
                .findAllByDepartureAirportAndArrivalAirport(departureAirport, arrivalAirport)
                .stream()
                .map(FlightMapper.INSTANCE::flightToFlightDto)
                .collect(Collectors.toList());

    }

    public List<CityCount> findTop10Cities(){
        return flightRepository.countTotalFlightsByArrivalAirport();
    }

    @Caching(
            put = {
                    @CachePut(cacheNames = "flight", key = "#flightDto.id")
            },
            evict = {
                    @CacheEvict(cacheNames = "flights", allEntries = true)
    }
    )
    public FlightDto create(FlightDto flightDto){
        Flight flight = FlightMapper.INSTANCE.flightDtoToFlight(flightDto);
        return FlightMapper.INSTANCE.flightToFlightDto(flightRepository.save(flight));
    }

    @Caching(
            put = {
                    @CachePut(cacheNames = "flight", key = "#flightDto.id")
            },
            evict = {
                    @CacheEvict(cacheNames = "flights", allEntries = true)
            }
    )
    public FlightDto edit(FlightDto flightDto) {
        Flight findedFlight = flightRepository
                .findById(flightDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight with id " + flightDto.getId() + " not flound"));

        findedFlight.setAirplane(AirplaneMapper.INSTANCE.airplaneDtoToAirplane(flightDto.getAirplane()));
        findedFlight.setArrivalAirport(AirportMapper.INSTANCE.airportDtoToAirport(flightDto.getArrivalAirport()));
        findedFlight.setDepartureAirport(AirportMapper.INSTANCE.airportDtoToAirport(flightDto.getDepartureAirport()));
        findedFlight.setArrivalDate(flightDto.getArrivalDate());
        findedFlight.setDepartureDate(flightDto.getDepartureDate());

        return FlightMapper.INSTANCE.flightToFlightDto(flightRepository.save(findedFlight));
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "flights", allEntries = true),
                    @CacheEvict(cacheNames = "flight", key = "#id")
            }
    )
    public void delete(UUID id){
        flightRepository.deleteById(id);
    }

}

