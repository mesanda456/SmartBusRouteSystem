package com.example.bus_router_planner.algorithms;

import com.example.bus_router_planner.graph.Graph;
import com.example.bus_router_planner.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class AlternativeRoutes {

    public static List<RouteResponse> findAlternatives(
            Graph graph, BusStop source, BusStop destination) {

        List<RouteResponse> alternatives = new ArrayList<>();

        RouteResponse fastest = Dijkstra.getRoute(graph, source, destination,
                "time", false);
        if (fastest.isFound()) {
            fastest.setMessage("Fastest route (time-optimized)");
            alternatives.add(fastest);
        }

        RouteResponse cheapest = Dijkstra.getRoute(graph, source, destination,
                "cost", false);
        if (cheapest.isFound()) {
            cheapest.setMessage("Cheapest route (cost-optimized)");
            alternatives.add(cheapest);
        }

        RouteResponse fewestTransfers = BFS.getRoute(graph, source, destination);
        if (fewestTransfers.isFound()) {
            fewestTransfers.setMessage("Minimum transfers route");
            alternatives.add(fewestTransfers);
        }

        List<RouteResponse> unique = removeDuplicates(alternatives);

        if (unique.size() < 3) {
            RouteResponse shortest = Dijkstra.getRoute(graph, source, destination,
                    "distance", false);
            if (shortest.isFound() && !isDuplicate(shortest, unique)) {
                shortest.setMessage("Shortest distance route");
                unique.add(shortest);
            }
        }

        return unique;
    }

    public static RouteResponse getBalancedRoute(
            Graph graph, BusStop source, BusStop destination,
            double distanceWeight, double timeWeight, double costWeight) {

        double totalWeight = distanceWeight + timeWeight + costWeight;
        distanceWeight /= totalWeight;
        timeWeight /= totalWeight;
        costWeight /= totalWeight;

        Map<BusStop, Double> dist = new HashMap<>();
        Map<BusStop, BusStop> prev = new HashMap<>();
        PriorityQueue<BusStop> pq = new PriorityQueue<>(
                Comparator.comparingDouble(stop -> dist.getOrDefault(stop, Double.POSITIVE_INFINITY))
        );

        for (BusStop stop : graph.getAllStops()) {
            dist.put(stop, Double.POSITIVE_INFINITY);
        }

        dist.put(source, 0.0);
        pq.add(source);
        prev.put(source, null);

        while (!pq.isEmpty()) {
            BusStop current = pq.poll();

            if (current.equals(destination)) break;

            for (Edge edge : graph.getEdges(current)) {
                double weight = (edge.getDistance() * distanceWeight) +
                        (edge.getTime() * timeWeight) +
                        (edge.getCost() * costWeight);

                double newDist = dist.get(current) + weight;

                if (newDist < dist.get(edge.getDestination())) {
                    dist.put(edge.getDestination(), newDist);
                    prev.put(edge.getDestination(), current);
                    pq.remove(edge.getDestination());
                    pq.add(edge.getDestination());
                }
            }
        }

        List<BusStop> path = new ArrayList<>();
        BusStop current = destination;
        while (current != null) {
            path.add(0, current);
            current = prev.get(current);
        }

        if (path.isEmpty() || !path.get(0).equals(source)) {
            return new RouteResponse(
                    false,
                    new ArrayList<>(),
                    null,
                    0,
                    "Weighted",
                    "balanced"
            );

        }

        int totalCost = dist.get(destination).intValue();
        RouteResponse result = new RouteResponse(
                true,
                path,
                null, // polylines added later
                totalCost,
                "Weighted Multi-Objective",
                "balanced"
        );

        result.setMessage(String.format(
                "Balanced route (%.0f%% distance, %.0f%% time, %.0f%% cost)",
                distanceWeight * 100, timeWeight * 100, costWeight * 100
        ));
        return result;
    }

    private static List<RouteResponse> removeDuplicates(List<RouteResponse> routes) {
        List<RouteResponse> unique = new ArrayList<>();
        Set<String> seenPaths = new HashSet<>();

        for (RouteResponse route : routes) {
            String pathKey = route.getPath().stream()
                    .map(BusStop::getId)
                    .collect(Collectors.joining("->"));

            if (!seenPaths.contains(pathKey)) {
                seenPaths.add(pathKey);
                unique.add(route);
            }
        }

        return unique;
    }

    private static boolean isDuplicate(RouteResponse route, List<RouteResponse> existing) {
        String pathKey = route.getPath().stream()
                .map(BusStop::getId)
                .collect(Collectors.joining("->"));

        for (RouteResponse exist : existing) {
            String existKey = exist.getPath().stream()
                    .map(BusStop::getId)
                    .collect(Collectors.joining("->"));

            if (pathKey.equals(existKey)) {
                return true;
            }
        }

        return false;
    }
}