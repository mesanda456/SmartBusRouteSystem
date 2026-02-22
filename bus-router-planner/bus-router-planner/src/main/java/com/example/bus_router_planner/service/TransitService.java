package com.example.bus_router_planner.service;

import com.example.bus_router_planner.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Builds Google Maps-style transit options from route results.
 * Converts raw path data into structured segments with bus numbers,
 * walking legs, departure/arrival times, and cost breakdown.
 */
@Service
public class TransitService {

    @Autowired
    private RouteService routeService;

    @Autowired
    private RoadSegmentService roadSegmentService;

    @Autowired
    private CrowdPredictionService crowdPredictionService;

    // ==================== BUS ROUTE MAPPING ====================
    // Maps corridor segments to real Sri Lankan bus numbers
    private static final Map<String, List<String>> BUS_NUMBERS = new HashMap<>();

    static {
        // Galle Road Corridor
        BUS_NUMBERS.put("FORT_PET", List.of("1","100","138","176","255"));
        BUS_NUMBERS.put("FORT_KOL", List.of("100","101","154","400"));
        BUS_NUMBERS.put("KOL_BAM",  List.of("100","101","154","400"));
        BUS_NUMBERS.put("BAM_WEL",  List.of("100","101","154","400"));
        BUS_NUMBERS.put("WEL_DEH",  List.of("100","101","154","400"));
        BUS_NUMBERS.put("DEH_MLV",  List.of("100","101","400"));
        BUS_NUMBERS.put("MLV_RAT",  List.of("100","101","400"));
        BUS_NUMBERS.put("RAT_MOR",  List.of("100","400","350"));
        BUS_NUMBERS.put("MOR_PAN",  List.of("400","350","2"));

        // Inner city
        BUS_NUMBERS.put("PET_MAR",  List.of("1","138","177","224"));
        BUS_NUMBERS.put("MAR_BOR",  List.of("138","177","224","255"));
        BUS_NUMBERS.put("BOR_TWN",  List.of("138","103"));
        BUS_NUMBERS.put("TWN_KOL",  List.of("103","154"));
        BUS_NUMBERS.put("TWN_NAR",  List.of("103","174"));
        BUS_NUMBERS.put("BAM_NAR",  List.of("174","168"));

        // Nugegoda corridor
        BUS_NUMBERS.put("BOR_NUG",  List.of("138","224","255","149"));
        BUS_NUMBERS.put("NAR_NUG",  List.of("138","174","224"));
        BUS_NUMBERS.put("NUG_WIJ",  List.of("255","224"));
        BUS_NUMBERS.put("WIJ_DEH",  List.of("255","251"));
        BUS_NUMBERS.put("NUG_MAH",  List.of("138","224","255","149"));
        BUS_NUMBERS.put("MAH_KOT",  List.of("138","255","2/2"));
        BUS_NUMBERS.put("KOT_HOM",  List.of("138","2/2"));

        // Kandy road
        BUS_NUMBERS.put("BOR_RAJ",  List.of("177","103","188"));
        BUS_NUMBERS.put("PET_RAJ",  List.of("177","188"));
        BUS_NUMBERS.put("RAJ_BAT",  List.of("177","188","1"));
        BUS_NUMBERS.put("BAT_MAL",  List.of("177","1"));
        BUS_NUMBERS.put("MAL_KAD",  List.of("1","177"));
        BUS_NUMBERS.put("BAT_NUG",  List.of("149","174"));

        // Kelaniya
        BUS_NUMBERS.put("PET_KEL",  List.of("235","245","246"));
        BUS_NUMBERS.put("KEL_KIR",  List.of("235","245"));
        BUS_NUMBERS.put("KEL_KDW",  List.of("246","243"));
        BUS_NUMBERS.put("KDW_KIR",  List.of("243","245"));

        // Piliyandala
        BUS_NUMBERS.put("MAH_PIL",  List.of("350","370"));
        BUS_NUMBERS.put("PIL_HOR",  List.of("350","370"));
        BUS_NUMBERS.put("RAT_PIL",  List.of("350","370"));

        // Cross routes
        BUS_NUMBERS.put("NUG_RAJ",  List.of("149","174"));
        BUS_NUMBERS.put("MAH_MAL",  List.of("255","149"));
        BUS_NUMBERS.put("KAD_KDW",  List.of("243","1"));

        // Route 183: Moratuwa → Nugegoda (via Mt Lavinia coastal)
        BUS_NUMBERS.put("MOR_KRJ",  List.of("183","155"));
        BUS_NUMBERS.put("KRJ_KTB",  List.of("183","155"));
        BUS_NUMBERS.put("KTB_ANG",  List.of("183","155"));
        BUS_NUMBERS.put("ANG_SOY",  List.of("183","155"));
        BUS_NUMBERS.put("SOY_RAT",  List.of("183","155"));
        BUS_NUMBERS.put("RAT_GOL",  List.of("183","155"));
        BUS_NUMBERS.put("GOL_VIJ",  List.of("183","155"));
        BUS_NUMBERS.put("VIJ_MLV",  List.of("183","155"));
        BUS_NUMBERS.put("DEH_KLB",  List.of("183"));
        BUS_NUMBERS.put("KLB_KHW",  List.of("183"));
        BUS_NUMBERS.put("KHW_NUG",  List.of("183"));

        // Route 192/117: Moratuwa → Nugegoda (via Attidiya inland)
        BUS_NUMBERS.put("VIJ_ATT",  List.of("192","117"));
        BUS_NUMBERS.put("ATT_PAP",  List.of("192","117"));
        BUS_NUMBERS.put("PAP_GAM",  List.of("192","117"));
        BUS_NUMBERS.put("GAM_NUG",  List.of("192","117"));
    }

