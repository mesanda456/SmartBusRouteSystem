package com.example.bus_router_planner.model;

import java.util.List;

public class RouteResponse {

    private boolean found;
    private List<BusStop> path;                 // logical bus stop path
    private List<List<double[]>> polylines;     // real road geometry

    private int totalValue;
    private int totalStops;
    private String algorithm;
    private String mode;
    private String message;


    // Add these fields to RouteResponse.java
    private String crowdLevel;
    private int seatsAvailable;
    private String crowdMessage;


    private long executionTime;
    private int nodesVisited;

    // Add getters/setters for all three
    public RouteResponse() {}

    public RouteResponse(
            boolean found,
            List<BusStop> path,
            List<List<double[]>> polylines,
            int totalValue,
            String algorithm,
            String mode
    ) {
        this.found = found;
        this.path = path;
        this.polylines = polylines;
        this.totalValue = totalValue;
        this.totalStops = path != null ? path.size() : 0;
        this.algorithm = algorithm;
        this.mode = mode;
        this.message = found ? "Route found successfully" : "No route available";
    }

    public static RouteResponse error(String message) {
        RouteResponse r = new RouteResponse();
        r.setFound(false);
        r.setMessage(message);
        return r;
    }

    // ---------- getters & setters ----------

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public List<BusStop> getPath() {
        return path;
    }

    public void setPath(List<BusStop> path) {
        this.path = path;
        this.totalStops = path != null ? path.size() : 0;
    }

    // 🔹 Original name (map rendering)
    public List<List<double[]>> getPolylines() {
        return polylines;
    }

    public void setPolylines(List<List<double[]>> polylines) {
        this.polylines = polylines;
    }

    // 🔹 ALIAS for algorithms (fixes your errors)
    public List<List<double[]>> getRoutes() {
        return polylines;
    }

    public void setRoutes(List<List<double[]>> routes) {
        this.polylines = routes;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getTotalStops() {
        return totalStops;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(String crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getCrowdMessage() {
        return crowdMessage;
    }

    public void setCrowdMessage(String crowdMessage) {
        this.crowdMessage = crowdMessage;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public void setNodesVisited(int nodesVisited) {
        this.nodesVisited = nodesVisited;
    }


    public long getExecutionTime() {
        return executionTime;
    }

    public int getNodesVisited() {
        return nodesVisited;
    }
}
