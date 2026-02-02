package controller;



import model.BusStop;
import model.RouteRequest;
import model.RouteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.RouteService;

import java.util.List;

/**
 * REST API Controller for Route Planning
 * Base URL: http://localhost:8080/api
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Allow requests from any origin (frontend)
public class RouteController {

    @Autowired
    private RouteService routeService;

    /**
     * Health check endpoint
     * GET http://localhost:8080/api/health
     */
    @GetMapping("/health")
    public String health() {
        return "Bus Route Planner API is running! 🚌";
    }

    /**
     * Get all bus stops
     * GET http://localhost:8080/api/stops
     *
     * Response: [
     *   { "id": "A", "name": "Fort Station", "latitude": 6.9344, "longitude": 79.8428 },
     *   ...
     * ]
     */
    @GetMapping("/stops")
    public List<BusStop> getAllStops() {
        return routeService.getAllBusStops();
    }

    /**
     * Find optimal route
     * POST http://localhost:8080/api/route
     *
     * Request Body:
     * {
     *   "source": "A",
     *   "destination": "E",
     *   "algorithm": "dijkstra",
     *   "mode": "distance",
     *   "emergencyOnly": false
     * }
     *
     * Response:
     * {
     *   "found": true,
     *   "path": [...],
     *   "totalValue": 5,
     *   "totalStops": 4,
     *   "algorithm": "Dijkstra",
     *   "mode": "distance",
     *   "message": "Route found successfully"
     * }
     */
    @PostMapping("/route")
    public RouteResponse findRoute(@RequestBody RouteRequest request) {
        return routeService.findRoute(request);
    }

    /**
     * Simple GET endpoint for testing
     * GET http://localhost:8080/api/route?source=A&destination=E&algorithm=dijkstra
     */
    @GetMapping("/route")
    public RouteResponse findRouteSimple(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(defaultValue = "dijkstra") String algorithm,
            @RequestParam(defaultValue = "distance") String mode,
            @RequestParam(defaultValue = "false") boolean emergencyOnly) {

        RouteRequest request = new RouteRequest(source, destination,
                algorithm, mode, emergencyOnly);
        return routeService.findRoute(request);
    }
}