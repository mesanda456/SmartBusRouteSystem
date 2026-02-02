package algorithms;




import graph.Graph;
import model.BusStop;
import model.Edge;
import model.RouteResponse;

import java.util.*;

/**
 * Breadth-First Search (BFS) Implementation
 * Finds route with minimum number of stops
 * Time Complexity: O(V + E)
 */
public class BFS {

    /**
     * Find minimum number of stops between two locations
     */
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

        return -1;  // No path found
    }

    /**
     * Get full route with minimum transfers
     */
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
                // Reconstruct path
                List<BusStop> path = new ArrayList<>();
                BusStop node = end;

                while (node != null) {
                    path.add(0, node);
                    node = previous.get(node);
                }

                int totalStops = stops.get(end);
                return new RouteResponse(true, path, totalStops, "BFS", "stops");
            }

            for (Edge edge : graph.adjList.get(current)) {
                if (!stops.containsKey(edge.getDestination())) {
                    stops.put(edge.getDestination(), stops.get(current) + 1);
                    previous.put(edge.getDestination(), current);
                    queue.add(edge.getDestination());
                }
            }
        }

        // No path found
        return new RouteResponse(false, new ArrayList<>(), -1, "BFS", "stops");
    }
}
