package algorithms;




import graph.Graph;
import model.BusStop;
import model.Edge;
import model.RouteResponse;

import java.util.*;

/**
 * Dijkstra's Algorithm Implementation
 * Time Complexity: O((V + E) log V)
 */
public class Dijkstra {

    /**
     * Result class to store distances and previous nodes
     */
    public static class Result {
        public Map<BusStop, Integer> distance;
        public Map<BusStop, BusStop> previous;

        public Result(Map<BusStop, Integer> d, Map<BusStop, BusStop> p) {
            this.distance = d;
            this.previous = p;
        }
    }

    /**
     * Find shortest path using Dijkstra's algorithm
     */
    public static Result findPath(Graph graph, BusStop source,
                                  String mode, boolean emergencyOnly) {

        Map<BusStop, Integer> dist = new HashMap<>();
        Map<BusStop, BusStop> prev = new HashMap<>();

        // Priority Queue to get minimum distance node
        PriorityQueue<BusStop> pq =
                new PriorityQueue<>(Comparator.comparingInt(dist::get));

        // Initialize distances
        for (BusStop stop : graph.adjList.keySet()) {
            dist.put(stop, Integer.MAX_VALUE);
            prev.put(stop, null);
        }

        dist.put(source, 0);
        pq.add(source);

        // Main algorithm
        while (!pq.isEmpty()) {
            BusStop current = pq.poll();

            for (Edge edge : graph.adjList.get(current)) {

                // Skip unsafe routes in emergency mode
                if (emergencyOnly && !edge.isSafe())
                    continue;

                // Select weight based on optimization mode
                int weight = switch (mode) {
                    case "time" -> edge.getTime();
                    case "cost" -> edge.getCost();
                    default -> edge.getDistance();
                };

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(edge.getDestination())) {
                    dist.put(edge.getDestination(), newDist);
                    prev.put(edge.getDestination(), current);

                    // Re-add to priority queue with new distance
                    pq.remove(edge.getDestination());
                    pq.add(edge.getDestination());
                }
            }
        }

        return new Result(dist, prev);
    }

    /**
     * Reconstruct path from source to destination
     */
    public static RouteResponse getRoute(Graph graph, BusStop source,
                                         BusStop destination, String mode,
                                         boolean emergencyOnly) {

        Result result = findPath(graph, source, mode, emergencyOnly);

        // Check if destination is reachable
        if (result.distance.get(destination) == Integer.MAX_VALUE) {
            return new RouteResponse(false, new ArrayList<>(), 0,
                    "Dijkstra", mode);
        }

        // Reconstruct path
        List<BusStop> path = new ArrayList<>();
        BusStop current = destination;

        while (current != null) {
            path.add(0, current);  // Add to front
            current = result.previous.get(current);
        }

        int totalCost = result.distance.get(destination);

        return new RouteResponse(true, path, totalCost, "Dijkstra", mode);
    }
}