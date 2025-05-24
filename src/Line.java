import java.util.*;

public class Line {
    private String name;
    private Map<Station, Map<Station, Integer>> distances;

    public Line(String name) {
        this.name = name;
        this.distances = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addStation(Station station1, Station station2, int distance) {
        distances.computeIfAbsent(station1, k -> new HashMap<>()).put(station2, distance);
        distances.computeIfAbsent(station2, k -> new HashMap<>()).put(station1, distance);
    }

    public Map<Station, Map<Station, Integer>> getDistances() {
        return distances;
    }

    public int getStationCount() {
        Set<Station> stations = new HashSet<>();
        for (Map.Entry<Station, Map<Station, Integer>> entry : distances.entrySet()) {
            stations.add(entry.getKey());
            stations.addAll(entry.getValue().keySet());
        }
        return stations.size();
    }
}
