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
}
