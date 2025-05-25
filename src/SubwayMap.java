import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SubwayMap {
    private Map<String, Line> lines;
    private Map<String, Station> stations;

    public SubwayMap(String filePath) {
        this.lines = new HashMap<>();
        this.stations = new HashMap<>();
        readFile(filePath);
    }

    private void readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String lineName = parts[0];
                    String station1Name = parts[1];
                    String station2Name = parts[2];
                    // 将 Integer.parseInt 替换为 Double.parseDouble 并四舍五入为整数
                    int distance = (int) Math.round(Double.parseDouble(parts[3]));

                    Line lineObj = lines.computeIfAbsent(lineName, Line::new);
                    Station station1 = stations.computeIfAbsent(station1Name, Station::new);
                    Station station2 = stations.computeIfAbsent(station2Name, Station::new);

                    station1.addLine(lineName);
                    station2.addLine(lineName);
                    lineObj.addStation(station1, station2, distance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Line> getLines() {
        return lines;
    }

    public Map<String, Station> getStations() {
        return stations;
    }

    public Set<Station> getTransferStations() {
        Set<Station> transferStations = new HashSet<>();
        for (Station station : stations.values()) {
            if (station.getLines().size() >= 2) {
                transferStations.add(station);
            }
        }
        return transferStations;
    }
}