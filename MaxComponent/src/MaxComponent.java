import java.util.*;

public class MaxComponent {
    static int maxVertices = -1; // количество вершин в наибольшей компоненте связности
    static List<Integer>[] adjList; // список инцидентности
    static boolean[] visited; // массив, отмечающий посещенные вершины
    static List<Integer> maxComponent; // список вершин наибольшей компоненты связности

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        adjList = new List[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            adjList[u].add(v);
            adjList[v].add(u);
        }

        visited = new boolean[n];
        maxComponent = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<Integer> component = new ArrayList<>();
                dfs(i, component); // запустить обход из вершины i
                if (component.size() > maxVertices || (component.size() == maxVertices && edges(component) > edges(maxComponent))) {
                    maxVertices = component.size();
                    maxComponent = component;
                }
            }
        }

        // вывод графа в формате DOT
        System.out.println("graph G {");
        for (int i = 0; i < n; i++) {
            System.out.print("  " + i);
            if (maxComponent.contains(i)) { // назначить красный цвет вершинам наибольшей компоненты связности
                System.out.print(" [color=red]");
            }
            System.out.println(";");
        }
        Set<String> drawnEdges = new HashSet<>(); // множество уже нарисованных ребер
        for (int u = 0; u < n; u++) {
            for (int v : adjList[u]) {
                if (u <= v && !drawnEdges.contains(u + "," + v)) { // проверка, не было ли уже нарисовано это ребро
                    drawnEdges.add(u + "," + v);
                    drawnEdges.add(v + "," + u);
                    if (maxComponent.contains(u) && maxComponent.contains(v)) {
                        System.out.println("  " + u + " -- " + v + " [color=red];");
                    } else {
                        System.out.println("  " + u + " -- " + v + ";");
                    }
                }
            }
        }
        System.out.println("}");
    }

    // обход в глубину из вершины v, добавление посещенных вершин в список component
    static void dfs(int v, List<Integer> component) {
        visited[v] = true;
        component.add(v); // новая вершина добавляется только если была посещена впервые
        for (int u : adjList[v]) {
            if (!visited[u]) {
                dfs(u, component);
            }
        }
    }

    // вычисление количества ребер в списке вершин
    static int edges(List<Integer> vertices) {
        int count = 0;
        for (int u : vertices) {
            for (int v : adjList[u]) {
                if (vertices.contains(v)) {
                    count++;
                }
            }
        }
        return count / 2; // каждое ребро учитывается дважды
    }
}