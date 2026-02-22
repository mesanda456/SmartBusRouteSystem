package com.example.bus_router_planner.model;

import java.util.List;

/**
 * Represents one complete transit route option.
 */
public class TransitOption {

    private String departureTime;
    private String arrivalTime;
    private int totalDurationMinutes;
    private int totalDistanceKm;
    private int totalCostRs;
    private int totalStops;
    private int transfers;
    private List<String> busNumbers;
    private List<TransitSegment> segments;
    private String label;
    private String algorithm;
    private String crowdLevel;
    private int availableSeats;

    // ✅ NEW FIELDS (FOR PDSA PERFORMANCE)
    private long executionTime;
    private int nodesVisited;

    public TransitOption() {}

    // ---- Getters & Setters ----

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public int getTotalDurationMinutes() { return totalDurationMinutes; }
    public void setTotalDurationMinutes(int totalDurationMinutes) { this.totalDurationMinutes = totalDurationMinutes; }

    public int getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(int totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }

    public int getTotalCostRs() { return totalCostRs; }
    public void setTotalCostRs(int totalCostRs) { this.totalCostRs = totalCostRs; }

    public int getTotalStops() { return totalStops; }
    public void setTotalStops(int totalStops) { this.totalStops = totalStops; }

    public int getTransfers() { return transfers; }
    public void setTransfers(int transfers) { this.transfers = transfers; }

    public List<String> getBusNumbers() { return busNumbers; }
    public void setBusNumbers(List<String> busNumbers) { this.busNumbers = busNumbers; }

    public List<TransitSegment> getSegments() { return segments; }
    public void setSegments(List<TransitSegment> segments) { this.segments = segments; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getCrowdLevel() { return crowdLevel; }
    public void setCrowdLevel(String crowdLevel) { this.crowdLevel = crowdLevel; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    // ✅ NEW GETTERS & SETTERS

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public int getNodesVisited() {
        return nodesVisited;
    }

    public void setNodesVisited(int nodesVisited) {
        this.nodesVisited = nodesVisited;
    }
}