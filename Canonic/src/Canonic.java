import java.util.Scanner;
import java.util.Stack;
class Edge {
    int to;
    String str;

    Edge(int to, String str) {
        this.to = to;
        this.str = str;
    }
}

class Graph{
    public static void depthFirstSearch(int vertex, int[] count, Edge[][] graph, boolean[] visited, int[] num, int[] anum) {
        Stack<Integer> stack = new Stack<>();
        stack.push(vertex);
        visited[vertex] = true;
        num[count[0]] = vertex;
        anum[vertex] = count[0];
        count[0]++;

        while (!stack.isEmpty()) {
            int currVertex = stack.peek();
            boolean foundUnvisited = false;
            for (int i = 0; i < graph[currVertex].length; i++) {
                int to = graph[currVertex][i].to;
                if (!visited[to]) {
                    stack.push(to);
                    visited[to] = true;
                    num[count[0]] = to;
                    anum[to] = count[0];
                    count[0]++;
                    foundUnvisited = true;
                    break;
                }
            }
            if (!foundUnvisited) {
                stack.pop();
            }
        }
    }
    public static Edge[][] graphInitial(int numberOfVertices, int numberOfEdges, Scanner scanner){
        Edge[][] graph = new Edge[numberOfVertices][numberOfEdges];
        try {
            for (int i = 0; i < numberOfVertices; i++) {
                for (int j = 0; j < numberOfEdges; j++) {
                    graph[i][j] = new Edge(scanner.nextInt(), "");
                }
            }
            for (int i = 0; i < numberOfVertices; i++) {
                for (int j = 0; j < numberOfEdges; j++) {
                    graph[i][j].str = scanner.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }
}

public class Canonic {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            int numberOfVertices = scanner.nextInt();
            int numberOfEdges = scanner.nextInt();
            int startingVertex = scanner.nextInt();
            Edge[][] graph = Graph.graphInitial(numberOfVertices, numberOfEdges, scanner);

            int[] num = new int[numberOfVertices];
            int[] anum = new int[numberOfVertices];
            int[] count = {0};
            boolean[] visited = new boolean[numberOfVertices];
            Graph.depthFirstSearch(startingVertex, count, graph, visited, num, anum);

            numberOfVertices = count[0];
            System.out.println(numberOfVertices);
            System.out.println(numberOfEdges);
            System.out.println("0");
            for (int i = 0; i < numberOfVertices; i++) {
                for (int j = 0; j < numberOfEdges; j++) {
                    System.out.print(anum[graph[num[i]][j].to] + " ");
                }
                System.out.println();
            }
            for (int i = 0; i < numberOfVertices; i++) {
                for (int j = 0; j < numberOfEdges; j++) {
                    System.out.print(graph[num[i]][j].str + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}