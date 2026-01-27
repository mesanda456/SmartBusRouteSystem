package model;

import java.util.List;

/**
 * Result object returned by routing algorithms
 * Will be converted to JSON for frontend
 */
public class RouteResult {
    public boolean found;
    public List<BusStop> path;
    public int totalDistance;
    public int totalTime;
    public int totalCost;
    public int totalStops;
    public String algorithm;  // "dijkstra" or "bfs"
    public String mode;       // "distance", "time", or "cost"

    public RouteResult(boolean found, List<BusStop> path,
                       int total, String algorithm, String mode) {
        this.found = found;
        this.path = path;
        this.algorithm = algorithm;
        this.mode = mode;
        this.totalStops = path != null ? path.size() : 0;

        // Set the appropriate total based on mode
        if ("time".equals(mode)) {
            this.totalTime = total;
        } else if ("cost".equals(mode)) {
            this.totalCost = total;
        } else {
            this.totalDistance = total;
        }
    }

    public String getFormattedPath() {
        if (!found || path == null || path.isEmpty()) {
            return "No route found";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i).name);
            if (i < path.size() - 1) {
                sb.append(" → ");
            }
        }
        return sb.toString();
    }
}