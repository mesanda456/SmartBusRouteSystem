package com.example.bus_router_planner.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class CrowdPredictionService {

    public String predictCrowd(LocalTime departureTime) {

        int hour = departureTime.getHour();

        // Morning peak
        if (hour >= 7 && hour <= 9) {
            return "HIGH";
        }

        // Evening peak
        if (hour >= 16 && hour <= 19) {
            return "HIGH";
        }

        // Lunch rush
        if (hour >= 12 && hour <= 13) {
            return "MEDIUM";
        }

        return "LOW";
    }

    public int estimateSeatAvailability(String crowdLevel) {

        switch (crowdLevel) {
            case "HIGH":
                return 5;   // Almost full
            case "MEDIUM":
                return 20;  // Half full
            default:
                return 40;  // Plenty seats
        }
    }
}