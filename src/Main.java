import model.*;
import graph.*;
import algorithms.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Graph g = new Graph();

        // ================= CREATE BUS STOPS =================
        BusStop colombo = new BusStop("Colombo");
        BusStop gampaha = new BusStop("Gampaha");
        BusStop kandy = new BusStop("Kandy");
        BusStop kurunegala = new BusStop("Kurunegala");

        BusStop[] stops = {colombo, gampaha, kandy, kurunegala};

        for (BusStop s : stops) g.addStop(s);

        // ================= ADD ROUTES =================
        g.addRoute(colombo, gampaha, 30, 45, 120, true);
        g.addRoute(gampaha, kurunegala, 60, 90, 200, true);
        g.addRoute(kurunegala, kandy, 40, 60, 150, true);
        g.addRoute(colombo, kandy, 120, 180, 400, false);

        // ================= USER INPUT =================
        System.out.println("Available Bus Stops:");
        for (int i = 0; i < stops.length; i++) {
            System.out.println((i + 1) + ". " + stops[i]);
        }

        System.out.print("Select Starting Stop (number): ");
        BusStop start = stops[sc.nextInt() - 1];

        System.out.print("Select Destination Stop (number): ");
        BusStop end = stops[sc.nextInt() - 1];

        System.out.println("\nSelect Route Type:");
        System.out.println("1. Shortest Distance");
        System.out.println("2. Fastest Time");
        System.out.println("3. Cheapest Cost");
        System.out.println("4. Fewest Stops");
        System.out.println("5. Emergency Safe Route");

        int choice = sc.nextInt();

        // ================= PROCESS =================
        if (choice == 4) {
            int stopsCount = BFS.minStops(g, start, end);
            System.out.println("\nMinimum Stops Required: " + stopsCount);
        } else {
            String mode = (choice == 2) ? "time"
                    : (choice == 3) ? "cost"
                    : "distance";

            boolean emergencyOnly = (choice == 5);

            Dijkstra.Result result =
                    (Dijkstra.Result) Dijkstra.findPath(g, start, mode, emergencyOnly);

            int value = result.distance.get(end);

            if (value == Integer.MAX_VALUE) {
                System.out.println("\nNo route available.");
            } else {
                System.out.println("\nBest Route Result:");
                System.out.println("From: " + start);
                System.out.println("To  : " + end);

                printPath(result.previous, start, end);

                if (mode.equals("distance"))
                    System.out.println("Total Distance: " + value + " km");
                else if (mode.equals("time"))
                    System.out.println("Total Time: " + value + " minutes");
                else
                    System.out.println("Total Cost: Rs. " + value);
            }
        }

        sc.close();
    }

    // ================= PATH PRINTING METHOD =================
    static void printPath(Map<BusStop, BusStop> prev,
                          BusStop start, BusStop end) {

        List<BusStop> path = new ArrayList<>();
        for (BusStop at = end; at != null; at = prev.get(at)) {
            path.add(at);
        }

        Collections.reverse(path);

        System.out.print("Route: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1)
                System.out.print(" -> ");
        }
        System.out.println();
    }
}
