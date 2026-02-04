package com.example.bus_router_planner.service;

import com.example.bus_router_planner.model.BusStop;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoadSegmentService {

    // key format: FROMID_TOID (example: A_B)
    private final Map<String, List<double[]>> roadSegments = new HashMap<>();

    public RoadSegmentService() {

        // A (Fort) → B (Pettah)
        roadSegments.put("A_B", List.of(
                new double[]{6.9344, 79.8428},
                new double[]{6.9352, 79.8460},
                new double[]{6.9368, 79.8505},
                new double[]{6.9387, 79.8550}
        ));

        // A (Fort) → C (Kollupitiya)
        roadSegments.put("A_C", List.of(
                new double[]{6.9344, 79.8428},
                new double[]{6.9298, 79.8452},
                new double[]{6.9221, 79.8479},
                new double[]{6.9147, 79.8501}
        ));

        // B (Pettah) → D (Bambalapitiya)
        roadSegments.put("B_D", List.of(
                new double[]{6.9387, 79.8550},
                new double[]{6.9255, 79.8573},
                new double[]{6.9109, 79.8567},
                new double[]{6.8934, 79.8553}
        ));

        // C (Kollupitiya) → D (Bambalapitiya)
        roadSegments.put("C_D", List.of(
                new double[]{6.9147, 79.8501},
                new double[]{6.9079, 79.8526},
                new double[]{6.9008, 79.8543},
                new double[]{6.8934, 79.8553}
        ));

        // D (Bambalapitiya) → E (Dehiwala)
        roadSegments.put("D_E", List.of(
                new double[]{6.8934, 79.8553},
                new double[]{6.8821, 79.8612},
                new double[]{6.8703, 79.8685},
                new double[]{6.8569, 79.8742}
        ));

        // D (Bambalapitiya) → F (Mount Lavinia)
        roadSegments.put("D_F", List.of(
                new double[]{6.8934, 79.8553},
                new double[]{6.8764, 79.8579},
                new double[]{6.8582, 79.8606},
                new double[]{6.8389, 79.8630}
        ));
    }

    /**
     * Returns road polyline between two bus stops.
     * Never returns null.
     */
    public List<double[]> getSegment(BusStop from, BusStop to) {

        if (from == null || to == null) {
            return Collections.emptyList();
        }

        String fromId = from.getId();
        String toId = to.getId();

        // forward
        String key = fromId + "_" + toId;
        if (roadSegments.containsKey(key)) {
            return roadSegments.get(key);
        }

        // reverse (auto reverse points)
        String reverseKey = toId + "_" + fromId;
        if (roadSegments.containsKey(reverseKey)) {
            List<double[]> reversed =
                    new ArrayList<>(roadSegments.get(reverseKey));
            Collections.reverse(reversed);
            return reversed;
        }

        // fallback: straight line (last resort)
        return List.of(
                new double[]{from.getLatitude(), from.getLongitude()},
                new double[]{to.getLatitude(), to.getLongitude()}
        );
    }
}
