package graph;




import model.BusStop;
import model.Edge;

import java.util.*;

/**
 * Graph data structure using Adjacency List
 */
public class Graph {
    public Map<BusStop, List<Edge>> adjList = new HashMap<>();

    /**
     * Add a bus stop to the graph
     */
    public void addStop(BusStop stop) {
        adjList.putIfAbsent(stop, new ArrayList<>());
    }

    /**
     * Add a directed route between two stops
     */
    public void addRoute(BusStop from, BusStop to, int distance,
                         int time, int cost, boolean safe) {
        adjList.get(from).add(new Edge(to, distance, time, cost, safe));
    }

    /**
     * Add bidirectional route
     */
    public void addBidirectionalRoute(BusStop stop1, BusStop stop2,
                                      int distance, int time, int cost, boolean safe) {
        addRoute(stop1, stop2, distance, time, cost, safe);
        addRoute(stop2, stop1, distance, time, cost, safe);
    }

    /**
     * Get all bus stops
     */
    public Set<BusStop> getAllStops() {
        return adjList.keySet();
    }

    /**
     * Find bus stop by ID
     */
    public BusStop findStopById(String id) {
        return adjList.keySet().stream()
                .filter(stop -> stop.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get edges for a specific stop
     */
    public List<Edge> getEdges(BusStop stop) {
        return adjList.getOrDefault(stop, new ArrayList<>());
    }
}