package com.example.bus_router_planner.algorithms;

import com.example.bus_router_planner.graph.Graph;
import com.example.bus_router_planner.model.*;
import java.util.*;

public class LandmarkRouting {

    public static RouteResponse getRouteViaLandmarks(
            Graph graph, BusStop source, BusStop destination,
            List<BusStop> landmarks, String mode) {

        List<BusStop> fullPath = new ArrayList<>();
        List<List<double[]>> fullRoutes = new ArrayList<>();
        int totalCost = 0;

        List<BusStop> waypoints = new ArrayList<>();
        waypoints.add(source);
        waypoints.addAll(landmarks);
        waypoints.add(destination);

        for (int i = 0; i < waypoints.size() - 1; i++) {
            BusStop from = waypoints.get(i);
            BusStop to = waypoints.get(i + 1);

            RouteResponse segment = Dijkstra.getRoute(graph, from, to, mode, false);

            if (!segment.isFound()) {
                String message = String.format(
                        "Cannot reach %s from %s via specified landmarks",
                        destination.getName(), source.getName()
                );
                // ❌ FIXED: Added missing routes parameter
                RouteResponse failed = new RouteResponse(false, new ArrayList<>(),
                        new ArrayList<>(), 0, "Landmark", mode);
                failed.setMessage(message);
                return failed;
            }

            // Combine paths
            if (fullPath.isEmpty()) {
                fullPath.addAll(segment.getPath());
            } else {
                List<BusStop> segmentPath = segment.getPath();
                fullPath.addAll(segmentPath.subList(1, segmentPath.size()));
            }

            // ✅ NEW: Combine route coordinates
            if (segment.getRoutes() != null && !segment.getRoutes().isEmpty()) {
                fullRoutes.addAll(segment.getRoutes());
            }

            totalCost += segment.getTotalValue();
        }

        // ❌ FIXED: Added missing routes parameter
        RouteResponse result = new RouteResponse(true, fullPath, fullRoutes,
                totalCost, "Landmark", mode);
        result.setMessage(String.format("Route via %d landmarks", landmarks.size()));
        return result;
    }

    // ✅ NEW: Alternative method if you want to generate routes from the full path
    public static RouteResponse getRouteViaLandmarksWithGeneratedRoutes(
            Graph graph, BusStop source, BusStop destination,
            List<BusStop> landmarks, String mode) {

        List<BusStop> fullPath = new ArrayList<>();
        int totalCost = 0;

        List<BusStop> waypoints = new ArrayList<>();
        waypoints.add(source);
        waypoints.addAll(landmarks);
        waypoints.add(destination);

        for (int i = 0; i < waypoints.size() - 1; i++) {
            BusStop from = waypoints.get(i);
            BusStop to = waypoints.get(i + 1);

            RouteResponse segment = Dijkstra.getRoute(graph, from, to, mode, false);

            if (!segment.isFound()) {
                String message = String.format(
                        "Cannot reach %s from %s via specified landmarks",
                        destination.getName(), source.getName()
                );
                RouteResponse failed = new RouteResponse(false, new ArrayList<>(),
                        new ArrayList<>(), 0, "Landmark", mode);
                failed.setMessage(message);
                return failed;
            }

            if (fullPath.isEmpty()) {
                fullPath.addAll(segment.getPath());
            } else {
                List<BusStop> segmentPath = segment.getPath();
                fullPath.addAll(segmentPath.subList(1, segmentPath.size()));
            }

            totalCost += segment.getTotalValue();
        }

        // Generate route coordinates from the complete path
        List<List<double[]>> routes = generateRouteCoordinates(fullPath);

        RouteResponse result = new RouteResponse(true, fullPath, routes,
                totalCost, "Landmark", mode);
        result.setMessage(String.format("Route via %d landmarks", landmarks.size()));
        return result;
    }

    // ✅ NEW: Helper method to generate route coordinates
    private static List<List<double[]>> generateRouteCoordinates(List<BusStop> path) {
        List<List<double[]>> routes = new ArrayList<>();

        if (path.size() < 2) {
            return routes;
        }

        List<double[]> routeCoordinates = new ArrayList<>();
        for (BusStop stop : path) {
            double[] coords = {stop.getLatitude(), stop.getLongitude()};
            routeCoordinates.add(coords);
        }

        routes.add(routeCoordinates);
        return routes;
    }
}
