package algorithms;

import graph.Graph;
import model.*;
import java.util.*;

public class BFS {

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
}
