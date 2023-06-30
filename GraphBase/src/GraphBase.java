import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class Vertex {
    List<Integer> neighbors;
    boolean processed, explored;
    int component, inDegree, minValue;

    Vertex() {
        neighbors = new ArrayList<>();
        processed = explored = false;
        component = inDegree = 0;
        minValue = Integer.MAX_VALUE;
    }
}

class Graph{
    public static void assignComponent(List<Vertex> graph, int v, List<Vertex> cond, int comp) {
        graph.get(v).processed = true;
        graph.get(v).component = comp;
        if (v < cond.get(comp).minValue) {
            cond.get(comp).minValue = v;
        }
        for (int i = 0; i < graph.get(v).neighbors.size(); i++) {
            int to = graph.get(v).neighbors.get(i);
            if (graph.get(to).processed) {
                if (graph.get(to).component != comp) {
                    cond.get(comp).neighbors.add(graph.get(to).component);
                    cond.get(graph.get(to).component).inDegree++;
                }
                continue;
            }
            assignComponent(graph, to, cond, comp);
        }
    }
    public static void printSortBase(List<Vertex> cond) {
        for (int i = 0; i < cond.size(); i++) {
            if (cond.get(i).inDegree == 0) {
                System.out.print(cond.get(i).minValue + " ");
            }
        }
    }
    public static void resetProcessedFlag(List<Vertex> graph) {
        for (int i = 0; i < graph.size(); i++) {
            graph.get(i).processed = false;
        }
    }

    public static void dfs(List<Vertex> graph, int v, List<Integer> order) {
        graph.get(v).processed = true;
        for (int i = 0; i < graph.get(v).neighbors.size(); i++) {
            int to = graph.get(v).neighbors.get(i);
            if (graph.get(to).processed) {
                continue;
            }
            dfs(graph, to, order);
        }
        graph.get(v).explored = true;
        order.add(v);
    }
    public static void createGraph(List<Vertex> graph){
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(), m = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            Vertex ver = new Vertex();
            graph.add(ver);
        }

        for (int i = 0; i < m; i++) {
            int a = scanner.nextInt(), b = scanner.nextInt();
            graph.get(a).neighbors.add(b);
        }
    }
    public static void findBase(List<Vertex> graph, List<Integer> order, List<Vertex> cond){
        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);
            if (!graph.get(v).processed) {
                Vertex ver = new Vertex();
                cond.add(ver);
                Graph.assignComponent(graph, v, cond, cond.size() - 1);
            }
        }
    }
}

public class GraphBase {
    public static void main(String[] args) {
        List<Vertex> graph = new ArrayList<>();

        Graph.createGraph(graph);

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            if (!graph.get(i).processed) {
                Graph.dfs(graph, i, order);
            }
        }

        Graph.resetProcessedFlag(graph);
        List<Vertex> cond = new ArrayList<>();
        Graph.findBase(graph, order, cond);
        Graph.printSortBase(cond);

        System.out.println();
    }
}

/*
22
33
 0  8     1  3     1 10     2 11     2 13     3 14
 4  6     4 16     5 17     6 19     8  1     8  9
 9  0     9  2    10  1    10  4    11 12    12  2
12  4    13  5    13 12    14 15    15  3    15  6
16  4    16  7    17  7    17 18    18  5    19  6
1 2    21 18    2 1
 */