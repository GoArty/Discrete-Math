import java.util.*;

public class EqDist {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // количество вершин
        int m = scanner.nextInt(); // количество ребер

        // создаем список смежности
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            // добавляем ребро (u, v) и (v, u)
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        int k = scanner.nextInt(); // количество опорных вершин
        Set<Integer> pivotal = new HashSet<>(); // множество опорных вершин
        for (int i = 0; i < k; i++) {
            pivotal.add(scanner.nextInt());
        }

        // вычисляем расстояния от опорных вершин до всех остальных вершин в графе
        Map<Integer, Map<Integer, Integer>> distances = new HashMap<>();
        for (int p : pivotal) {
            Map<Integer, Integer> pDistances = bfs(graph, p);
            for (int v : pDistances.keySet()) {
                Map<Integer, Integer> vDistances = distances.getOrDefault(v, new HashMap<>());
                vDistances.put(p, pDistances.get(v));
                distances.put(v, vDistances);
            }
        }

        // ищем вершины, равноудаленные от всех опорных вершин
        List<Integer> result = new ArrayList<>();
        for (int v = 0; v < n; v++) {
            boolean isPivotal = true;
            int distance = -1;
            for (int p : pivotal) {
                if (!distances.containsKey(v) || !distances.get(v).containsKey(p)) {
                    isPivotal = false;
                    break;
                }
                if (distance == -1) {
                    distance = distances.get(v).get(p);
                } else if (distance != distances.get(v).get(p)) {
                    isPivotal = false;
                    break;
                }
            }
            if (isPivotal) {
                result.add(v);
            }
        }

        // выводим результат
        if (result.isEmpty()) {
            System.out.println("-");
        } else {
            Collections.sort(result);
            for (int v : result) {
                System.out.print(v + " ");
            }
        }
    }

    // обход графа в ширину
    private static Map<Integer, Integer> bfs(List<List<Integer>> graph, int start) {
        Map<Integer, Integer> distances = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        distances.put(start, 0);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : graph.get(u)) {
                if (!distances.containsKey(v)) {
                    distances.put(v, distances.get(u) + 1);
                    queue.offer(v);
                }
            }
        }
        return distances;
    }
}

/*
10
10
3 8
0 4
3 7
0 3
5 1
0 2
3 6
5 9
4 1
3 5
2
5 7
 */