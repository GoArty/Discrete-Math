import java.util.*;
import java.io.*;

public class Cpm {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String line;
        Map<String, Integer> duration = new HashMap<>();
        Map<String, Set<String>> dependsOn = new HashMap<>();
        Set<String> allTasks = new HashSet<>();

        // read input and extract task durations and dependencies
        while (input.hasNext()) {
            line = input.nextLine().replaceAll("\\s+", "");
            String[] tasks = line.split("<");
            int i, n = tasks.length;
            for (i = 0; i < n - 1; i++) {
                String t1 = tasks[i], t2 = tasks[i + 1];
                if (!dependsOn.containsKey(t2)) dependsOn.put(t2, new HashSet<>());
                dependsOn.get(t2).add(t1);
                allTasks.add(t1);
                allTasks.add(t2);
            }
            String[] parts = tasks[0].split("\\(");
            duration.put(parts[0], Integer.valueOf(parts[1].substring(0, parts[1].length() - 1)));
            allTasks.add(parts[0]);
        }

        // topological sort
        Map<String, Integer> inDegree = new HashMap<>();
        for (String task : allTasks) {
            inDegree.put(task, 0);
        }
        for (String task : dependsOn.keySet()) {
            for (String dependant : dependsOn.get(task)) {
                inDegree.put(task, inDegree.get(task) + 1);
            }
        }
        List<String> topoOrder = new ArrayList<>();
        Queue<String> q = new LinkedList<>();
        for (String task : inDegree.keySet()) {
            if (inDegree.get(task) == 0) q.offer(task);
        }
        while (!q.isEmpty()) {
            String task = q.poll();
            topoOrder.add(task);
            if (dependsOn.containsKey(task)) {
                for (String dependant : dependsOn.get(task)) {
                    inDegree.put(dependant, inDegree.get(dependant) - 1);
                    if (inDegree.get(dependant) == 0) q.offer(dependant);
                }
            }
        }

        // calculate earliest completion times
        Map<String, Integer> earliest = new HashMap<>();
        for (String task : allTasks) {
            earliest.put(task, 0);
        }
        for (String task : topoOrder) {
            int t = duration.getOrDefault(task, 0), max = 0;
            if (dependsOn.containsKey(task)) {
                for (String dependency : dependsOn.get(task)) {
                    max = Math.max(max, earliest.get(dependency));
                }
            }
            earliest.put(task, t + max);
        }

        // calculate latest completion times
        Map<String, Integer> latest = new HashMap<>();
        int last = earliest.get(topoOrder.get(topoOrder.size() - 1));
        for (String task : allTasks) {
            latest.put(task, last);
        }
        for (int i = topoOrder.size() - 1; i >= 0; i--) {
            String task = topoOrder.get(i);
            int t = duration.getOrDefault(task, 0), min = last;
            if (dependsOn.containsKey(task)) {
                for (String dependant : dependsOn.get(task)) {
                    min = Math.min(min, latest.get(dependant) - t);
                }
            }
            latest.put(task, min);
        }

        // identify critical path and print DOT graph
        System.out.println("digraph G {");
        for (String task : allTasks) {
            if (duration.containsKey(task)) {
                int e = earliest.get(task), l = latest.get(task);
                if (e == l) {
                    System.out.println(task + " [style=filled, color=red];");
                } else {
                    System.out.println(task + ";");
                }
            } else {
                System.out.println(task + " [style=filled, color=blue];");
            }
            if (dependsOn.containsKey(task)) {
                for (String dependant : dependsOn.get(task))
                {
                    System.out.println(dependant + " -> " + task + ";");
                }
            }
        }
        System.out.println("}");
    }
}