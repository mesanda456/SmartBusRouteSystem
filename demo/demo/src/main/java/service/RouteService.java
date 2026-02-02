package service;


import algorithms.BFS;
import algorithms.Dijkstra;
import graph.Graph;
import model.BusStop;
import model.RouteRequest;
import model.RouteResponse;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for route planning logic
 */
@Service
public class RouteService {

    private Graph graph;
    private List<BusStop> allStops;

    /**
     * Initialize graph with bus stops and routes
     * This runs when the application starts
     */
    @PostConstruct
    public void initializeGraph() {
        graph = new Graph();
        allStops = new ArrayList<>();

        // Create bus stops (Colombo area - replace with your actual data)
        BusStop stopA = new BusStop("A", "Fort Railway Station", 6.9344, 79.8428);
        BusStop stopB = new BusStop("B", "Pettah Market", 6.9387, 79.8550);
        BusStop stopC = new BusStop("C", "Kollupitiya Junction", 6.9147, 79.8501);
        BusStop stopD = new BusStop("D", "Bambalapitiya", 6.8934, 79.8553);
        BusStop stopE = new BusStop("E", "Dehiwala Zoo", 6.8569, 79.8742);
        BusStop stopF = new BusStop("F", "Mount Lavinia", 6.8389, 79.8630);

        // Add stops to graph
        allStops.add(stopA);
        allStops.add(stopB);
        allStops.add(stopC);
        allStops.add(stopD);
        allStops.add(stopE);
        allStops.add(stopF);

        allStops.forEach(graph::addStop);

        // Add routes (bidirectional)
        // Format: (stop1, stop2, distance_km, time_min, cost_rs, is_safe)
        graph.addBidirectionalRoute(stopA, stopB, 3, 15, 50, true);
        graph.addBidirectionalRoute(stopA, stopC, 2, 10, 40, true);
        graph.addBidirectionalRoute(stopB, stopD, 4, 20, 60, false);  // Unsafe route
        graph.addBidirectionalRoute(stopC, stopD, 1, 8, 30, true);
        graph.addBidirectionalRoute(stopD, stopE, 3, 18, 55, true);
        graph.addBidirectionalRoute(stopD, stopF, 5, 25, 70, true);

        System.out.println("✅ Graph initialized with " + allStops.size() + " bus stops");
    }

    /**
     * Get all available bus stops
     */
    public List<BusStop> getAllBusStops() {
        return allStops;
    }

    /**
     * Find optimal route between two stops
     */
    public RouteResponse findRoute(RouteRequest request) {

        // Validate input
        if (request.getSource() == null || request.getDestination() == null) {
            RouteResponse response = new RouteResponse();
            response.setFound(false);
            response.setMessage("Source and destination are required");
            return response;
        }

        // Find bus stops
        BusStop source = graph.findStopById(request.getSource());
        BusStop destination = graph.findStopById(request.getDestination());

        if (source == null || destination == null) {
            RouteResponse response = new RouteResponse();
            response.setFound(false);
            response.setMessage("Invalid source or destination");
            return response;
        }

        if (source.equals(destination)) {
            RouteResponse response = new RouteResponse();
            response.setFound(false);
            response.setMessage("Source and destination cannot be the same");
            return response;
        }

        // Execute algorithm
        String algorithm = request.getAlgorithm() != null ?
                request.getAlgorithm().toLowerCase() : "dijkstra";

        if ("bfs".equals(algorithm)) {
            return BFS.getRoute(graph, source, destination);
        } else {
            String mode = request.getMode() != null ? request.getMode() : "distance";
            boolean emergency = request.isEmergencyOnly();
            return Dijkstra.getRoute(graph, source, destination, mode, emergency);
        }
    }
}