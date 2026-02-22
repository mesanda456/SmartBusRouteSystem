package com.example.bus_router_planner.model;

public class RouteRequest {
    private String source;
    private String destination;
    private String algorithm;
    private String mode;
    private boolean emergencyOnly;
    private String departureTime; // e.g. "08:30"

    public RouteRequest() {}

    public RouteRequest(String source, String destination, String algorithm,
                        String mode, boolean emergencyOnly) {
        this.source = source;
        this.destination = destination;
        this.algorithm = algorithm;
        this.mode = mode;
        this.emergencyOnly = emergencyOnly;

    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public boolean isEmergencyOnly() {
        return emergencyOnly;
    }

    public void setEmergencyOnly(boolean emergencyOnly) {
        this.emergencyOnly = emergencyOnly;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
}