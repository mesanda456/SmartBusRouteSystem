package algorithms;

import graph.Graph;
import model.*;
import java.util.*;

public class Dijkstra {

    // ================= RESULT CLASS =================
    public static class Result {
        public Map<BusStop, Integer> distance;
        public Map<BusStop, BusStop> previous;

        public Result(Map<BusStop, Integer> d, Map<BusStop, BusStop> p) {
            this.distance = d;
            this.previous = p;
        }
    }

    // ================= DIJKSTRA ALGORITHM =================
    public static Result findPath(Graph graph, BusStop source,
                                  String mode, boolean emergencyOnly) {

        Map<BusStop, Integer> dist = new HashMap<>();
        Map<BusStop, BusStop> prev = new HashMap<>();

        PriorityQueue<BusStop> pq =
                new PriorityQueue<>(Comparator.comparingInt(dist::get));

        // Initialize
        for (BusStop s : graph.adjList.keySet()) {
            dist.put(s, Integer.MAX_VALUE);
            prev.put(s, null);
        }

        dist.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            BusStop current = pq.poll();

            for (Edge e : graph.adjList.get(current)) {

                // Skip unsafe routes in emergency mode
                if (emergencyOnly && !e.safe)
                    continue;

                // Select weight based on mode
                int weight = switch (mode) {
                    case "time" -> e.time;
                    case "cost" -> e.cost;
                    default -> e.distance;
                };

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(e.destination)) {
                    dist.put(e.destination, newDist);
                    prev.put(e.destination, current);
                    pq.remove(e.destination);
                    pq.add(e.destination);
                }
            }
        }

        return new Result(dist, prev);
    }

    // ================= PATH RECONSTRUCTION =================
    public static RouteResult getRoute(Graph graph, BusStop source,
                                       BusStop destination, String mode,
                                       boolean emergencyOnly) {

        Result result = findPath(graph, source, mode, emergencyOnly);

        // Check if destination is reachable
        if (result.distance.get(destination) == Integer.MAX_VALUE) {
            return new RouteResult(false, new ArrayList<>(), 0, "dijkstra", mode);
        }

        // Reconstruct path
        List<BusStop> path = new ArrayList<>();
        BusStop current = destination;

        while (current != null) {
            path.add(0, current);  // Add to front
            current = result.previous.get(current);
        }

        int totalCost = result.distance.get(destination);

        return new RouteResult(true, path, totalCost, "dijkstra", mode);
    }
}