import java.util.*;

class Position implements Comparable<Position>{
    int i, level;
    Position previous;
    Position[] move;
    String[] possibleMoves;
    boolean marked;
    public Position(int i, Position parent, int depth, boolean marked, int m) {
        this.i = i;
        this.previous = parent;
        this.level = depth;
        this.marked = marked;
        this.move = new Position[m];
        this.possibleMoves = new String[m];
    }
    @Override
    public int compareTo(Position other) {
        return Integer.compare(this.i, other.i);
    }
}

class Graph{
    private static int num = 0;
    public static void numSet(int num){
        Graph.num = num;
    }
    public static Position[] mergeSort(Position[] automat, int n, int m) {
        Position[] pi = new Position[n];
        int m1 = Split1(automat, pi, n, m);

        while (true) {
            int m2 = Split(automat, pi, n, m);
            if (m1 == m2) {
                break;
            }
            m1 = m2;
        }

        ArrayList<Position> ans = new ArrayList<>();

        for (Position q : automat) {
            Position qt = pi[q.i];
            if (!searchElementInArray(ans, qt)) {
                ans.add(qt);
                for (int i = 0; i < m; i++) {
                    qt.move[i] = pi[q.move[i].i];
                    qt.possibleMoves[i] = q.possibleMoves[i];
                }
            }
        }

        return ans.toArray(new Position[0]);
    }

