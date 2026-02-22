package com.example.bus_router_planner.algorithms;

import com.example.bus_router_planner.graph.Graph;
import com.example.bus_router_planner.model.*;
import java.util.*;

public class BFS {

    public static int minStops(Graph graph, BusStop start, BusStop end) {

        Queue<BusStop> queue = new LinkedList<>();
        Map<BusStop, Integer> stops = new HashMap<>();

        queue.add(start);
        stops.put(start, 0);

        while (!queue.isEmpty()) {
            BusStop current = queue.poll();

            if (current.equals(end))
                return stops.get(current);

            for (Edge edge : graph.adjList.get(current)) {
                if (!stops.containsKey(edge.getDestination())) {
                    stops.put(edge.getDestination(), stops.get(current) + 1);
                    queue.add(edge.getDestination());
                }
            }
        }

        return -1;
    }

    public static RouteResponse getRoute(Graph graph, BusStop start, BusStop end) {

        Queue<BusStop> queue = new LinkedList<>();
        Map<BusStop, Integer> stops = new HashMap<>();
        Map<BusStop, BusStop> previous = new HashMap<>();

        queue.add(start);
        stops.put(start, 0);
        previous.put(start, null);

        while (!queue.isEmpty()) {
            BusStop current = queue.poll();

            if (current.equals(end)) {
                List<BusStop> path = new ArrayList<>();
                BusStop node = end;

                while (node != null) {
                    path.add(0, node);
                    node = previous.get(node);
                }

                int totalStops = stops.get(end);

                // 🔹 BFS has no real road geometry
                List<List<double[]>> polylines = new ArrayList<>();

                RouteResponse result = new RouteResponse(
                        true,
                        path,
                        polylines,
                        totalStops,
                        "BFS",
                        "stops"
                );

                result.setMessage("Minimum stops route found");
                return result;
            }

            for (Edge edge : graph.adjList.getOrDefault(current, Collections.emptyList())) {
                BusStop neighbor = edge.getDestination();

                if (!stops.containsKey(neighbor)) {
                    stops.put(neighbor, stops.get(current) + 1);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // ❌ No route found
        RouteResponse failed = new RouteResponse(
                false,
                new ArrayList<>(),
                new ArrayList<>(),
                -1,
                "BFS",
                "stops"
        );

        failed.setMessage("No route available");
        return failed;
    }
}
