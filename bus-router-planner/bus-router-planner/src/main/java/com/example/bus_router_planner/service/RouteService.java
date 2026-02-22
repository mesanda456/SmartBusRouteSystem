package com.example.bus_router_planner.service;

import com.example.bus_router_planner.algorithms.*;
import com.example.bus_router_planner.graph.Graph;
import com.example.bus_router_planner.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
public class RouteService {


    @Autowired
    private CrowdPredictionService crowdPredictionService;

    private Graph graph;
    private List<BusStop> allStops;

    @Autowired
    private RoadSegmentService roadSegmentService;

    @PostConstruct
    public void initializeGraph() {

        graph = new Graph();
        allStops = new ArrayList<>();

        // ==================== COLOMBO CITY ====================
        BusStop fortRailway    = new BusStop("FORT",   "Fort Railway Station",      6.9344, 79.8428);
        BusStop pettah         = new BusStop("PET",    "Pettah Bus Stand",          6.9387, 79.8550);
        BusStop kollupitiya    = new BusStop("KOL",    "Kollupitiya Junction",      6.9147, 79.8501);
        BusStop bambalapitiya  = new BusStop("BAM",    "Bambalapitiya",             6.8934, 79.8553);
        BusStop wellawatte     = new BusStop("WEL",    "Wellawatte",                6.8747, 79.8598);
        BusStop maradana       = new BusStop("MAR",    "Maradana",                  6.9297, 79.8638);
        BusStop borella        = new BusStop("BOR",    "Borella Junction",          6.9180, 79.8770);

        // ==================== SOUTHERN COLOMBO ====================
        BusStop dehiwala       = new BusStop("DEH",    "Dehiwala",                  6.8569, 79.8642);
        BusStop mountLavinia   = new BusStop("MLV",    "Mount Lavinia",             6.8389, 79.8630);
        BusStop ratmalana      = new BusStop("RAT",    "Ratmalana",                 6.8220, 79.8760);
        BusStop moratuwa       = new BusStop("MOR",    "Moratuwa",                  6.7730, 79.8820);
        BusStop panadura       = new BusStop("PAN",    "Panadura",                  6.7130, 79.9070);

        // ==================== NUGEGODA - MAHARAGAMA CORRIDOR ====================
        BusStop nugegoda       = new BusStop("NUG",    "Nugegoda",                  6.8722, 79.8892);
        BusStop wijerama       = new BusStop("WIJ",    "Wijerama Junction",         6.8650, 79.8830);
        BusStop maharagama     = new BusStop("MAH",    "Maharagama",                6.8470, 79.9270);
        BusStop kottawa        = new BusStop("KOT",    "Kottawa",                   6.8410, 79.9600);
        BusStop homagama       = new BusStop("HOM",    "Homagama",                  6.8440, 79.9980);

        // ==================== KANDY ROAD CORRIDOR ====================
        BusStop rajagiriya     = new BusStop("RAJ",    "Rajagiriya",                6.9060, 79.8960);
        BusStop battaramulla   = new BusStop("BAT",    "Battaramulla",              6.8990, 79.9170);
        BusStop malabe          = new BusStop("MAL",    "Malabe",                    6.9050, 79.9530);
        BusStop kaduwela       = new BusStop("KAD",    "Kaduwela",                  6.9320, 79.9840);
        BusStop kadawatha      = new BusStop("KDW",    "Kadawatha",                 6.9800, 79.9530);

        // ==================== KIRIBATHGODA - KELANIYA ====================
        BusStop kelaniya       = new BusStop("KEL",    "Kelaniya",                  6.9560, 79.9220);
        BusStop kiribathgoda   = new BusStop("KIR",    "Kiribathgoda",              6.9780, 79.9280);

        // ==================== PILIYANDALA - HORANA ====================
        BusStop piliyandala    = new BusStop("PIL",    "Piliyandala",               6.8010, 79.9220);
        BusStop horana         = new BusStop("HOR",    "Horana",                    6.7160, 80.0620);

        // ==================== COLOMBO 7 / CITY CENTRE ====================
        BusStop townHall       = new BusStop("TWN",    "Town Hall (Colombo 7)",     6.9172, 79.8641);
        BusStop narahenpita    = new BusStop("NAR",    "Narahenpita",               6.8990, 79.8760);

        // ==================== ROUTE 183 & 192/117: MORATUWA → NUGEGODA (New Stops) ====================
        BusStop kurusaJn       = new BusStop("KRJ",    "Moratuwa Kurusa Junction",  6.7788, 79.8825);
        BusStop katubedda      = new BusStop("KTB",    "Katubedda",                 6.7968, 79.8884);
        BusStop angulana       = new BusStop("ANG",    "Angulana",                  6.8033, 79.8875);
        BusStop soysapura      = new BusStop("SOY",    "Soysapura",                 6.8055, 79.8858);
        BusStop golumadama     = new BusStop("GOL",    "Golumadama Junction",       6.8107, 79.8823);
        BusStop vijitha        = new BusStop("VIJ",    "Vijitha",                   6.8174, 79.8751);
        BusStop kalubowila     = new BusStop("KLB",    "Kalubowila",                6.8668, 79.8762);
        BusStop kohuwala       = new BusStop("KHW",    "Kohuwala",                  6.8674, 79.8850);
        // Route 192/117 specific stops
        BusStop attidiya       = new BusStop("ATT",    "Attidiya Junction",         6.8399, 79.8846);
        BusStop papiliyana     = new BusStop("PAP",    "Papiliyana",                6.8566, 79.8898);
        BusStop gamsabhaJn     = new BusStop("GAM",    "Gamsabha Junction",         6.8666, 79.8971);

        // Add all stops
        allStops.add(fortRailway);
        allStops.add(pettah);
        allStops.add(kollupitiya);
        allStops.add(bambalapitiya);
        allStops.add(wellawatte);
        allStops.add(maradana);
        allStops.add(borella);
        allStops.add(dehiwala);
        allStops.add(mountLavinia);
        allStops.add(ratmalana);
        allStops.add(moratuwa);
        allStops.add(panadura);
        allStops.add(nugegoda);
        allStops.add(wijerama);
        allStops.add(maharagama);
        allStops.add(kottawa);
        allStops.add(homagama);
        allStops.add(rajagiriya);
        allStops.add(battaramulla);
        allStops.add(malabe);
        allStops.add(kaduwela);
        allStops.add(kadawatha);
        allStops.add(kelaniya);
        allStops.add(kiribathgoda);
        allStops.add(piliyandala);
        allStops.add(horana);
        allStops.add(townHall);
        allStops.add(narahenpita);
        // Route 183 & 192/117 new stops
        allStops.add(kurusaJn);
        allStops.add(katubedda);
        allStops.add(angulana);
        allStops.add(soysapura);
        allStops.add(golumadama);
        allStops.add(vijitha);
        allStops.add(kalubowila);
        allStops.add(kohuwala);
        allStops.add(attidiya);
        allStops.add(papiliyana);
        allStops.add(gamsabhaJn);

        allStops.forEach(graph::addStop);

        // ==================== GALLE ROAD CORRIDOR (138, 100, 400) ====================
        graph.addBidirectionalRoute(fortRailway, pettah,        3,  15, 30, true);
        graph.addBidirectionalRoute(fortRailway, kollupitiya,   2,  10, 25, true);
        graph.addBidirectionalRoute(kollupitiya, bambalapitiya,  2,   8, 25, true);
        graph.addBidirectionalRoute(bambalapitiya, wellawatte,   2,   8, 20, true);
        graph.addBidirectionalRoute(wellawatte, dehiwala,        3,  12, 30, true);
        graph.addBidirectionalRoute(dehiwala, mountLavinia,      2,  10, 25, true);
        graph.addBidirectionalRoute(mountLavinia, ratmalana,     2,   8, 20, true);
        graph.addBidirectionalRoute(ratmalana, moratuwa,         5,  20, 40, true);
        graph.addBidirectionalRoute(moratuwa, panadura,          8,  30, 50, true);

        // ==================== COLOMBO INNER CITY ====================
        graph.addBidirectionalRoute(pettah, maradana,            2,  10, 20, true);
        graph.addBidirectionalRoute(maradana, borella,           2,  10, 20, true);
        graph.addBidirectionalRoute(borella, townHall,           1,   5, 15, true);
        graph.addBidirectionalRoute(townHall, kollupitiya,       2,   8, 20, true);
        graph.addBidirectionalRoute(townHall, narahenpita,       2,   8, 20, true);
        graph.addBidirectionalRoute(bambalapitiya, narahenpita,  2,  10, 25, true);

        // ==================== NUGEGODA CORRIDOR (138, 224, 255) ====================
        graph.addBidirectionalRoute(borella, nugegoda,           5,  20, 40, true);
        graph.addBidirectionalRoute(narahenpita, nugegoda,       3,  12, 30, true);
        graph.addBidirectionalRoute(nugegoda, wijerama,          1,   5, 15, true);
        graph.addBidirectionalRoute(wijerama, dehiwala,          3,  12, 30, true);
        graph.addBidirectionalRoute(nugegoda, maharagama,        5,  20, 40, true);
        graph.addBidirectionalRoute(maharagama, kottawa,         5,  18, 35, true);
        graph.addBidirectionalRoute(kottawa, homagama,           7,  25, 45, true);

        // ==================== KANDY ROAD (1, 4/4, Kandy route) ====================
        graph.addBidirectionalRoute(borella, rajagiriya,         3,  12, 25, true);
        graph.addBidirectionalRoute(pettah, rajagiriya,          5,  20, 35, true);
        graph.addBidirectionalRoute(rajagiriya, battaramulla,    3,  12, 25, true);
        graph.addBidirectionalRoute(battaramulla, malabe,        5,  18, 35, true);
        graph.addBidirectionalRoute(malabe, kaduwela,            4,  15, 30, true);
        graph.addBidirectionalRoute(battaramulla, nugegoda,      4,  15, 30, true);

        // ==================== KELANIYA / KIRIBATHGODA ====================
        graph.addBidirectionalRoute(pettah, kelaniya,            5,  20, 35, true);
        graph.addBidirectionalRoute(kelaniya, kiribathgoda,      3,  12, 25, true);
        graph.addBidirectionalRoute(kelaniya, kadawatha,         4,  15, 30, true);
        graph.addBidirectionalRoute(kadawatha, kiribathgoda,     3,  10, 20, true);

        // ==================== PILIYANDALA CORRIDOR ====================
        graph.addBidirectionalRoute(maharagama, piliyandala,     4,  15, 30, true);
        graph.addBidirectionalRoute(piliyandala, horana,        12,  40, 60, true);
        graph.addBidirectionalRoute(ratmalana, piliyandala,      5,  20, 35, true);

        // ==================== CROSS ROUTES ====================
        graph.addBidirectionalRoute(nugegoda, rajagiriya,        5,  18, 35, true);
        graph.addBidirectionalRoute(maharagama, malabe,           4,  15, 30, true);
        graph.addBidirectionalRoute(kaduwela, kadawatha,          6,  22, 40, false);

        // ==================== ROUTE 183: MORATUWA → NUGEGODA (via Mt Lavinia/Dehiwala) ====================
        graph.addBidirectionalRoute(moratuwa, kurusaJn,          1,   3, 15, true);
        graph.addBidirectionalRoute(kurusaJn, katubedda,         2,   6, 20, true);
        graph.addBidirectionalRoute(katubedda, angulana,         1,   3, 15, true);
        graph.addBidirectionalRoute(angulana, soysapura,         1,   2, 10, true);
        graph.addBidirectionalRoute(soysapura, ratmalana,        1,   3, 10, true);
        graph.addBidirectionalRoute(ratmalana, golumadama,       1,   3, 10, true);
        graph.addBidirectionalRoute(golumadama, vijitha,         1,   3, 10, true);
        graph.addBidirectionalRoute(vijitha, mountLavinia,       1,   4, 15, true);
        graph.addBidirectionalRoute(mountLavinia, dehiwala,      2,  10, 25, true);  // already exists
        graph.addBidirectionalRoute(dehiwala, kalubowila,        2,   8, 20, true);
        graph.addBidirectionalRoute(kalubowila, kohuwala,        1,   4, 15, true);
        graph.addBidirectionalRoute(kohuwala, nugegoda,          1,   4, 15, true);

        // ==================== ROUTE 192/117: MORATUWA → NUGEGODA (via Attidiya/Papiliyana) ====================
        // Shares Moratuwa→Vijitha with Route 183, then branches inland
        graph.addBidirectionalRoute(vijitha, attidiya,           3,  10, 25, true);
        graph.addBidirectionalRoute(attidiya, papiliyana,        2,   8, 20, true);
        graph.addBidirectionalRoute(papiliyana, gamsabhaJn,      2,   6, 15, true);
        graph.addBidirectionalRoute(gamsabhaJn, nugegoda,        1,   5, 15, true);

        System.out.println("✅ Graph initialized with " + allStops.size() + " bus stops and extended Sri Lanka routes");
    }

