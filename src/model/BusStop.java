package model;

public class BusStop {
    public String name;

    public BusStop(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
