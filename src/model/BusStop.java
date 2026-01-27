package model;

import java.util.Objects;

public class BusStop {
    public String id;           // Unique identifier
    public String name;         // Display name
    public double latitude;     // GPS coordinates
    public double longitude;

    public BusStop(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Constructor for backward compatibility
    public BusStop(String name) {
        this.id = name;
        this.name = name;
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStop busStop = (BusStop) o;
        return Objects.equals(id, busStop.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}