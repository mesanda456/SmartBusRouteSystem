package com.example.bus_router_planner.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FareService {

    // SLTB fare structure (Rs. per km)
    private static final double SLTB_BASE_FARE = 20.0;
    private static final double SLTB_PER_KM = 3.50;

    // Private bus fare structure
    private static final double PRIVATE_BASE_FARE = 25.0;
    private static final double PRIVATE_PER_KM = 4.00;

    // AC bus fare structure
    private static final double AC_BASE_FARE = 50.0;
    private static final double AC_PER_KM = 8.00;

    /**
     * Calculate fare for a given distance
     */
    public Map<String, Object> calculateFare(double distanceKm) {
        Map<String, Object> fare = new LinkedHashMap<>();
        fare.put("distanceKm", Math.round(distanceKm * 10) / 10.0);

        Map<String, Object> sltb = new LinkedHashMap<>();
        sltb.put("type", "SLTB (Government)");
        sltb.put("fare", Math.round(SLTB_BASE_FARE + distanceKm * SLTB_PER_KM));
        sltb.put("baseFare", SLTB_BASE_FARE);
        sltb.put("perKm", SLTB_PER_KM);
        fare.put("sltb", sltb);

        Map<String, Object> pvt = new LinkedHashMap<>();
        pvt.put("type", "Private Bus");
        pvt.put("fare", Math.round(PRIVATE_BASE_FARE + distanceKm * PRIVATE_PER_KM));
        pvt.put("baseFare", PRIVATE_BASE_FARE);
        pvt.put("perKm", PRIVATE_PER_KM);
        fare.put("private", pvt);

        Map<String, Object> ac = new LinkedHashMap<>();
        ac.put("type", "AC / Luxury");
        ac.put("fare", Math.round(AC_BASE_FARE + distanceKm * AC_PER_KM));
        ac.put("baseFare", AC_BASE_FARE);
        ac.put("perKm", AC_PER_KM);
        fare.put("ac", ac);

        return fare;
    }

    /**
     * Get fare by bus number
     */
    public Map<String, Object> getFareByBus(String busNumber, double distanceKm) {
        String type;
        double base, perKm;

        int num = Integer.parseInt(busNumber.replaceAll("[^0-9]", "0"));
        if (num <= 99) {
            type = "SLTB"; base = SLTB_BASE_FARE; perKm = SLTB_PER_KM;
        } else if (num <= 299) {
            type = "Private"; base = PRIVATE_BASE_FARE; perKm = PRIVATE_PER_KM;
        } else {
            type = "Express/AC"; base = AC_BASE_FARE; perKm = AC_PER_KM;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("busNumber", busNumber);
        result.put("busType", type);
        result.put("distanceKm", distanceKm);
        result.put("fare", Math.round(base + distanceKm * perKm));
        return result;
    }
}