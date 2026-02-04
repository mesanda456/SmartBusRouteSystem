package com.example.bus_router_planner.service;

import com.example.bus_router_planner.algorithms.*;
import com.example.bus_router_planner.graph.Graph;
import com.example.bus_router_planner.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {

    private Graph graph;
    private List<BusStop> allStops;

    @Autowired
    private RoadSegmentService roadSegmentService;

    @PostConstruct
    public void initializeGraph() {

        graph = new Graph();
        allStops = new ArrayList<>();

        BusStop stopA = new BusStop("A", "Fort Railway Station", 6.9344, 79.8428);
        BusStop stopB = new BusStop("B", "Pettah Market", 6.9387, 79.8550);
        BusStop stopC = new BusStop("C", "Kollupitiya Junction", 6.9147, 79.8501);
        BusStop stopD = new BusStop("D", "Bambalapitiya", 6.8934, 79.8553);
        BusStop stopE = new BusStop("E", "Dehiwala Zoo", 6.8569, 79.8742);
        BusStop stopF = new BusStop("F", "Mount Lavinia", 6.8389, 79.8630);

        allStops.add(stopA);
        allStops.add(stopB);
        allStops.add(stopC);
        allStops.add(stopD);
        allStops.add(stopE);
        allStops.add(stopF);

        allStops.forEach(graph::addStop);

        graph.addBidirectionalRoute(stopA, stopB, 3, 15, 50, true);
        graph.addBidirectionalRoute(stopA, stopC, 2, 10, 40, true);
        graph.addBidirectionalRoute(stopB, stopD, 4, 20, 60, false);
        graph.addBidirectionalRoute(stopC, stopD, 1, 8, 30, true);
        graph.addBidirectionalRoute(stopD, stopE, 3, 18, 55, true);
        graph.addBidirectionalRoute(stopD, stopF, 5, 25, 70, true);

        System.out.println("✅ Graph initialized with " + allStops.size() + " bus stops");
    }

    public List<BusStop> getAllBusStops() {
        return allStops;
    }

    public RouteResponse findRoute(RouteRequest request) {

        if (request.getSource() == null || request.getDestination() == null) {
            RouteResponse response = new RouteResponse();
            response.setFound(false);
            response.setMessage("Source and destination are required");
            return response;
        }

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

        String algorithm = request.getAlgorithm() != null
                ? request.getAlgorithm().toLowerCase()
                : "dijkstra";

        RouteResponse response;

        switch (algorithm) {

            case "bfs":
                response = BFS.getRoute(graph, source, destination);
                break;

            case "astar":
                String mode = request.getMode() != null ? request.getMode() : "distance";
                response = AStar.getRoute(graph, source, destination, mode);
                break;

            case "balanced":
                response = AlternativeRoutes.getBalancedRoute(
                        graph, source, destination, 0.33, 0.33, 0.34
                );
                break;

            case "dijkstra":
            default:
                String dijkstraMode = request.getMode() != null
                        ? request.getMode()
                        : "distance";
                boolean emergency = request.isEmergencyOnly();
                response = Dijkstra.getRoute(
                        graph, source, destination, dijkstraMode, emergency
                );
        }

        // ✅ Attach Google-Maps-style road polylines
        attachPolylines(response);

        return response;
    }

    public List<RouteResponse> getAlternativeRoutes(String sourceId, String destinationId) {

        BusStop source = graph.findStopById(sourceId);
        BusStop destination = graph.findStopById(destinationId);

        if (source == null || destination == null) {
            return new ArrayList<>();
        }

        List<RouteResponse> routes =
                AlternativeRoutes.findAlternatives(graph, source, destination);

        for (RouteResponse route : routes) {
            attachPolylines(route);
        }

        return routes;
    }

    // ===================== HELPER METHOD =====================

    private void attachPolylines(RouteResponse response) {

        if (!response.isFound() || response.getPath() == null) return;

        List<List<double[]>> polylines = new ArrayList<>();
        List<BusStop> path = response.getPath();

        for (int i = 0; i < path.size() - 1; i++) {

            List<double[]> segment =
                    roadSegmentService.getSegment(
                            path.get(i),
                            path.get(i + 1)
                    );

            // never null, safe to add
            if (!segment.isEmpty()) {
                polylines.add(segment);
            }
        }

        response.setPolylines(polylines);
    }
}
