package graph;

import model.*;
import java.util.*;

public class Graph {

    public Map<BusStop, List<Edge>> adjList = new HashMap<>();

    public void addStop(BusStop stop) {
        adjList.putIfAbsent(stop, new ArrayList<>());
    }

    public void addRoute(BusStop from, BusStop to,
                         int distance, int time, int cost, boolean safe) {
        adjList.get(from).add(new Edge(to, distance, time, cost, safe));
    }
}
