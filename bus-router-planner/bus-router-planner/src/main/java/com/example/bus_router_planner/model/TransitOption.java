package com.example.bus_router_planner.model;

import java.util.List;

/**
 * Represents one complete transit route option (like one card in Google Maps).
 * Contains: departure time, arrival time, total duration, segments (walk+bus+walk),
 * bus numbers used, total cost, etc.
 */
public class TransitOption {

    private String departureTime;        // e.g. "11:04 PM"
    private String arrivalTime;          // e.g. "2:10 AM"
    private int totalDurationMinutes;    // total trip time
    private int totalDistanceKm;
    private int totalCostRs;
    private int totalStops;
    private int transfers;               // number of bus changes
    private List<String> busNumbers;     // all bus numbers used ["138","224"]
    private List<TransitSegment> segments; // ordered segments: walk→bus→walk→bus→walk
    private String label;                // "Fastest", "Cheapest", "Fewest Transfers"
    private String algorithm;
    private String crowdLevel;
    private int availableSeats;

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
}