package model;

public class Edge {
    public BusStop destination;
    public int distance;
    public int time;
    public int cost;
    public boolean safe;

    public Edge(BusStop destination, int distance, int time, int cost, boolean safe) {
        this.destination = destination;
        this.distance = distance;
        this.time = time;
        this.cost = cost;
        this.safe = safe;
    }
}