    /**
     * Get bus numbers for a segment between two stops.
     */
    public List<String> getBusNumbers(String fromId, String toId) {
        List<String> buses = BUS_NUMBERS.get(fromId + "_" + toId);
        if (buses != null) return buses;
        buses = BUS_NUMBERS.get(toId + "_" + fromId);
        if (buses != null) return buses;
        return List.of("Local");
    }

    /**
     * Find all transit options (like Google Maps transit tab).
     * Returns multiple route options with different strategies.
     */
    public List<TransitOption> findTransitOptions(String sourceId,
                                                  String destId,
                                                  String mode) {

        List<TransitOption> options = new ArrayList<>();
        List<RouteResponse> routes = new ArrayList<>();

        // Dijkstra route
        RouteRequest mainRequest =
                new RouteRequest(sourceId, destId, "dijkstra", "distance", false);
        RouteResponse mainRoute = routeService.findRoute(mainRequest);

        if (mainRoute.isFound()) {
            routes.add(mainRoute);
        }

        // A* route
        RouteRequest aStarRequest =
                new RouteRequest(sourceId, destId, "astar", mode, false);

        RouteResponse aStarRoute = routeService.findRoute(aStarRequest);

        if (aStarRoute.isFound()) {
            routes.add(aStarRoute);
        }

        String[] labels = {"Primary Route", "Alternative Route"};
        LocalTime now = LocalTime.now();

        for (int i = 0; i < routes.size(); i++) {
            RouteResponse route = routes.get(i);
            if (!route.isFound() || route.getPath() == null) continue;

            LocalTime departure = now.plusMinutes(5 + (i * 10));

            TransitOption option = buildTransitOption(
                    route,
                    departure,
                    i < labels.length ? labels[i] : "Alternative " + (i + 1)
            );

            options.add(option);
        }

        return options;
    }

