package com.stepuro.aviatickets.services.implementation;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.stepuro.aviatickets.api.dto.AirportDto;
import com.stepuro.aviatickets.api.dto.FlightDto;
import com.stepuro.aviatickets.api.dto.FlightRequest;
import com.stepuro.aviatickets.api.exeptions.NoPathFoundException;
import com.stepuro.aviatickets.api.exeptions.ResourceNotFoundException;
import com.stepuro.aviatickets.api.mapper.AirplaneMapper;
import com.stepuro.aviatickets.api.mapper.AirportMapper;
import com.stepuro.aviatickets.api.mapper.FlightMapper;
import com.stepuro.aviatickets.models.Airport;
import com.stepuro.aviatickets.models.CityCount;
import com.stepuro.aviatickets.models.Flight;
import com.stepuro.aviatickets.repositories.FlightRepository;
import com.stepuro.aviatickets.services.FlightService;
import jakarta.transaction.Transactional;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.Multigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {
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

    public List<FlightDto> findShortestFlightsPathByDepartureAndArrivalAirport(FlightRequest flightRequest){
        Airport departureAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getDepartureAirport());

        Airport arrivalAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getArrivalAirport());

        Graph<Airport, Flight> graph = createGraph(flightRequest, false);

        DijkstraShortestPath<Airport, Flight> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Airport, Flight> path = dijkstraShortestPath.getPath(departureAirport, arrivalAirport);

        if(path == null){
            throw new NoPathFoundException("No path found for request " + flightRequest);
        }

        return path
                .getEdgeList()
                .stream()
                .map(FlightMapper.INSTANCE::flightToFlightDto)
                .toList();
    }

    public List<FlightDto> findShortestWeightedFlightsPathByDepartureAndArrivalAirport(FlightRequest flightRequest){
        Airport departureAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getDepartureAirport());

        Airport arrivalAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getArrivalAirport());

        Graph<Airport, Flight> graph = createGraph(flightRequest, true);

        DijkstraShortestPath<Airport, Flight> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        GraphPath<Airport, Flight> path = dijkstraShortestPath.getPath(departureAirport, arrivalAirport);

        if(path == null){
            throw new NoPathFoundException("No path found for request " + flightRequest);
        }

        return path
                .getEdgeList()
                .stream()
                .map(FlightMapper.INSTANCE::flightToFlightDto)
                .toList();
    }

    public List<List<FlightDto>> getFlightPathsWhereLengthLessThenPathLength(FlightRequest flightRequest, int pathLength){
        Airport departureAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getDepartureAirport());

        Airport arrivalAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getArrivalAirport());

        Graph<Airport, Flight> graph = createGraph(flightRequest, false);

        AllDirectedPaths<Airport, Flight> allDirectedPaths = new AllDirectedPaths<>(graph);

        List<GraphPath<Airport, Flight>> allPathsWithLengthLessThanPathLength = allDirectedPaths.getAllPaths(
                departureAirport,
                arrivalAirport,
                true,
                pathLength);

        if(allPathsWithLengthLessThanPathLength.isEmpty()){
            throw new NoPathFoundException("No path found for request " + flightRequest + " with length less than " + pathLength);
        }

        List<List<FlightDto>> foundPaths = new ArrayList<>();

        allPathsWithLengthLessThanPathLength.forEach(airportFlightGraphPath ->
            foundPaths.add(airportFlightGraphPath
                    .getEdgeList()
                    .stream()
                    .map(FlightMapper.INSTANCE::flightToFlightDto)
                    .toList())
        );

        return foundPaths;
    }

    public byte[] createTotalFlightGraphImage() throws IOException {
        List<Flight> allFlights = flightRepository.findAll();

        Graph<Airport, Flight> graph = createWeightedGraph(allFlights);

        BufferedImage bufferedImage = visualiseGraph(graph);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);

        return baos.toByteArray();
    }

    public byte[] createFlightGraphImage(FlightRequest flightRequest) throws IOException {
        Graph<Airport, Flight> graph = createGraph(flightRequest, true);

        BufferedImage bufferedImage = visualiseGraph(graph);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);

        return baos.toByteArray();
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

    private Map<Airport, Airport> createSetOfAirport(List<Flight> flights){
        Map<Airport, Airport> airportSet = new HashMap<>();

        for(Flight flight : flights){
            airportSet.put(flight.getArrivalAirport(), flight.getArrivalAirport());
            airportSet.put(flight.getDepartureAirport(), flight.getDepartureAirport());
        }

        return airportSet;
    }

    private Graph<Airport, Flight> createGraph(FlightRequest flightRequest, boolean isWeighted){
        List<Flight> flights = flightRepository.findAllByArrivalDateBetweenAndDepartureDateBetween(
                flightRequest.getDepartureDate(),
                flightRequest.getArrivalDate(),
                flightRequest.getDepartureDate(),
                flightRequest.getArrivalDate()
        );

        Airport departureAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getDepartureAirport());

        Airport arrivalAirport = AirportMapper.INSTANCE.airportDtoToAirport(flightRequest.getArrivalAirport());

        return createGraph(flights);
    }

    private Graph<Airport, Flight> createGraph(List<Flight> flights){
        Graph<Airport, Flight> graph = new Multigraph<>(Flight.class);

        Map<Airport, Airport> airportMap = createSetOfAirport(flights);

        airportMap.forEach((airport, airport2) ->
            graph.addVertex(airport)
        );

        for(Flight flight: flights)
            graph.addEdge(flight.getArrivalAirport(), flight.getDepartureAirport(), flight);

        return graph;
    }

    private Graph<Airport, Flight> createWeightedGraph(List<Flight> flights){
        Multigraph<Airport, Flight> graph = new Multigraph<>(Flight.class);

        Map<Airport, Airport> airportMap = createSetOfAirport(flights);

        airportMap.forEach((airport, airport2) ->
                graph.addVertex(airport));


        for(Flight flight: flights){
            graph.addEdge(flight.getArrivalAirport(), flight.getDepartureAirport(), flight);
            graph.setEdgeWeight(flight, (flight.getArrivalDate().getTime() - flight.getDepartureDate().getTime()));
        }

        return graph;
    }

    private BufferedImage visualiseGraph(Graph<Airport, Flight> graph){
        JGraphXAdapter<Airport, Flight> graphXAdapter = new JGraphXAdapter<>(graph);

        mxIGraphLayout layout = new mxCircleLayout(graphXAdapter);
        layout.execute(graphXAdapter.getDefaultParent());

        return mxCellRenderer.createBufferedImage(
                graphXAdapter,
                null,
                2,
                Color.WHITE,
                true,
                null);
    }
}

