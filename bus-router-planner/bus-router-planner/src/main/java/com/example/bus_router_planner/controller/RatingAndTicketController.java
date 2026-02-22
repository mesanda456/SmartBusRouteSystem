package com.example.bus_router_planner.controller;

import com.example.bus_router_planner.service.RatingService;
import com.example.bus_router_planner.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RatingAndTicketController {

    private final RatingService ratingService;
    private final TicketService ticketService;

    public RatingAndTicketController(RatingService ratingService, TicketService ticketService) {
        this.ratingService = ratingService;
        this.ticketService = ticketService;
    }

    // ─── RATING ENDPOINTS ───────────────────────────────────────────────────

    /**
     * GET /api/ratings/{busNumber}
     * Returns average ratings + recent reviews for a specific bus.
     * Example: GET /api/ratings/100
     */
    @GetMapping("/ratings/{busNumber}")
    public ResponseEntity<Map<String, Object>> getRatings(@PathVariable String busNumber) {
        return ResponseEntity.ok(ratingService.getRatings(busNumber));
    }

    /**
     * POST /api/ratings/{busNumber}
     * Submit a new rating for a bus.
     * Body (JSON):
     * {
     * "cleanliness": 4,
     * "driver": 5,
     * "punctuality": 3,
     * "comment": "Good ride!"
     * }
     */
    @PostMapping("/ratings/{busNumber}")
    public ResponseEntity<Map<String, Object>> addRating(
            @PathVariable String busNumber,
            @RequestBody Map<String, Object> body) {

        int cleanliness = (int) body.getOrDefault("cleanliness", 3);
        int driver = (int) body.getOrDefault("driver", 3);
        int punctuality = (int) body.getOrDefault("punctuality", 3);
        String comment = (String) body.getOrDefault("comment", "");

        Map<String, Object> rating = ratingService.addRating(busNumber, cleanliness, driver, punctuality, comment);
        return ResponseEntity.ok(rating);
    }

    /**
     * GET /api/ratings/top
     * Returns a list of all buses sorted by highest average rating.
     */
    @GetMapping("/ratings/top")
    public ResponseEntity<List<Map<String, Object>>> getTopBuses() {
        return ResponseEntity.ok(ratingService.getTopBuses());
    }


    // ─── TICKET ENDPOINTS ───────────────────────────────────────────────────

    /**
     * POST /api/ticket/generate
     * Generates a bus ticket with QR code data.
     * Body (JSON):
     * {
     *   "fromStop": "Colombo Fort",
     *   "toStop": "Kandy",
     *   "busNumber": "100",
     *   "fare": 150.0,
     *   "distanceKm": 115.0
     * }
     */
    @PostMapping("/ticket/generate")
    public ResponseEntity<Map<String, Object>> generateTicket(@RequestBody Map<String, Object> body) {
        String fromStop = (String) body.getOrDefault("fromStop", "");
        String toStop = (String) body.getOrDefault("toStop", "");
        String busNumber = (String) body.getOrDefault("busNumber", "");
        double fare = ((Number) body.getOrDefault("fare", 0)).doubleValue();
        double distanceKm = ((Number) body.getOrDefault("distanceKm", 0)).doubleValue();

        Map<String, Object> ticket = ticketService.generateTicket(fromStop, toStop, busNumber, fare, distanceKm);
        return ResponseEntity.ok(ticket);
    }
}
