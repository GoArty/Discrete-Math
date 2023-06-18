import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Dividers {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long x = scanner.nextLong();

        Set<Long> divisors = new HashSet<>();
        for (long i = 1; i <= Math.sqrt(x); i++) {
            if (x % i == 0) {
                divisors.add(i);
                divisors.add(x / i);
            }
        }

        List<Edge> edges = new ArrayList<>();
        for (long i : divisors) {
            for (long j : divisors) {
                if (i != j && i % j == 0) {
                    boolean addEdge = true;
                    for (long k : divisors) {
                        if (i % k == 0 && k % j == 0 && k != i && k != j) {
                            addEdge = false;
                            break;
                        }
                    }
                    if (addEdge) {
                        edges.add(new Edge(i, j));
                    }
                }
            }
        }

        System.out.println("graph {");
        for (long divisor : divisors) {
            System.out.println(divisor);
        }
        for (Edge edge : edges) {
            System.out.println(edge.u + "--" + edge.v);
        }
        System.out.println("}");
    }

    private static class Edge {
        private final long u;
        private final long v;

        private Edge(long u, long v) {
            this.u = u;
            this.v = v;
        }
    }
}


/*
        System.out.println("digraph DivisorGraph {");
        for (long divisor : divisors) {
            System.out.println(divisor + " [label=\"" + divisor + "\"];");
        }
        for (Edge edge : edges) {
            System.out.println(edge.u + " -> " + edge.v + ";");
        }
        System.out.println("}");


 */