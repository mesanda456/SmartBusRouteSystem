package com.example.bus_router_planner.service;

import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TrafficService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("h:mm a");

    /**
     * Get current traffic alerts
     */
    public List<Map<String, Object>> getAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        // Rush hour alerts
        if ((hour >= 7 && hour <= 9) || (hour >= 16 && hour <= 19)) {
            alerts.add(makeAlert("warning", "Rush Hour Traffic",
                    "Heavy traffic on Galle Road (Fort - Dehiwala). Expect 15-20 min delays.",
                    "Galle Road", 6.9100, 79.8520));
            alerts.add(makeAlert("warning", "Congestion Alert",
                    "Slow traffic on Baseline Road (Borella - Nugegoda). Delays of 10-15 min.",
                    "Baseline Road", 6.9050, 79.8830));
            alerts.add(makeAlert("warning", "Kandy Road Congestion",
                    "Heavy traffic near Rajagiriya junction. Consider alternative routes.",
                    "Kandy Road", 6.9060, 79.8960));
        }

        // Construction alerts (always active)
        alerts.add(makeAlert("construction", "Road Construction",
                "Ongoing road widening near Kaduwela interchange. Single lane traffic.",
                "Kaduwela", 6.9320, 79.9840));

        alerts.add(makeAlert("info", "New Bus Stop",
                "Temporary bus stop at Maharagama due to road work. 100m from usual location.",
                "Maharagama", 6.8470, 79.9270));

        // Rain alert (afternoon)
        if (hour >= 14 && hour <= 18) {
            alerts.add(makeAlert("weather", "Rain Alert",
                    "Heavy rainfall expected. Possible flooding on low-lying roads near Wellawatte.",
                    "Wellawatte", 6.8747, 79.8598));
        }

        return alerts;
    }

    /**
     * Get traffic condition for a specific corridor
     */
    public Map<String, Object> getCorridorTraffic(String corridor) {
        Map<String, Object> traffic = new LinkedHashMap<>();
        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        String condition;
        int delayMinutes;
        String color;

        if ((hour >= 7 && hour <= 9) || (hour >= 16 && hour <= 19)) {
            condition = "Heavy"; delayMinutes = 15; color = "#EA4335";
        } else if ((hour >= 10 && hour <= 15)) {
            condition = "Moderate"; delayMinutes = 5; color = "#EA8600";
        } else {
            condition = "Light"; delayMinutes = 0; color = "#34A853";
        }

        traffic.put("corridor", corridor);
        traffic.put("condition", condition);
        traffic.put("delayMinutes", delayMinutes);
        traffic.put("color", color);
        traffic.put("timestamp", now.format(FMT));

        return traffic;
    }

    private Map<String, Object> makeAlert(String type, String title, String message,
                                          String location, double lat, double lng) {
        Map<String, Object> alert = new LinkedHashMap<>();
        alert.put("type", type);
        alert.put("title", title);
        alert.put("message", message);
        alert.put("location", location);
        alert.put("latitude", lat);
        alert.put("longitude", lng);
        alert.put("timestamp", LocalTime.now().format(FMT));
        return alert;
    }
}