    public List<BusStop> getAllBusStops() {
        return allStops;
    }

    /**
     * Search bus stops by name (partial match, case-insensitive)
     */
    public List<BusStop> searchStops(String query) {
        if (query == null || query.trim().isEmpty()) {
            return allStops;
        }
        String lower = query.trim().toLowerCase();
        return allStops.stream()
                .filter(s -> s.getName().toLowerCase().contains(lower)
                        || s.getId().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public RouteResponse findRoute(RouteRequest request) {

        try {

            if (request.getSource() == null || request.getDestination() == null) {
                return RouteResponse.error("Source and destination are required");
            }

            BusStop source = graph.findStopById(request.getSource());
            BusStop destination = graph.findStopById(request.getDestination());

            if (source == null || destination == null) {
                return RouteResponse.error("Invalid source or destination");
            }

            if (source.equals(destination)) {
                return RouteResponse.error("Source and destination cannot be the same");
            }

            String algorithm = request.getAlgorithm() != null
                    ? request.getAlgorithm().toLowerCase()
                    : "dijkstra";

            String mode = request.getMode() != null
                    ? request.getMode().toLowerCase()
                    : "distance";

            RouteResponse response;

            switch (algorithm) {

                case "bfs":
                    response = BFS.getRoute(graph, source, destination);
                    break;

                case "astar":
                    response = AStar.getRoute(graph, source, destination, mode);
                    break;

                case "balanced":
                    response = AlternativeRoutes.getBalancedRoute(
                            graph, source, destination, 0.33, 0.33, 0.34
                    );
                    break;

                case "dijkstra":
                default:
                    response = Dijkstra.getRoute(
                            graph, source, destination, mode, request.isEmergencyOnly()
                    );
            }

            attachPolylines(response);

            // Crowd prediction
            LocalTime t = (request.getDepartureTime() != null)
                    ? LocalTime.parse(request.getDepartureTime())
                    : LocalTime.now();
            String crowd = crowdPredictionService.predictCrowd(t);
            response.setCrowdLevel(crowd);
            response.setSeatsAvailable(crowdPredictionService.estimateSeatAvailability(crowd));
            response.setCrowdMessage(crowd.equals("HIGH") ? "Very crowded at this time"
                    : crowd.equals("MEDIUM") ? "Moderately busy" : "Good time to travel");
            return response;

        } catch (Exception e) {

            e.printStackTrace();

            RouteResponse error = new RouteResponse();
            error.setFound(false);
            error.setMessage("Routing engine error");
            return error;
        }
    }

    public List<RouteResponse> getAlternativeRoutes(String sourceId, String destinationId) {

        BusStop source = graph.findStopById(sourceId);
        BusStop destination = graph.findStopById(destinationId);

        if (source == null || destination == null) {
            return new ArrayList<>();
        }

        List<RouteResponse> routes =
                AlternativeRoutes.findAlternatives(graph, source, destination);

        for (RouteResponse route : routes) {
            attachPolylines(route);
        }

        return routes;
    }

    // ===================== HELPER METHOD =====================

    private void attachPolylines(RouteResponse response) {

        if (!response.isFound() || response.getPath() == null) return;

        List<List<double[]>> polylines = new ArrayList<>();
        List<BusStop> path = response.getPath();

        for (int i = 0; i < path.size() - 1; i++) {

            List<double[]> segment =
                    roadSegmentService.getSegment(
                            path.get(i),
                            path.get(i + 1)
                    );

            if (!segment.isEmpty()) {
                polylines.add(segment);
            }
        }

        response.setPolylines(polylines);


    }

    /**
     * Get all graph edges for visualization
     */
    public List<Map<String, Object>> getGraphEdges() {
        List<Map<String, Object>> edges = new ArrayList<>();
        for (var entry : graph.adjList.entrySet()) {
            BusStop from = entry.getKey();
            for (var edge : entry.getValue()) {
                Map<String, Object> e = new LinkedHashMap<>();
                e.put("from", from.getId());
                e.put("to", edge.getDestination().getId());
                e.put("distance", edge.getDistance());
                e.put("time", edge.getTime());
                e.put("cost", edge.getCost());
                e.put("safe", edge.isSafe());
                e.put("fromLat", from.getLatitude());
                e.put("fromLng", from.getLongitude());
                e.put("toLat", edge.getDestination().getLatitude());
                e.put("toLng", edge.getDestination().getLongitude());
                edges.add(e);
            }
        }
        return edges;
    }

}