    /**
     * Build a single TransitOption from a RouteResponse.
     */
    private TransitOption buildTransitOption(RouteResponse route, LocalTime departure, String label) {
        TransitOption option = new TransitOption();
        List<TransitSegment> segments = new ArrayList<>();
        List<BusStop> path = route.getPath();

        int totalTime = 0;
        int totalDistance = 0;
        int totalCost = 0;
        Set<String> allBuses = new LinkedHashSet<>();

        // 1. Add initial walk segment (walk to first bus stop)
        BusStop firstStop = path.get(0);
        int walkToStop = 3 + new Random(firstStop.getId().hashCode()).nextInt(5); // 3-7 min walk
        segments.add(TransitSegment.walk(
                "Your Location", "LOC", firstStop.getName(), firstStop.getId(),
                walkToStop,
                firstStop.getLatitude() + 0.001, firstStop.getLongitude() + 0.001,
                firstStop.getLatitude(), firstStop.getLongitude()
        ));
        totalTime += walkToStop;

        // 2. Build bus segments - group consecutive stops on same bus
        int i = 0;
        while (i < path.size() - 1) {
            // Find what bus serves this segment
            List<String> busesHere = getBusNumbers(path.get(i).getId(), path.get(i + 1).getId());
            String primaryBus = busesHere.get(0); // take first available bus

            // Group consecutive segments that share a common bus
            int segStart = i;
            int segTime = 0;
            int segDist = 0;
            int segCost = 0;
            int segStops = 0;
            List<BusStop> segPath = new ArrayList<>(); // track all stops in this bus leg
            segPath.add(path.get(i));

            while (i < path.size() - 1) {
                List<String> nextBuses = getBusNumbers(path.get(i).getId(), path.get(i + 1).getId());
                if (!nextBuses.contains(primaryBus)) break; // need to transfer

                segTime += estimateTime(path.get(i), path.get(i + 1));
                segDist += estimateDistance(path.get(i), path.get(i + 1));
                segCost += estimateCost(path.get(i), path.get(i + 1));
                segStops++;
                i++;
                segPath.add(path.get(i));
            }

            allBuses.add(primaryBus);

            TransitSegment busSeg = TransitSegment.bus(
                    primaryBus,
                    path.get(segStart).getName(), path.get(segStart).getId(),
                    path.get(i).getName(), path.get(i).getId(),
                    segTime, segDist, segCost, segStops,
                    path.get(segStart).getLatitude(), path.get(segStart).getLongitude(),
                    path.get(i).getLatitude(), path.get(i).getLongitude()
            );

            // ✅ Attach road-following polyline for this bus segment
            List<double[]> fullPolyline = new ArrayList<>();
            for (int p = 0; p < segPath.size() - 1; p++) {
                List<double[]> roadPts = roadSegmentService.getSegment(segPath.get(p), segPath.get(p + 1));
                if (fullPolyline.isEmpty()) {
                    fullPolyline.addAll(roadPts);
                } else {
                    // skip first point to avoid duplicates at junctions
                    for (int r = 1; r < roadPts.size(); r++) {
                        fullPolyline.add(roadPts.get(r));
                    }
                }
            }
            busSeg.setPolyline(fullPolyline);

            segments.add(busSeg);

            totalTime += segTime;
            totalDistance += segDist;
            totalCost += segCost;

            // If there are more segments, add a transfer walk
            if (i < path.size() - 1) {
                int transferWalk = 2 + new Random(path.get(i).getId().hashCode()).nextInt(4);
                segments.add(TransitSegment.walk(
                        path.get(i).getName(), path.get(i).getId(),
                        path.get(i).getName() + " (Transfer)", path.get(i).getId(),
                        transferWalk,
                        path.get(i).getLatitude(), path.get(i).getLongitude(),
                        path.get(i).getLatitude(), path.get(i).getLongitude()
                ));
                totalTime += transferWalk;
            }
        }

        // 3. Add final walk segment
        BusStop lastStop = path.get(path.size() - 1);
        int walkFromStop = 2 + new Random(lastStop.getId().hashCode() + 1).nextInt(5);
        segments.add(TransitSegment.walk(
                lastStop.getName(), lastStop.getId(),
                "Destination", "DEST",
                walkFromStop,
                lastStop.getLatitude(), lastStop.getLongitude(),
                lastStop.getLatitude() - 0.001, lastStop.getLongitude() + 0.001
        ));
        totalTime += walkFromStop;

        // Build final option
        LocalTime arrival = departure.plusMinutes(totalTime);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a");

        option.setDepartureTime(departure.format(fmt));
        option.setArrivalTime(arrival.format(fmt));
        option.setTotalDurationMinutes(totalTime);
        option.setTotalDistanceKm(totalDistance);
        option.setTotalCostRs(totalCost);
        option.setTotalStops(path.size());
        option.setTransfers(Math.max(0, (int) segments.stream().filter(s -> "bus".equals(s.getType())).count() - 1));
        option.setBusNumbers(new ArrayList<>(allBuses));
        option.setSegments(segments);
        option.setLabel(label);
        option.setAlgorithm(route.getAlgorithm());
        // Crowd prediction
        String crowd = crowdPredictionService.predictCrowd(departure);
        int seats = crowdPredictionService.estimateSeatAvailability(crowd);

        option.setCrowdLevel(crowd);
        option.setAvailableSeats(seats);


        option.setExecutionTime(route.getExecutionTime());
        option.setNodesVisited(route.getNodesVisited());

        return option;
    }

    public List<TransitOption> compareTransit(String sourceId,
                                              String destId,
                                              String mode) {

        List<TransitOption> options = new ArrayList<>();

        String[] algorithms = {"dijkstra", "bfs", "astar"};
        LocalTime now = LocalTime.now();

        for (int i = 0; i < algorithms.length; i++) {

            RouteRequest request =
                    new RouteRequest(sourceId, destId, algorithms[i], mode, false);

            RouteResponse route = routeService.findRoute(request);

            if (route.isFound()) {

                TransitOption option = buildTransitOption(
                        route,
                        now.plusMinutes(5 + (i * 5)),
                        algorithms[i].toUpperCase()
                );

                options.add(option);
            }
        }

        return options;
    }

    // ---- Estimate helpers (fallback values based on coordinate distance) ----

    private int estimateTime(BusStop a, BusStop b) {
        double dist = haversine(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
        return Math.max(5, (int)(dist / 0.5)); // ~30 km/h average bus speed
    }

    private int estimateDistance(BusStop a, BusStop b) {
        return Math.max(1, (int) Math.round(
                haversine(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude())
        ));
    }

    private int estimateCost(BusStop a, BusStop b) {
        int dist = estimateDistance(a, b);
        return Math.max(20, dist * 15); // ~Rs.15/km
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}