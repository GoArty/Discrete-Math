import java.util.*;

public class Main {
    // структура для хранения информации о вершинах графа
    static class Vertex {
        int index; // индекс вершины
        int lowlink; // lowlink вершины (используется при поиске компонент сильной связности)
        boolean onStack; // находится ли вершина в стеке (используется при поиске компонент сильной связности)
        int group; // номер компоненты сильной связности, к которой относится вершина
        List<Integer> adj; // список смежных вершин
        Vertex(int index) {
            this.index = index;
            this.lowlink = index;
            this.onStack = false;
            this.group = -1;
            this.adj = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // количество команд
        List<Vertex> graph = new ArrayList<>(); // граф
        for (int i = 0; i < n; i++) {
            Vertex vertex = new Vertex(i);
            graph.add(vertex);
        }

        // чтение команд и формирование графа
        for (int i = 0; i < n; i++) {
            int label = scanner.nextInt(); // метка
            String command = scanner.next(); // команда
            Vertex vertex = graph.get(i);
            if (command.equals("ACTION")) {
                vertex.adj.add(i + 1);
            } else if (command.equals("JUMP")) {
                int target = scanner.nextInt(); // метка целевой команды
                vertex.adj.add(target);
            } else if (command.equals("BRANCH")) {
                int target = scanner.nextInt(); // метка целевой команды
                vertex.adj.add(target);
                vertex.adj.add(i + 1);
            }
        }

        // поиск компонент сильной связности
        Stack<Vertex> stack = new Stack<>();
        int time = 0;
        for (Vertex vertex : graph) {
            if (vertex.group == -1) {
                strongConnect(vertex, graph, stack, time);
            }
        }

        // поиск натуральных циклов в компонентах сильной связности
        int count = 0;
        for (int i = 0; i < graph.size(); i++) {
            Vertex vertex = graph.get(i);
            for (int j : vertex.adj) {
                if (vertex.group == graph.get(j).group && vertex.index <= graph.get(j).index) {
                    count++;
                    break;
                }
            }
        }

        System.out.println(count);
    }

    // рекурсивная функция для поиска компонент сильной связности
    private static void strongConnect(Vertex vertex, List<Vertex> graph, Stack<Vertex> stack, int time) {
        vertex.lowlink = vertex.index;
        vertex.onStack = true;
        stack.push(vertex);
        time++;
        for (int w : vertex.adj) {
            Vertex next = graph.get(w);
            if (next.group == -1) {
                strongConnect(next, graph, stack, time);
                vertex.lowlink = Math.min(vertex.lowlink, next.lowlink);
            } else if (next.onStack) {
                vertex.lowlink = Math.min(vertex.lowlink, next.index);
            }
        }
        if (vertex.lowlink == vertex.index) {
            Vertex w;
            do {
                w = stack.pop();
                w.onStack = false;
                w.group = vertex.index;
            } while (w != vertex);
        }
    }
}