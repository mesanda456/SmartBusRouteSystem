package com.example.bus_router_planner.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RatingService {

    // In-memory store: busNumber -> list of ratings
    private final Map<String, List<Map<String, Object>>> ratings = new ConcurrentHashMap<>();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RatingService() {
        // Seed some sample ratings
        addRating("100", 4, 5, 4, "Clean bus, good driver");
        addRating("100", 3, 4, 3, "Bit crowded but on time");
        addRating("138", 5, 5, 5, "Excellent service!");
        addRating("138", 4, 3, 4, "Good overall");
        addRating("183", 3, 4, 3, "Average condition");
        addRating("224", 4, 4, 5, "Very punctual");
        addRating("255", 2, 3, 2, "Bus was late and dirty");
        addRating("400", 5, 5, 4, "Express service, fast");
        addRating("192", 3, 3, 3, "Normal service");
        addRating("1", 4, 4, 4, "Long route but comfortable");
    }

    /**
     * Add a new rating
     */
    public Map<String, Object> addRating(String busNumber, int cleanliness, int driver, int punctuality, String comment) {
        Map<String, Object> rating = new LinkedHashMap<>();
        rating.put("busNumber", busNumber);
        rating.put("cleanliness", Math.min(5, Math.max(1, cleanliness)));
        rating.put("driver", Math.min(5, Math.max(1, driver)));
        rating.put("punctuality", Math.min(5, Math.max(1, punctuality)));
        rating.put("overall", Math.round((cleanliness + driver + punctuality) / 3.0 * 10) / 10.0);
        rating.put("comment", comment);
        rating.put("timestamp", LocalDateTime.now().format(FMT));

        ratings.computeIfAbsent(busNumber, k -> new ArrayList<>()).add(rating);
        return rating;
    }

    /**
     * Get ratings for a bus
     */
    public Map<String, Object> getRatings(String busNumber) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("busNumber", busNumber);

        List<Map<String, Object>> busRatings = ratings.getOrDefault(busNumber, List.of());
        result.put("totalReviews", busRatings.size());

        if (busRatings.isEmpty()) {
            result.put("avgCleanliness", 0);
            result.put("avgDriver", 0);
            result.put("avgPunctuality", 0);
            result.put("avgOverall", 0);
            result.put("reviews", List.of());
            return result;
        }
        double avgC = busRatings.stream().mapToInt(r -> (int) r.get("cleanliness")).average().orElse(0);
        double avgD = busRatings.stream().mapToInt(r -> (int) r.get("driver")).average().orElse(0);
        double avgP = busRatings.stream().mapToInt(r -> (int) r.get("punctuality")).average().orElse(0);
        double avgO = (avgC + avgD + avgP) / 3.0;

        result.put("avgCleanliness", Math.round(avgC * 10) / 10.0);
        result.put("avgDriver", Math.round(avgD * 10) / 10.0);
        result.put("avgPunctuality", Math.round(avgP * 10) / 10.0);
        result.put("avgOverall", Math.round(avgO * 10) / 10.0);

        // Return latest reviews (newest first)
        List<Map<String, Object>> sorted = new ArrayList<>(busRatings);
        Collections.reverse(sorted);
        result.put("reviews", sorted.stream().limit(10).toList());

        return result;
    }

    /**
     * Get top-rated buses
     */
    public List<Map<String, Object>> getTopBuses() {
        List<Map<String, Object>> top = new ArrayList<>();
        for (var entry : ratings.entrySet()) {
            Map<String, Object> bus = getRatings(entry.getKey());
            top.add(bus);
        }
        top.sort((a, b) -> Double.compare((double) b.get("avgOverall"), (double) a.get("avgOverall")));
        return top;
    }
}

