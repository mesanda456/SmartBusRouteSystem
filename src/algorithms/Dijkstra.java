package algorithms;

import graph.Graph;
import model.*;
import java.util.*;

public class Dijkstra {

    // ================= RESULT CLASS =================
    public static class Result {
        public Map<BusStop, Integer> distance;
        public Map<BusStop, BusStop> previous;

        public Result(Map<BusStop, Integer> d,
                      Map<BusStop, BusStop> p) {
            this.distance = d;
            this.previous = p;
        }
    }

    // ================= DIJKSTRA ALGORITHM =================
    public static Result findPath(
            Graph graph, BusStop source,
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

                if (emergencyOnly && !e.safe)
                    continue;

                int weight = switch (mode) {
                    case "time" -> e.time;
                    case "cost" -> e.cost;
                    default -> e.distance;
                };

                int newDist = dist.get(current) + weight;

                if (newDist < dist.get(e.destination)) {
                    dist.put(e.destination, newDist);
                    prev.put(e.destination, current);
                    pq.add(e.destination);
                }
            }
        }

        return new Result(dist, prev);
    }
}
