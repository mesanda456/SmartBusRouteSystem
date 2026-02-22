package com.example.bus_router_planner.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TicketService {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private int ticketCounter = 1000;

    /**
     * Generate a bus ticket with QR data
     */
    public Map<String, Object> generateTicket(String fromStop, String toStop,
                                              String busNumber, double fare, double distanceKm) {
        Map<String, Object> ticket = new LinkedHashMap<>();

        String ticketId = "TKT-" + (++ticketCounter);
        String issueTime = LocalDateTime.now().format(FMT);

        ticket.put("ticketId", ticketId);
        ticket.put("fromStop", fromStop);
        ticket.put("toStop", toStop);
        ticket.put("busNumber", busNumber);
        ticket.put("fare", fare);
        ticket.put("distanceKm", distanceKm);
        ticket.put("issueTime", issueTime);
        ticket.put("validUntil", LocalDateTime.now().plusHours(3).format(FMT));
        ticket.put("status", "VALID");
        ticket.put("passengerType", "Adult");

        // QR code data string (will be encoded as QR on frontend)
        String qrData = String.format("SLBUS|%s|%s|%s|BUS:%s|Rs.%.0f|%s",
                ticketId, fromStop, toStop, busNumber, fare, issueTime);
        ticket.put("qrData", qrData);

        return ticket;
    }
}