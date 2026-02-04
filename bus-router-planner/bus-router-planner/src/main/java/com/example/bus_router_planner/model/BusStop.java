package com.example.bus_router_planner.model;

import java.util.Objects;

public class BusStop {

    private String id;
    private String name;
    private double latitude;
    private double longitude;

    // Required for JSON / Spring
    public BusStop() {}

    // MAIN constructor (use this everywhere)
    public BusStop(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters & setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return name;
    }

    // IMPORTANT: equality based on ID only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusStop)) return false;
        BusStop busStop = (BusStop) o;
        return Objects.equals(id, busStop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