    private static int Split1(Position[] automat, Position[] pi, int n, int m) {
        for (Position q : automat) {
            q.previous = q;
            q.level = 0;
        }

        int mt = n;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (searchElement(automat[i]) != searchElement(automat[j])) {
                    boolean eq = true;
                    for (int c = 0; c < m; c++) {
                        if (!automat[i].possibleMoves[c].equals(automat[j].possibleMoves[c])) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        Union(automat[i], automat[j]);
                        mt--;
                    }
                }
            }
        }

        for (Position q : automat) {
            pi[q.i] = searchElement(q);
        }
        return mt;
    }

    private static int Split(Position[] automat, Position[] pi, int n, int m) {
        for (Position q : automat) {
            q.previous = q;
            q.level = 0;
        }

        int mt = n;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (pi[automat[i].i] == pi[automat[j].i] && searchElement(automat[i]) != searchElement(automat[j])) {
                    boolean eq = true;
                    for (int c = 0; c < m; c++) {
                        int w1 = automat[i].move[c].i;
                        int w2 = automat[j].move[c].i;
                        if (pi[w1] != pi[w2]) {
                            eq = false;
                            break;
                        }
                    }
                    if (eq) {
                        Union(automat[i], automat[j]);
                        mt--;
                    }
                }
            }
        }

        for (Position q : automat) {
            pi[q.i] = searchElement(q);
        }
        return mt;
    }

    private static void Union(Position x, Position y) {
        Position root_x = searchElement(x);
        Position root_y = searchElement(y);
        if (root_x.level < root_y.level) {
            root_x.previous = root_y;
        } else {
            root_y.previous = root_x;
            if (root_x.level == root_y.level && root_x != root_y) {
                root_x.level++;
            }
        }
    }

    private static Position searchElement(Position x) {
        if (x.previous == x) {
            return x;
        } else {
            x.previous = searchElement(x.previous);
            return searchElement(x.previous);
        }
    }

    public static void DFS(Position s, int m) {
        s.i = num;
        num++;
        s.marked = true;

        for (int i = 0; i < m; i++) {
            if (!s.move[i].marked) {
                DFS(s.move[i], m);
            }
        }
    }

    private static boolean searchElementInArray(ArrayList<Position> automat, Position q) {
        for (Position s : automat) {
            if (s == q) {
                return true;
            }
        }
        return false;
    }
    public static boolean isEqual(Position[]  first, int m_1, Position[] second, int m_2) {
        if (first.length != second.length || m_1 != m_2) {
            return false;
        } else {
            for (int i = 0; i < first.length; i++) {
                for (int j = 0; j < m_1; j++) {
                    if (first[i].move[j].i != second[i].move[j].i ||
                            !first[i].possibleMoves[j].equals(second[i].possibleMoves[j])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}

public class EqMealy {
    public static void main(String[] args) {
        int n_1, m_1, k_1, n_2, m_2, k_2;
        Scanner scanner = new Scanner(System.in);
        n_1 = scanner.nextInt();
        m_1 = scanner.nextInt();
        k_1 = scanner.nextInt();

        Position[] furst_automat = new Position[n_1];

        for (int i = 0; i < n_1; i++) {
            furst_automat[i] = new Position(i, null, 0, false, m_1);
        }

        for (int i = 0; i < n_1; i++) {
            for (int j = 0; j < m_1; j++) {
                int t = scanner.nextInt();
                furst_automat[i].move[j] = furst_automat[t];
            }
        }

        for (int i = 0; i < n_1; i++) {
            for (int j = 0; j < m_1; j++) {
                String s = scanner.next();
                furst_automat[i].possibleMoves[j] = s;
            }
        }

        Position[] min_first_automat = Graph.mergeSort(furst_automat, n_1, m_1);

        for (Position q : min_first_automat) {
            boolean fl = true;
            for (int i = 0; i < m_1; i++) {
                if (!q.possibleMoves[i].equals(furst_automat[k_1].possibleMoves[i])) {
                    fl = false;
                    break;
                }
            }
            if (fl) {
                Graph.DFS(q, m_1);
                break;
            }
        }

        Graph.numSet(0);
        n_2 = scanner.nextInt();
        m_2 = scanner.nextInt();
        k_2 = scanner.nextInt();

        Position[] second_automat = new Position[n_2];

        for (int i = 0; i < n_2; i++) {
            second_automat[i] = new Position(i, null, 0, false, m_2);
        }

        for (int i = 0; i < n_2; i++) {
            for (int j = 0; j < m_2; j++) {
                int t = scanner.nextInt();
                second_automat[i].move[j] = second_automat[t];
            }
        }

        for (int i = 0; i < n_2; i++) {
            for (int j = 0; j < m_2; j++) {
                String s = scanner.next();
                second_automat[i].possibleMoves[j] = s;
            }
        }

        Position[] min_second_automat = Graph.mergeSort(second_automat, n_2, m_2);

        for (Position q : min_second_automat) {
            boolean fl = true;
            for (int i = 0; i < m_2; i++) {
                if (!q.possibleMoves[i].equals(second_automat[k_2].possibleMoves[i])) {
                    fl = false;
                    break;
                }
            }
            if (fl) {
                Graph.DFS(q, m_2);
                break;
            }
        }
        Arrays.sort(min_first_automat);
        Arrays.sort(min_second_automat);
        if(Graph.isEqual(min_first_automat, m_1, min_second_automat, m_2)){
            System.out.println("EQUAL");
        } else {
            System.out.println("NOT EQUAL");
        }
/*
        System.out.println("////////////////////////////");
        System.out.println("digraph {");
        System.out.println("  rankdir = LR");
        for (Position q : min_first_automat) {
            for (int i = 0; i < m_1; i++) {
                System.out.printf("  %d -> %d [label = \"%s(%s)\"]%n", q.i, q.move[i].i, (char) (i + 97), q.possibleMoves[i]);
            }
        }
        System.out.println("}");

        System.out.println("////////////////////////////");
        System.out.println("digraph {");
        System.out.println("  rankdir = LR");
        for (Position q : min_second_automat) {
            for (int i = 0; i < m_2; i++) {
                System.out.printf("  %d -> %d [label = \"%s(%s)\"]%n", q.i, q.move[i].i, (char) (i + 97), q.possibleMoves[i]);
            }
        }
        System.out.println("}");
        */

    }
}