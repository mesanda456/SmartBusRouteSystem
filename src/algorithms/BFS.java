package algorithms;

import graph.Graph;
import model.*;
import java.util.*;

public class BFS {

    /**
     * Find minimum number of stops between two bus stops
     * Returns -1 if no path exists
     */
    public static int minStops(Graph graph, BusStop start, BusStop end) {

        Queue<BusStop> q = new LinkedList<>();
        Map<BusStop, Integer> stops = new HashMap<>();

        q.add(start);
        stops.put(start, 0);

        while (!q.isEmpty()) {
            BusStop current = q.poll();

            if (current.equals(end))
                return stops.get(current);

            for (Edge e : graph.adjList.get(current)) {
                if (!stops.containsKey(e.destination)) {
                    stops.put(e.destination, stops.get(current) + 1);
                    q.add(e.destination);
                }
            }
        }
        return -1;
    }

    /**
     * Get full route with minimum transfers
     * Returns RouteResult object for API
     */
    public static RouteResult getRoute(Graph graph, BusStop start, BusStop end) {

        Queue<BusStop> q = new LinkedList<>();
        Map<BusStop, Integer> stops = new HashMap<>();
        Map<BusStop, BusStop> previous = new HashMap<>();

        q.add(start);
        stops.put(start, 0);
        previous.put(start, null);

        while (!q.isEmpty()) {
            BusStop current = q.poll();

            if (current.equals(end)) {
                // Reconstruct path
                List<BusStop> path = new ArrayList<>();
                BusStop node = end;

                while (node != null) {
                    path.add(0, node);
                    node = previous.get(node);
                }

                int totalStops = stops.get(end);
                return new RouteResult(true, path, totalStops, "bfs", "stops");
            }

            for (Edge e : graph.adjList.get(current)) {
                if (!stops.containsKey(e.destination)) {
                    stops.put(e.destination, stops.get(current) + 1);
                    previous.put(e.destination, current);
                    q.add(e.destination);
                }
            }
        }

        // No path found
        return new RouteResult(false, new ArrayList<>(), -1, "bfs", "stops");
    }
}