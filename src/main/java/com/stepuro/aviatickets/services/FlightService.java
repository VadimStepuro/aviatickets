package com.stepuro.aviatickets.services;

import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.FlightRequest;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.api.mapper.AirplaneMapper;
import com.stepuro.aviatickets.api.mapper.AirportMapper;
import com.stepuro.aviatickets.api.mapper.FlightMapper;
import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.repositories.FlightRepository;
import jakarta.transaction.Transactional;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
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

    public List<FlightDto> findAllByDepartureAirportAndArrivalAirportWithoutTransfer(AirportDto departureAirportDto, AirportDto arrivalAirportDto){
        Airport departureAirport = AirportMapper.INSTANCE.airportDtoToAirport(departureAirportDto);
        Airport arrivalAirport = AirportMapper.INSTANCE.airportDtoToAirport(arrivalAirportDto);

        return flightRepository
                .findAllByDepartureAirportAndArrivalAirport(departureAirport, arrivalAirport)
                .stream()
                .map(FlightMapper.INSTANCE::flightToFlightDto)
                .collect(Collectors.toList());

    }

    public List<FlightDto> findAllFlightsByDepartureAndArrivalAirport(@RequestBody FlightRequest request){
        List<Flight> flights = flightRepository.findAllByArrivalDateBetweenAndDepartureDateBetween(
                request.getDepartureDate(),
                request.getArrivalDate(),
                request.getDepartureDate(),
                request.getArrivalDate()
        );

        Graph<Airport, Flight> graph = createGraph(flights);

        DijkstraShortestPath<Airport, Flight> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        List<Flight> result = new ArrayList<>();



        return null;
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

    private Graph<Airport, Flight> createGraph(List<Flight> flights){
        DefaultDirectedGraph<Airport, Flight> graph = new DefaultDirectedGraph<>(Flight.class);

        Set<Airport> setOfAirport = createSetOfAirport(flights);

        for(Airport airport: setOfAirport){
            graph.addVertex(airport);
        }

        for(Flight flight: flights){
        }

        return graph;
    }

    private Set<Airport> createSetOfAirport(List<Flight> flights){
        Set<Airport> airportSet = new HashSet<>();

        for(Flight flight : flights){
            airportSet.add(flight.getArrivalAirport());
            airportSet.add(flight.getDepartureAirport());
        }

        return airportSet;
    }
}

