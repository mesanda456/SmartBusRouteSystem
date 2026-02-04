package com.example.bus_router_planner.algorithms;

import com.example.bus_router_planner.graph.Graph;
import com.example.bus_router_planner.model.*;
import java.util.*;

public class Dijkstra {

    public static class Result {
        public Map<BusStop, Integer> distance;
        public Map<BusStop, BusStop> previous;

        public Result(Map<BusStop, Integer> d, Map<BusStop, BusStop> p) {
            this.distance = d;
            this.previous = p;
        }
    }

    public static Result findPath(
            Graph graph,
            BusStop source,
            String mode,
            boolean emergencyOnly
    ) {

        Map<BusStop, Integer> dist = new HashMap<>();
        Map<BusStop, BusStop> prev = new HashMap<>();

        PriorityQueue<BusStop> pq =
                new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (BusStop stop : graph.adjList.keySet()) {
            dist.put(stop, Integer.MAX_VALUE);
            prev.put(stop, null);
        }

        dist.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            BusStop current = pq.poll();

            for (Edge edge : graph.adjList.get(current)) {

                if (emergencyOnly && !edge.isSafe()) continue;

                int weight = switch (mode) {
                    case "time" -> edge.getTime();
                    case "cost" -> edge.getCost();
                    default -> edge.getDistance();
                };

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(edge.getDestination())) {
                    dist.put(edge.getDestination(), newDist);
                    prev.put(edge.getDestination(), current);
                    pq.remove(edge.getDestination());
                    pq.add(edge.getDestination());
                }
            }
        }

        return new Result(dist, prev);
    }

    public static RouteResponse getRoute(
            Graph graph,
            BusStop source,
            BusStop destination,
            String mode,
            boolean emergencyOnly
    ) {

        Result result = findPath(graph, source, mode, emergencyOnly);

        if (result.distance.get(destination) == Integer.MAX_VALUE) {
            return new RouteResponse(
                    false,
                    new ArrayList<>(),
                    null,              // polylines handled later
                    0,
                    "Dijkstra",
                    mode
            );
        }

        List<BusStop> path = new ArrayList<>();
        BusStop current = destination;

        while (current != null) {
            path.add(0, current);
            current = result.previous.get(current);
        }

        int totalCost = result.distance.get(destination);

        return new RouteResponse(
                true,
                path,
                null,              // polylines handled in RouteService
                totalCost,
                "Dijkstra",
                mode
        );
    }
}
