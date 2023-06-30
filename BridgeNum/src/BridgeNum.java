import java.util.*;

public class BridgeNum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        int[] tin = new int[n];
        int[] low = new int[n];
        boolean[] used = new boolean[n];
        int[] bridgesCount = new int[1];
        int timer = 0;
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                dfs(i, -1, tin, low, used, timer, bridgesCount, graph);
            }
        }
        System.out.println(bridgesCount[0]);
    }

    private static void dfs(int u, int p, int[] tin, int[] low, boolean[] used, int timer, int[] bridgesCount, List<Integer>[] graph) {
        used[u] = true;
        tin[u] = low[u] = timer++;
        for (int v : graph[u]) {
            if (v == p) continue;
            if (used[v]) {
                low[u] = Math.min(low[u], tin[v]);
            } else {
                dfs(v, u, tin, low, used, timer, bridgesCount, graph);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > tin[u]) {
                    bridgesCount[0]++;
                }
            }
        }
    }
}