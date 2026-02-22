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
            boolean emergencyOnly,
            int[] nodesVisited
    ) {

        Map<BusStop, Integer> dist = new HashMap<>();
        Map<BusStop, BusStop> prev = new HashMap<>();

        class Node {
            BusStop stop;
            int distance;

            Node(BusStop s, int d) {
                stop = s;
                distance = d;
            }
        }

        PriorityQueue<Node> pq =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));

        for (BusStop stop : graph.adjList.keySet()) {
            dist.put(stop, Integer.MAX_VALUE);
            prev.put(stop, null);
        }

        dist.put(source, 0);
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {

            Node node = pq.poll();
            nodesVisited[0]++;  // count visited nodes

            BusStop current = node.stop;

            if (node.distance > dist.get(current)) continue;

            for (Edge edge : graph.adjList.get(current)) {

                if (emergencyOnly && !edge.isSafe()) continue;

                int weight = switch (mode != null ? mode : "distance") {
                    case "time" -> edge.getTime();
                    case "cost" -> edge.getCost();
                    default -> edge.getDistance();
                };

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(edge.getDestination())) {

                    dist.put(edge.getDestination(), newDist);
                    prev.put(edge.getDestination(), current);

                    pq.add(new Node(edge.getDestination(), newDist));
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

        long startTime = System.currentTimeMillis();
        int[] nodesVisited = new int[]{0};

        Result result = findPath(graph, source, mode, emergencyOnly, nodesVisited);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // If no path found
        if (result.distance.get(destination) == Integer.MAX_VALUE) {

            RouteResponse response = new RouteResponse(
                    false,
                    new ArrayList<>(),
                    null,
                    0,
                    "Dijkstra",
                    mode
            );

            response.setExecutionTime(executionTime);
            response.setNodesVisited(nodesVisited[0]);

            return response;
        }

        // Build path
        List<BusStop> path = new ArrayList<>();
        BusStop current = destination;

        while (current != null) {
            path.add(0, current);
            current = result.previous.get(current);
        }

        int totalCost = result.distance.get(destination);

        RouteResponse response = new RouteResponse(
                true,
                path,
                null,
                totalCost,
                "Dijkstra",
                mode
        );

        response.setExecutionTime(executionTime);
        response.setNodesVisited(nodesVisited[0]);

        return response;
    }
}