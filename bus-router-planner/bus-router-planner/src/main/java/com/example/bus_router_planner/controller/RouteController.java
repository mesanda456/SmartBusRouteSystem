package com.example.bus_router_planner.controller;

import com.example.bus_router_planner.model.BusStop;
import com.example.bus_router_planner.model.RouteRequest;
import com.example.bus_router_planner.model.RouteResponse;
import com.example.bus_router_planner.model.TransitOption;
import com.example.bus_router_planner.service.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RouteController {

    private final RouteService routeService;
    private final TransitService transitService;
    private final ScheduleService scheduleService;
    private final FareService fareService;
    private final TrafficService trafficService;

    public RouteController(RouteService routeService, TransitService transitService,
                           ScheduleService scheduleService, FareService fareService,
                           TrafficService trafficService) {
        this.routeService = routeService;
        this.transitService = transitService;
        this.scheduleService = scheduleService;
        this.fareService = fareService;
        this.trafficService = trafficService;
    }

    @GetMapping("/health")
    public String health() { return "Bus Route Planner API is running!"; }

    @GetMapping("/stops")
    public List<BusStop> getAllStops() { return routeService.getAllBusStops(); }

    @GetMapping("/stops/search")
    public List<BusStop> searchStops(@RequestParam(defaultValue = "") String q) { return routeService.searchStops(q); }

    @GetMapping("/stops/nearby")
    public List<Map<String, Object>> nearbyStops(@RequestParam double lat, @RequestParam double lng,
                                                 @RequestParam(defaultValue = "2.0") double radiusKm) {
        return routeService.getAllBusStops().stream().map(s -> {
                    double dist = haversine(lat, lng, s.getLatitude(), s.getLongitude());
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("stop", s); m.put("distanceKm", Math.round(dist * 100) / 100.0);
                    m.put("walkMinutes", (int) Math.ceil(dist / 0.08)); return m;
                }).filter(m -> (double) m.get("distanceKm") <= radiusKm)
                .sorted(Comparator.comparingDouble(m -> (double) m.get("distanceKm"))).collect(Collectors.toList());
    }

    @GetMapping("/transit")
    public List<TransitOption> getTransitOptions(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(defaultValue = "distance") String mode) {

        return transitService.findTransitOptions(source, destination, mode);
    }

    @GetMapping("/schedule")
    public Map<String, Object> getSchedule(@RequestParam String stopId) { return scheduleService.getSchedule(stopId); }

    @GetMapping("/timetable")
    public Map<String, Object> getTimetable(@RequestParam String busNumber) { return scheduleService.getTimetable(busNumber); }

    @GetMapping("/fare")
    public Map<String, Object> calculateFare(@RequestParam double distanceKm) { return fareService.calculateFare(distanceKm); }

    @GetMapping("/fare/bus")
    public Map<String, Object> busFare(@RequestParam String busNumber, @RequestParam double distanceKm) {
        return fareService.getFareByBus(busNumber, distanceKm);
    }

    @GetMapping("/traffic/alerts")
    public List<Map<String, Object>> trafficAlerts() { return trafficService.getAlerts(); }

    @GetMapping("/traffic/corridor")
    public Map<String, Object> corridorTraffic(@RequestParam String corridor) { return trafficService.getCorridorTraffic(corridor); }

    @GetMapping("/graph")
    public Map<String, Object> getGraphData() {
        Map<String, Object> graph = new LinkedHashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        routeService.getAllBusStops().forEach(s -> {
            Map<String, Object> n = new LinkedHashMap<>();
            n.put("id", s.getId()); n.put("name", s.getName());
            n.put("lat", s.getLatitude()); n.put("lng", s.getLongitude()); nodes.add(n);
        });
        graph.put("nodes", nodes); graph.put("edges", routeService.getGraphEdges());
        graph.put("totalNodes", nodes.size()); graph.put("totalEdges", routeService.getGraphEdges().size());
        return graph;
    }

    @PostMapping("/route")
    public RouteResponse findRoute(@RequestBody RouteRequest request) { return routeService.findRoute(request); }

    @GetMapping("/route")
    public RouteResponse findRouteSimple(@RequestParam String source, @RequestParam String destination,
                                         @RequestParam(defaultValue = "dijkstra") String algorithm, @RequestParam(defaultValue = "distance") String mode,
                                         @RequestParam(defaultValue = "false") boolean emergencyOnly) {
        return routeService.findRoute(new RouteRequest(source, destination, algorithm, mode, emergencyOnly));
    }

    @GetMapping("/routes/alternatives")
    public List<RouteResponse> getAlternativeRoutes(@RequestParam String source, @RequestParam String destination) {
        return routeService.getAlternativeRoutes(source, destination);
    }

    @GetMapping("/routes/astar")
    public RouteResponse getAStarRoute(@RequestParam String source, @RequestParam String destination,
                                       @RequestParam(defaultValue = "distance") String mode) {
        RouteRequest r = new RouteRequest(); r.setSource(source); r.setDestination(destination);
        r.setAlgorithm("astar"); r.setMode(mode); return routeService.findRoute(r);
    }

    @GetMapping("/routes/balanced")
    public RouteResponse getBalancedRoute(@RequestParam String source, @RequestParam String destination) {
        RouteRequest r = new RouteRequest(); r.setSource(source); r.setDestination(destination);
        r.setAlgorithm("balanced"); return routeService.findRoute(r);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; double dLat = Math.toRadians(lat2 - lat1); double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.sin(dLon/2)*Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}