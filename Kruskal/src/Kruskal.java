import java.util.*;

public class Kruskal{

    static class Edge implements Comparable<Edge>{
        int source, destination;
        double weight;

        public Edge(int source, int destination, double weight){
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public int compareTo(Edge otherEdge){
            return Double.compare(weight, otherEdge.weight);
        }
    }

    static class Subset{
        int parent, rank;
    }

    static int find(Subset[] subsets, int i){
        if(subsets[i].parent != i){
            subsets[i].parent = find(subsets, subsets[i].parent);
        }
        return subsets[i].parent;
    }

    static void union(Subset[] subsets, int x, int y){
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        if(subsets[xroot].rank < subsets[yroot].rank){
            subsets[xroot].parent = yroot;
        }
        else if(subsets[xroot].rank > subsets[yroot].rank){
            subsets[yroot].parent = xroot;
        }
        else{
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    static double kruskalMST(Edge[] edges, int V){
        double weight = 0.0;
        int e = 0;

        Subset[] subsets = new Subset[V];
        for(int i = 0; i < V; i++){
            subsets[i] = new Subset();
            subsets[i].parent = i;
            subsets[i].rank = 0;
        }

        Arrays.sort(edges);

        int i = 0;
        while(e < V - 1){
            Edge nextEdge = edges[i++];
            int x = find(subsets, nextEdge.source);
            int y = find(subsets, nextEdge.destination);

            if(x != y){
                e++;
                weight += nextEdge.weight;
                union(subsets, x, y);
            }
        }
        return weight;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for(int i=0;i<n;i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }

        Edge[] edges = new Edge[n*(n-1)/2];
        int k=0;
        for(int i=0;i<n;i++) {
            for(int j=i+1;j<n;j++) {
                double weight = Math.sqrt((x[i]-x[j])*(x[i]-x[j]) + (y[i]-y[j])*(y[i]-y[j]));
                edges[k++] = new Edge(i, j, weight);
            }
        }
        System.out.printf("%.2f", kruskalMST(edges, n));
    }
}