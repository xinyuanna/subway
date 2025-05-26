import java.util.*;

public class SubwaySystem {
    private SubwayMap subwayMap;

    public SubwaySystem(SubwayMap subwayMap) {
        this.subwayMap = subwayMap;
    }

    // 1. 识别所有地铁中转站
    public Set<Station> getTransferStations() {
        return subwayMap.getTransferStations();
    }

    // 2. 输入某一站点，输出线路距离小于 n 的所有站点集合
    public Set<Map<String, Object>> getNearbyStations(String stationName, int n) {
        Set<Map<String, Object>> nearbyStations = new HashSet<>();
        Station startStation = subwayMap.getStations().get(stationName);
        if (startStation == null) {
            throw new IllegalArgumentException("站点名称不存在");
        }

        Queue<Map<String, Object>> queue = new LinkedList<>();
        Set<Station> visited = new HashSet<>();
        visited.add(startStation);

        for (String lineName : startStation.getLines()) {
            Line line = subwayMap.getLines().get(lineName);
            Map<Station, Integer> neighbors = line.getDistances().get(startStation);
            if (neighbors != null) {
                for (Map.Entry<Station, Integer> entry : neighbors.entrySet()) {
                    Station neighbor = entry.getKey();
                    int distance = entry.getValue();
                    if (distance < n) {
                        Map<String, Object> info = new HashMap<>();
                        info.put("station", neighbor.getName());
                        info.put("line", lineName);
                        info.put("distance", distance);
                        nearbyStations.add(info);
                        if (!visited.contains(neighbor)) {
                            queue.offer(info);
                            visited.add(neighbor);
                        }
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            Map<String, Object> currentInfo = queue.poll();
            Station currentStation = subwayMap.getStations().get((String) currentInfo.get("station"));
            int currentDistance = (int) currentInfo.get("distance");
            for (String lineName : currentStation.getLines()) {
                Line line = subwayMap.getLines().get(lineName);
                Map<Station, Integer> neighbors = line.getDistances().get(currentStation);
                if (neighbors != null) {
                    for (Map.Entry<Station, Integer> entry : neighbors.entrySet()) {
                        Station neighbor = entry.getKey();
                        int newDistance = currentDistance + entry.getValue();
                        if (newDistance < n && !visited.contains(neighbor)) {
                            Map<String, Object> info = new HashMap<>();
                            info.put("station", neighbor.getName());
                            info.put("line", lineName);
                            info.put("distance", newDistance);
                            nearbyStations.add(info);
                            queue.offer(info);
                            visited.add(neighbor);
                        }
                    }
                }
            }
        }

        return nearbyStations;
    }

    // 3. 输入起点站和终点站的名称，返回所有路径的集合
    public Set<List<Station>> getAllPaths(String startName, String endName) {
        Set<List<Station>> paths = new HashSet<>();
        Station start = subwayMap.getStations().get(startName);
        Station end = subwayMap.getStations().get(endName);
        if (start == null || end == null) {
            throw new IllegalArgumentException("站点名称不存在");
        }

        List<Station> path = new ArrayList<>();
        path.add(start);
        dfs(start, end, path, paths);
        return paths;
    }

    private void dfs(Station current, Station end, List<Station> path, Set<List<Station>> paths) {
        if (current.equals(end)) {
            paths.add(new ArrayList<>(path));
            return;
        }

        for (String lineName : current.getLines()) {
            Line line = subwayMap.getLines().get(lineName);
            Map<Station, Integer> neighbors = line.getDistances().get(current);
            if (neighbors != null) {
                for (Station neighbor : neighbors.keySet()) {
                    if (!path.contains(neighbor)) {
                        path.add(neighbor);
                        dfs(neighbor, end, path, paths);
                        path.remove(path.size() - 1);
                    }
                }
            }
        }
    }

    // 4. 给定起点站和终点站的名称，返回最短路径
    public List<Station> getShortestPath(String startName, String endName) {
        Station start = subwayMap.getStations().get(startName);
        Station end = subwayMap.getStations().get(endName);
        if (start == null || end == null) {
            throw new IllegalArgumentException("站点名称不存在");
        }

        Map<Station, Integer> distance = new HashMap<>();
        Map<Station, Station> prev = new HashMap<>();
        PriorityQueue<Station> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));

        for (Station station : subwayMap.getStations().values()) {
            distance.put(station, Integer.MAX_VALUE);
        }
        distance.put(start, 0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            Station current = queue.poll();
            if (current.equals(end)) {
                break;
            }

            for (String lineName : current.getLines()) {
                Line line = subwayMap.getLines().get(lineName);
                Map<Station, Integer> neighbors = line.getDistances().get(current);
                if (neighbors != null) {
                    for (Map.Entry<Station, Integer> entry : neighbors.entrySet()) {
                        Station neighbor = entry.getKey();
                        int newDistance = distance.get(current) + entry.getValue();
                        if (newDistance < distance.get(neighbor)) {
                            distance.put(neighbor, newDistance);
                            prev.put(neighbor, current);
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }

        List<Station> path = new ArrayList<>();
        for (Station at = end; at != null; at = prev.get(at)) {
            path.add(0, at);
        }
        return path;
    }

    // 5. 打印路径
    public void printPath(List<Station> path) {
        if (path.isEmpty()) {
            System.out.println("没有找到路径");
            return;
        }

        String currentLine = null;
        Station start = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            Station current = path.get(i);
            Set<String> commonLines = new HashSet<>(start.getLines());
            commonLines.retainAll(current.getLines());
            if (currentLine == null) {
                currentLine = commonLines.iterator().next();
            } else if (!commonLines.contains(currentLine)) {
                System.out.println("先坐" + currentLine + "号线从" + start.getName() + "站到" + path.get(i - 1).getName() + "站");
                currentLine = commonLines.iterator().next();
                start = path.get(i - 1);
            }
        }
        System.out.println("再坐" + currentLine + "号线从" + start.getName() + "站到" + path.get(path.size() - 1).getName() + "站");
    }

    // 6. 计算普通单程票费用
    public double calculateSingleTicketPrice(List<Station> path) {
        int totalDistance = 0;
        for (int i = 1; i < path.size(); i++) {
            Station prev = path.get(i - 1);
            Station current = path.get(i);
            for (String lineName : prev.getLines()) {
                Line line = subwayMap.getLines().get(lineName);
                Map<Station, Integer> neighbors = line.getDistances().get(prev);
                if (neighbors != null && neighbors.containsKey(current)) {
                    totalDistance += neighbors.get(current);
                    break;
                }
            }
        }

        return calculatePrice(totalDistance);
    }

    private double calculatePrice(int distance) {
        if (distance <= 9) {
            return 2;
        } else if (distance <= 14) {
            return 3;
        } else {
            int extra = (distance - 14) / 5;
            if ((distance - 14) % 5 != 0) {
                extra++;
            }
            return 3 + extra;
        }
    }

    // 7. 计算使用武汉通和日票的乘客的票价
    public Map<String, Double> calculateOtherTicketPrices(List<Station> path) {
        Map<String, Double> prices = new HashMap<>();
        double singlePrice = calculateSingleTicketPrice(path);
        prices.put("武汉通", singlePrice * 0.9);
        prices.put("1日票", 0.0);
        prices.put("3日票", 0.0);
        prices.put("7日票", 0.0);
        return prices;
    }
}
