package com.example.bus_router_planner.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class CrowdPredictionService {

    // Predict based on time only
    public String predictCrowd(LocalTime departureTime) {
        int hour = departureTime.getHour();
        if (hour >= 7 && hour <= 9) return "HIGH";
        if (hour >= 16 && hour <= 19) return "HIGH";
        if (hour >= 12 && hour <= 13) return "MEDIUM";
        if (hour >= 10 && hour <= 11) return "MEDIUM";
        return "LOW";
    }

    // Predict considering day of week
    public String predictCrowdAdvanced(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        int hour = dateTime.getHour();

        // Weekends are less crowded
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            if (hour >= 9 && hour <= 14) return "MEDIUM";
            return "LOW";
        }

        // Weekday peaks
        if (hour >= 7 && hour <= 9) return "HIGH";
        if (hour >= 16 && hour <= 19) return "HIGH";
        if (hour >= 12 && hour <= 13) return "MEDIUM";
        return "LOW";
    }

    // Stop-specific crowd variation
    public String predictCrowdForStop(String stopId, LocalTime time) {
        String base = predictCrowd(time);
        // Major interchange stops are always busier
        if ((stopId.equals("PET") || stopId.equals("FORT") || stopId.equals("NUG"))
                && base.equals("MEDIUM")) {
            return "HIGH";
        }
        return base;
    }

    public int estimateSeatAvailability(String crowdLevel) {
        switch (crowdLevel) {
            case "HIGH":   return 5;
            case "MEDIUM": return 20;
            default:       return 40;
        }
    }

    public int getCrowdPercentage(String crowdLevel) {
        switch (crowdLevel) {
            case "HIGH":   return 90;
            case "MEDIUM": return 50;
            default:       return 15;
        }
    }
}