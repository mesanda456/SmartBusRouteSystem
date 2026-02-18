package com.example.bus_router_planner.service;

import com.example.bus_router_planner.model.BusStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ScheduleService {

    @Autowired
    private RouteService routeService;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("h:mm a");

    // Bus frequency in minutes for each route
    private static final Map<String, Integer> BUS_FREQUENCY = new HashMap<>();
    static {
        BUS_FREQUENCY.put("100", 8);
        BUS_FREQUENCY.put("101", 15);
        BUS_FREQUENCY.put("138", 10);
        BUS_FREQUENCY.put("176", 20);
        BUS_FREQUENCY.put("177", 12);
        BUS_FREQUENCY.put("224", 15);
        BUS_FREQUENCY.put("255", 12);
        BUS_FREQUENCY.put("400", 10);
        BUS_FREQUENCY.put("1", 8);
        BUS_FREQUENCY.put("2", 20);
        BUS_FREQUENCY.put("103", 18);
        BUS_FREQUENCY.put("149", 20);
        BUS_FREQUENCY.put("154", 15);
        BUS_FREQUENCY.put("174", 18);
        BUS_FREQUENCY.put("188", 20);
        BUS_FREQUENCY.put("235", 15);
        BUS_FREQUENCY.put("245", 18);
        BUS_FREQUENCY.put("246", 20);
        BUS_FREQUENCY.put("350", 25);
        BUS_FREQUENCY.put("370", 30);
    }

    // First bus times
    private static final Map<String, String> FIRST_BUS = new HashMap<>();
    static {
        FIRST_BUS.put("100", "4:30 AM");
        FIRST_BUS.put("138", "4:45 AM");
        FIRST_BUS.put("400", "5:00 AM");
        FIRST_BUS.put("1", "4:30 AM");
        FIRST_BUS.put("224", "5:00 AM");
        FIRST_BUS.put("255", "5:15 AM");
    }

    // Last bus times
    private static final Map<String, String> LAST_BUS = new HashMap<>();
    static {
        LAST_BUS.put("100", "10:30 PM");
        LAST_BUS.put("138", "10:00 PM");
        LAST_BUS.put("400", "9:30 PM");
        LAST_BUS.put("1", "10:30 PM");
        LAST_BUS.put("224", "9:00 PM");
        LAST_BUS.put("255", "9:30 PM");
    }

    /**
     * Get upcoming departures for a bus stop
     */
    public Map<String, Object> getSchedule(String stopId) {
        Map<String, Object> schedule = new LinkedHashMap<>();
        schedule.put("stopId", stopId);

        BusStop stop = routeService.getAllBusStops().stream()
                .filter(s -> s.getId().equals(stopId))
                .findFirst().orElse(null);

        if (stop == null) {
            schedule.put("error", "Stop not found");
            return schedule;
        }

        schedule.put("stopName", stop.getName());

        LocalTime now = LocalTime.now();
        List<Map<String, String>> departures = new ArrayList<>();

        // Generate next 5 departures for buses serving this stop
        List<String> busesAtStop = getBusesAtStop(stopId);
        for (String bus : busesAtStop) {
            int freq = BUS_FREQUENCY.getOrDefault(bus, 15);
            // Find next departure after now
            int minutesSinceMidnight = now.getHour() * 60 + now.getMinute();
            int nextDep = ((minutesSinceMidnight / freq) + 1) * freq;

            for (int i = 0; i < 3; i++) {
                int depMin = nextDep + (i * freq);
                if (depMin >= 24 * 60) break;
                LocalTime depTime = LocalTime.of(depMin / 60, depMin % 60);
                int waitMin = depMin - minutesSinceMidnight;

                Map<String, String> dep = new LinkedHashMap<>();
                dep.put("busNumber", bus);
                dep.put("time", depTime.format(FMT));
                dep.put("waitMinutes", String.valueOf(Math.max(1, waitMin)));
                dep.put("type", Integer.parseInt(bus.replaceAll("[^0-9]", "0")) < 200 ? "SLTB" : "Private");
                departures.add(dep);
            }
        }

        // Sort by wait time
        departures.sort(Comparator.comparingInt(d -> Integer.parseInt(d.get("waitMinutes"))));
        schedule.put("departures", departures.stream().limit(10).toList());
        schedule.put("timestamp", now.format(FMT));

        return schedule;
    }

    /**
     * Get full timetable for a bus route
     */
    public Map<String, Object> getTimetable(String busNumber) {
        Map<String, Object> timetable = new LinkedHashMap<>();
        timetable.put("busNumber", busNumber);
        timetable.put("frequency", BUS_FREQUENCY.getOrDefault(busNumber, 15) + " min");
        timetable.put("firstBus", FIRST_BUS.getOrDefault(busNumber, "5:00 AM"));
        timetable.put("lastBus", LAST_BUS.getOrDefault(busNumber, "9:30 PM"));

        int freq = BUS_FREQUENCY.getOrDefault(busNumber, 15);
        List<String> times = new ArrayList<>();
        for (int m = 300; m < 22 * 60; m += freq) {
            times.add(LocalTime.of(m / 60, m % 60).format(FMT));
        }
        timetable.put("allDepartures", times);
        return timetable;
    }

    private List<String> getBusesAtStop(String stopId) {
        Map<String, List<String>> stopBuses = new HashMap<>();
        stopBuses.put("FORT", List.of("1", "100", "138", "176", "255", "400"));
        stopBuses.put("PET", List.of("1", "100", "138", "177", "224", "235", "245"));
        stopBuses.put("KOL", List.of("100", "101", "103", "154", "400"));
        stopBuses.put("BAM", List.of("100", "101", "154", "174", "400"));
        stopBuses.put("WEL", List.of("100", "101", "154", "400"));
        stopBuses.put("MAR", List.of("138", "177", "224", "255"));
        stopBuses.put("BOR", List.of("138", "177", "224", "255", "103"));
        stopBuses.put("DEH", List.of("100", "101", "255", "400"));
        stopBuses.put("MLV", List.of("100", "101", "400"));
        stopBuses.put("NUG", List.of("138", "224", "255", "149", "174"));
        stopBuses.put("MAH", List.of("138", "224", "255", "350"));
        stopBuses.put("RAJ", List.of("177", "103", "188"));
        stopBuses.put("KEL", List.of("235", "245", "246"));
        stopBuses.put("KIR", List.of("235", "245", "243"));
        stopBuses.put("TWN", List.of("103", "138", "154"));
        stopBuses.put("NAR", List.of("138", "174", "224"));
        stopBuses.put("RAT", List.of("100", "350", "400"));
        stopBuses.put("MOR", List.of("100", "350", "400"));
        stopBuses.put("PAN", List.of("2", "350", "400"));
        stopBuses.put("BAT", List.of("1", "177", "188"));
        stopBuses.put("MAL", List.of("1", "177"));
        stopBuses.put("KAD", List.of("1", "177", "243"));
        stopBuses.put("KDW", List.of("243", "245", "246"));
        stopBuses.put("WIJ", List.of("224", "255"));
        stopBuses.put("KOT", List.of("138", "255"));
        stopBuses.put("HOM", List.of("138"));
        stopBuses.put("PIL", List.of("350", "370"));
        stopBuses.put("HOR", List.of("350", "370"));

        return stopBuses.getOrDefault(stopId, List.of("Local"));
    }
}