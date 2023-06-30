import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Graph{
    public static List<Integer> graphInitial(int n, Scanner scanner){
        List<Integer> shades = new ArrayList<>();
        List<List<Integer>> connections = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            shades.add(-1);
            List<Integer> v = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                char c = scanner.next().charAt(0);
                if (c == '+') {
                    v.add(j);
                }
            }
            connections.add(v);
        }
        shades.set(0, 0);
        return Graph.depthFirstSearch(0, shades, connections);
    }
    private static boolean validateShades(int vertex, List<Integer> shades, List<List<Integer>> connections) {
        for (int x : connections.get(vertex)) {
            if (shades.get(x).equals(shades.get(vertex))) {
                return false;
            }
        }
        return true;
    }

    private static List<Integer> depthFirstSearch(int vertex, List<Integer> shades, List<List<Integer>> connections) {
        if (vertex == shades.size()) {
            shades.add(2);
            return shades;
        }
        if (shades.get(vertex) == -1) {
            List<Integer> shades_1 = new ArrayList<>(shades);

            shades.set(vertex, 0);
            if (validateShades(vertex, shades, connections)) {
                for (int x : connections.get(vertex)) {
                    shades.set(x, 1);
                }
                shades = depthFirstSearch(vertex + 1, shades, connections);
            } else {
                shades.add(-2);
            }

            shades_1.set(vertex, 1);
            if (validateShades(vertex, shades_1, connections)) {
                for (int x : connections.get(vertex)) {
                    shades_1.set(x, 0);
                }
                shades_1 = depthFirstSearch(vertex + 1, shades_1, connections);
            } else {
                shades_1.add(-2);
            }

            if (shades.get(shades.size() - 1) == 2 && shades_1.get(shades_1.size() - 1) == 2) {
                int initial = 0, next = 0, initial_1 = 0, next_1 = 0;

                for (int i = 0; i < shades.size() - 1; i++) {
                    if (shades.get(i) == 0) {
                        initial++;
                    } else {
                        next++;
                    }

                    if (shades_1.get(i) == 0) {
                        initial_1++;
                    } else {
                        next_1++;
                    }
                }

                if (Math.abs(initial - next) < Math.abs(initial_1 - next_1)) {
                    return shades;
                } else if (Math.abs(initial - next) == Math.abs(initial_1 - next_1)) {
                    if (initial <= next) {
                        return shades;
                    } else {
                        return shades_1;
                    }
                } else {
                    return shades_1;
                }
            } else if (shades_1.get(shades_1.size() - 1) == 2) {
                return shades_1;
            } else {
                return shades;
            }
        } else {
            if (validateShades(vertex, shades, connections)) {
                for (int x : connections.get(vertex)) {
                    shades.set(x, 1 - shades.get(vertex));
                }
                shades = depthFirstSearch(vertex + 1, shades, connections);
            } else {
                shades.add(-2);
            }

            return shades;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> shades = Graph.graphInitial(n, scanner);
        if (shades.get(shades.size() - 1) == 2) {
            int initial = 0, next = 0;
            for (Integer color : shades) {
                if (color == 0) {
                    initial++;
                } else {
                    next++;
                }
            }

            if (initial <= next) {
                for (int i = 0; i < n; i++) {
                    if (shades.get(i) == 0) {
                        System.out.print(i + 1 + " ");
                    }
                }
            } else {
                for (int i = 0; i < n; i++) {
                    if (shades.get(i) == 1) {
                        System.out.print(i + 1 + " ");
                    }
                }
            }
        } else {
            System.out.print("No solution");
        }
    }
}