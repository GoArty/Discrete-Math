import java.util.*;

public class MapRoute {
    static final int INF = Integer.MAX_VALUE; // бесконечность для дистанций

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] map = new int[n][n];
        int[][] dist = new int[n][n]; // дистанции от начальной точки до каждой точки на карте
        boolean[][] visited = new boolean[n][n]; // посещённые точки
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] = scanner.nextInt();
                dist[i][j] = INF;
            }
        }
        dist[0][0] = map[0][0];
        Queue<int[]> queue = new PriorityQueue<int[]>((a, b) -> a[2] - b[2]); // очередь с приоритетом
        queue.offer(new int[]{0, 0, map[0][0]});
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int i = curr[0], j = curr[1], d = curr[2];
            if (visited[i][j]) continue;
            visited[i][j] = true; // отмечаем точку как посещённую
            if (i == n - 1 && j == n - 1) { // если достигли конечной точки
                System.out.println(dist[n - 1][n - 1]);
                return;
            }
            // обновляем дистанции до соседних точек
            if (i > 0 && dist[i - 1][j] > d + map[i - 1][j]) {
                dist[i - 1][j] = d + map[i - 1][j];
                queue.offer(new int[]{i - 1, j, dist[i - 1][j]});
            }
            if (i < n - 1 && dist[i + 1][j] > d + map[i + 1][j]) {
                dist[i + 1][j] = d + map[i + 1][j];
                queue.offer(new int[]{i + 1, j, dist[i + 1][j]});
            }
            if (j > 0 && dist[i][j - 1] > d + map[i][j - 1]) {
                dist[i][j - 1] = d + map[i][j - 1];
                queue.offer(new int[]{i, j - 1, dist[i][j - 1]});
            }
            if (j < n - 1 && dist[i][j + 1] > d + map[i][j + 1]) {
                dist[i][j + 1] = d + map[i][j + 1];
                queue.offer(new int[]{i, j + 1, dist[i][j + 1]});
            }
        }
    }
}