import java.util.*;

public class Prim {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // количество бытовок
        int m = scanner.nextInt(); // количество дорог

        List<List<Edge>> graph = new ArrayList<>(); // граф в виде списка инцидентности
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int len = scanner.nextInt();
            graph.get(u).add(new Edge(v, len));
            graph.get(v).add(new Edge(u, len));
        }

        boolean[] visited = new boolean[n]; // отмечаем посещенные вершины
        PriorityQueue<Edge> pq = new PriorityQueue<>(); // очередь с приоритетом
        pq.add(new Edge(0, 0)); // начинаем с вершины 0

        int totalLength = 0; // общая длина телефонных линий

        while (!pq.isEmpty()) {
            Edge curr = pq.poll();
            int u = curr.to;

            if (visited[u]) {
                continue; // уже посетили эту вершину
            }

            visited[u] = true;
            totalLength += curr.length;

            for (Edge e : graph.get(u)) {
                if (!visited[e.to]) {
                    pq.add(e); // добавляем в очередь ребра, выходящие из текущей вершины
                }
            }
        }

        System.out.println(totalLength); // выводим результат
    }
}

class Edge implements Comparable<Edge> {
    int to;
    int length;

    public Edge(int to, int length) {
        this.to = to;
        this.length = length;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.length, other.length); // сравниваем ребра по длине
    }
}
/*
7
10
0 1 200
1 2 150
0 3 100
1 4 170
1 5 180
2 5 100
3 4 240
3 6 380
4 2 140
3 1 435
 */