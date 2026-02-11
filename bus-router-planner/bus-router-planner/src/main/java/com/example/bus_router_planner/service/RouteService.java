package com.example.bus_router_planner.service;

import com.example.bus_router_planner.model.BusStop;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoadSegmentService {

    // key format: FROMID_TOID
    private final Map<String, List<double[]>> roadSegments = new HashMap<>();

    public RoadSegmentService() {

        // ==================== GALLE ROAD CORRIDOR ====================

        // FORT → PET (Pettah)
        roadSegments.put("FORT_PET", List.of(
                new double[]{6.9344, 79.8428},
                new double[]{6.9352, 79.8460},
                new double[]{6.9368, 79.8505},
                new double[]{6.9387, 79.8550}
        ));

        // FORT → KOL (Kollupitiya)
        roadSegments.put("FORT_KOL", List.of(
                new double[]{6.9344, 79.8428},
                new double[]{6.9298, 79.8452},
                new double[]{6.9221, 79.8479},
                new double[]{6.9147, 79.8501}
        ));

        // KOL → BAM (Kollupitiya → Bambalapitiya)
        roadSegments.put("KOL_BAM", List.of(
                new double[]{6.9147, 79.8501},
                new double[]{6.9079, 79.8526},
                new double[]{6.9008, 79.8543},
                new double[]{6.8934, 79.8553}
        ));

        // BAM → WEL (Bambalapitiya → Wellawatte)
        roadSegments.put("BAM_WEL", List.of(
                new double[]{6.8934, 79.8553},
                new double[]{6.8870, 79.8570},
                new double[]{6.8810, 79.8585},
                new double[]{6.8747, 79.8598}
        ));

        // WEL → DEH (Wellawatte → Dehiwala)
        roadSegments.put("WEL_DEH", List.of(
                new double[]{6.8747, 79.8598},
                new double[]{6.8700, 79.8610},
                new double[]{6.8640, 79.8625},
                new double[]{6.8569, 79.8642}
        ));

        // DEH → MLV (Dehiwala → Mount Lavinia)
        roadSegments.put("DEH_MLV", List.of(
                new double[]{6.8569, 79.8642},
                new double[]{6.8510, 79.8640},
                new double[]{6.8450, 79.8635},
                new double[]{6.8389, 79.8630}
        ));

        // MLV → RAT (Mount Lavinia → Ratmalana)
        roadSegments.put("MLV_RAT", List.of(
                new double[]{6.8389, 79.8630},
                new double[]{6.8330, 79.8670},
                new double[]{6.8280, 79.8720},
                new double[]{6.8220, 79.8760}
        ));

        // RAT → MOR (Ratmalana → Moratuwa)
        roadSegments.put("RAT_MOR", List.of(
                new double[]{6.8220, 79.8760},
                new double[]{6.8100, 79.8780},
                new double[]{6.7930, 79.8800},
                new double[]{6.7730, 79.8820}
        ));

        // MOR → PAN (Moratuwa → Panadura)
        roadSegments.put("MOR_PAN", List.of(
                new double[]{6.7730, 79.8820},
                new double[]{6.7550, 79.8880},
                new double[]{6.7350, 79.8970},
                new double[]{6.7130, 79.9070}
        ));

        // ==================== COLOMBO INNER CITY ====================

        // PET → MAR (Pettah → Maradana)
        roadSegments.put("PET_MAR", List.of(
                new double[]{6.9387, 79.8550},
                new double[]{6.9370, 79.8575},
                new double[]{6.9340, 79.8610},
                new double[]{6.9297, 79.8638}
        ));

        // MAR → BOR (Maradana → Borella)
        roadSegments.put("MAR_BOR", List.of(
                new double[]{6.9297, 79.8638},
                new double[]{6.9260, 79.8680},
                new double[]{6.9220, 79.8730},
                new double[]{6.9180, 79.8770}
        ));

        // BOR → TWN (Borella → Town Hall)
        roadSegments.put("BOR_TWN", List.of(
                new double[]{6.9180, 79.8770},
                new double[]{6.9178, 79.8730},
                new double[]{6.9175, 79.8690},
                new double[]{6.9172, 79.8641}
        ));

        // TWN → KOL (Town Hall → Kollupitiya)
        roadSegments.put("TWN_KOL", List.of(
                new double[]{6.9172, 79.8641},
                new double[]{6.9165, 79.8600},
                new double[]{6.9157, 79.8550},
                new double[]{6.9147, 79.8501}
        ));

        // TWN → NAR (Town Hall → Narahenpita)
        roadSegments.put("TWN_NAR", List.of(
                new double[]{6.9172, 79.8641},
                new double[]{6.9120, 79.8670},
                new double[]{6.9050, 79.8720},
                new double[]{6.8990, 79.8760}
        ));

        // BAM → NAR (Bambalapitiya → Narahenpita)
        roadSegments.put("BAM_NAR", List.of(
                new double[]{6.8934, 79.8553},
                new double[]{6.8950, 79.8620},
                new double[]{6.8970, 79.8690},
                new double[]{6.8990, 79.8760}
        ));

        // ==================== NUGEGODA CORRIDOR ====================

        // BOR → NUG (Borella → Nugegoda)
        roadSegments.put("BOR_NUG", List.of(
                new double[]{6.9180, 79.8770},
                new double[]{6.9070, 79.8800},
                new double[]{6.8920, 79.8840},
                new double[]{6.8722, 79.8892}
        ));

        // NAR → NUG (Narahenpita → Nugegoda)
        roadSegments.put("NAR_NUG", List.of(
                new double[]{6.8990, 79.8760},
                new double[]{6.8900, 79.8800},
                new double[]{6.8810, 79.8850},
                new double[]{6.8722, 79.8892}
        ));

        // NUG → WIJ (Nugegoda → Wijerama)
        roadSegments.put("NUG_WIJ", List.of(
                new double[]{6.8722, 79.8892},
                new double[]{6.8700, 79.8870},
                new double[]{6.8675, 79.8850},
                new double[]{6.8650, 79.8830}
        ));

        // WIJ → DEH (Wijerama → Dehiwala)
        roadSegments.put("WIJ_DEH", List.of(
                new double[]{6.8650, 79.8830},
                new double[]{6.8630, 79.8780},
                new double[]{6.8600, 79.8720},
                new double[]{6.8569, 79.8642}
        ));

        // NUG → MAH (Nugegoda → Maharagama)
        roadSegments.put("NUG_MAH", List.of(
                new double[]{6.8722, 79.8892},
                new double[]{6.8660, 79.9010},
                new double[]{6.8560, 79.9140},
                new double[]{6.8470, 79.9270}
        ));

        // MAH → KOT (Maharagama → Kottawa)
        roadSegments.put("MAH_KOT", List.of(
                new double[]{6.8470, 79.9270},
                new double[]{6.8460, 79.9380},
                new double[]{6.8440, 79.9490},
                new double[]{6.8410, 79.9600}
        ));

        // KOT → HOM (Kottawa → Homagama)
        roadSegments.put("KOT_HOM", List.of(
                new double[]{6.8410, 79.9600},
                new double[]{6.8420, 79.9700},
                new double[]{6.8430, 79.9840},
                new double[]{6.8440, 79.9980}
        ));

        // ==================== KANDY ROAD CORRIDOR ====================

        // BOR → RAJ (Borella → Rajagiriya)
        roadSegments.put("BOR_RAJ", List.of(
                new double[]{6.9180, 79.8770},
                new double[]{6.9150, 79.8840},
                new double[]{6.9110, 79.8900},
                new double[]{6.9060, 79.8960}
        ));

        // PET → RAJ (Pettah → Rajagiriya)
        roadSegments.put("PET_RAJ", List.of(
                new double[]{6.9387, 79.8550},
                new double[]{6.9310, 79.8680},
                new double[]{6.9200, 79.8810},
                new double[]{6.9060, 79.8960}
        ));

        // RAJ → BAT (Rajagiriya → Battaramulla)
        roadSegments.put("RAJ_BAT", List.of(
                new double[]{6.9060, 79.8960},
                new double[]{6.9040, 79.9030},
                new double[]{6.9015, 79.9100},
                new double[]{6.8990, 79.9170}
        ));

        // BAT → MAL (Battaramulla → Malabe)
        roadSegments.put("BAT_MAL", List.of(
                new double[]{6.8990, 79.9170},
                new double[]{6.9000, 79.9280},
                new double[]{6.9020, 79.9400},
                new double[]{6.9050, 79.9530}
        ));

        // MAL → KAD (Malabe → Kaduwela)
        roadSegments.put("MAL_KAD", List.of(
                new double[]{6.9050, 79.9530},
                new double[]{6.9120, 79.9620},
                new double[]{6.9220, 79.9730},
                new double[]{6.9320, 79.9840}
        ));

        // BAT → NUG (Battaramulla → Nugegoda)
        roadSegments.put("BAT_NUG", List.of(
                new double[]{6.8990, 79.9170},
                new double[]{6.8920, 79.9080},
                new double[]{6.8820, 79.8990},
                new double[]{6.8722, 79.8892}
        ));

        // ==================== KELANIYA / KIRIBATHGODA ====================

        // PET → KEL (Pettah → Kelaniya)
        roadSegments.put("PET_KEL", List.of(
                new double[]{6.9387, 79.8550},
                new double[]{6.9420, 79.8720},
                new double[]{6.9480, 79.8950},
                new double[]{6.9560, 79.9220}
        ));

        // KEL → KIR (Kelaniya → Kiribathgoda)
        roadSegments.put("KEL_KIR", List.of(
                new double[]{6.9560, 79.9220},
                new double[]{6.9620, 79.9240},
                new double[]{6.9700, 79.9260},
                new double[]{6.9780, 79.9280}
        ));

        // KEL → KDW (Kelaniya → Kadawatha)
        roadSegments.put("KEL_KDW", List.of(
                new double[]{6.9560, 79.9220},
                new double[]{6.9620, 79.9320},
                new double[]{6.9710, 79.9430},
                new double[]{6.9800, 79.9530}
        ));

        // KDW → KIR (Kadawatha → Kiribathgoda)
        roadSegments.put("KDW_KIR", List.of(
                new double[]{6.9800, 79.9530},
                new double[]{6.9800, 79.9440},
                new double[]{6.9790, 79.9360},
                new double[]{6.9780, 79.9280}
        ));

        // ==================== PILIYANDALA CORRIDOR ====================

        // MAH → PIL (Maharagama → Piliyandala)
        roadSegments.put("MAH_PIL", List.of(
                new double[]{6.8470, 79.9270},
                new double[]{6.8340, 79.9260},
                new double[]{6.8170, 79.9245},
                new double[]{6.8010, 79.9220}
        ));

        // PIL → HOR (Piliyandala → Horana)
        roadSegments.put("PIL_HOR", List.of(
                new double[]{6.8010, 79.9220},
                new double[]{6.7780, 79.9520},
                new double[]{6.7470, 79.9920},
                new double[]{6.7160, 80.0620}
        ));

        // RAT → PIL (Ratmalana → Piliyandala)
        roadSegments.put("RAT_PIL", List.of(
                new double[]{6.8220, 79.8760},
                new double[]{6.8180, 79.8900},
                new double[]{6.8100, 79.9060},
                new double[]{6.8010, 79.9220}
        ));

        // ==================== CROSS ROUTES ====================

        // NUG → RAJ (Nugegoda → Rajagiriya)
        roadSegments.put("NUG_RAJ", List.of(
                new double[]{6.8722, 79.8892},
                new double[]{6.8830, 79.8910},
                new double[]{6.8950, 79.8935},
                new double[]{6.9060, 79.8960}
        ));

        // MAH → MAL (Maharagama → Malabe)
        roadSegments.put("MAH_MAL", List.of(
                new double[]{6.8470, 79.9270},
                new double[]{6.8600, 79.9350},
                new double[]{6.8820, 79.9440},
                new double[]{6.9050, 79.9530}
        ));

        // KAD → KDW (Kaduwela → Kadawatha)
        roadSegments.put("KAD_KDW", List.of(
                new double[]{6.9320, 79.9840},
                new double[]{6.9450, 79.9760},
                new double[]{6.9620, 79.9650},
                new double[]{6.9800, 79.9530}
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