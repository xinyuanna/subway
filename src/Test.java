import java.util.*;

public class Test {
    public static void main(String[] args) {
        SubwayMap subwayMap = new SubwayMap("src/subway.txt");
        SubwaySystem subwaySystem = new SubwaySystem(subwayMap);
        Scanner scanner = new Scanner(System.in);

        // 1. 识别所有地铁中转站
        Set<Station> transferStations = subwaySystem.getTransferStations();
        System.out.println("所有地铁中转站：");
        for (Station station : transferStations) {
            System.out.println(station);
        }

        // 2. 输入某一站点，输出线路距离小于 n 的所有站点集合
        try {
            System.out.print("请输入要查询的站点名称：");
            String targetStation = scanner.nextLine();
            System.out.print("请输入距离限制 n：");
            int n = scanner.nextInt();
            scanner.nextLine(); // 消耗 nextInt() 后的换行符
            Set<Map<String, Object>> nearbyStations = subwaySystem.getNearbyStations(targetStation, n);
            System.out.println("\n" + targetStation + "，距离小于 " + n + " 的站点：");
            for (Map<String, Object> info : nearbyStations) {
                System.out.println("<" + info.get("station") + "，" + info.get("line") + "号线，" + info.get("distance") + ">");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // 3. 输入起点站和终点站的名称，返回所有路径的集合
        try {
            System.out.print("请输入起点站名称：");
            String startStation = scanner.nextLine();
            System.out.print("请输入终点站名称：");
            String endStation = scanner.nextLine();
            Set<List<Station>> allPaths = subwaySystem.getAllPaths(startStation, endStation);
            System.out.println("\n" + startStation + " 到 " + endStation + " 的所有路径：");
            for (List<Station> path : allPaths) {
                for (Station station : path) {
                    System.out.print(station.getName() + " ");
                }
                System.out.println();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // 4. 给定起点站和终点站的名称，返回最短路径
        try {
            // 提示用户输入起始站和终点站
            System.out.print("请输入最短路径查询的起点站名称：");
            String start = scanner.nextLine();
            System.out.print("请输入最短路径查询的终点站名称：");
            String end = scanner.nextLine();

            List<Station> shortestPath = subwaySystem.getShortestPath(start, end);
            System.out.println("\n" + start + " 到 " + end + " 的最短路径：");
            for (Station station : shortestPath) {
                System.out.print(station.getName() + " ");
            }
            System.out.println();

            // 5. 打印路径
            System.out.println("\n最短路径打印：");
            subwaySystem.printPath(shortestPath);

            // 6. 计算普通单程票费用
            double singlePrice = subwaySystem.calculateSingleTicketPrice(shortestPath);
            System.out.println("\n普通单程票费用：" + singlePrice + "元");

            // 7. 计算使用武汉通和日票的乘客的票价
            Map<String, Double> otherPrices = subwaySystem.calculateOtherTicketPrices(shortestPath);
            System.out.println("\n使用武汉通的票价：" + otherPrices.get("武汉通") + "元");
            System.out.println("使用1日票的票价：" + otherPrices.get("1日票") + "元");
            System.out.println("使用3日票的票价：" + otherPrices.get("3日票") + "元");
            System.out.println("使用7日票的票价：" + otherPrices.get("7日票") + "元");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}