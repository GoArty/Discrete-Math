import java.util.*;

class Position {
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
}

class Graph{
    private static int num = 0;
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
}

public class MinMealy {
    private static int n, m, k;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        k = scanner.nextInt();

        Position[] automat = new Position[n];

        for (int i = 0; i < n; i++) {
            automat[i] = new Position(i, null, 0, false, m);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int t = scanner.nextInt();
                automat[i].move[j] = automat[t];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                String s = scanner.next();
                automat[i].possibleMoves[j] = s;
            }
        }

        Position[] min_automat = Graph.mergeSort(automat, n, m);

        for (Position q : min_automat) {
            boolean fl = true;
            for (int i = 0; i < m; i++) {
                if (!q.possibleMoves[i].equals(automat[k].possibleMoves[i])) {
                    fl = false;
                    break;
                }
            }
            if (fl) {
                Graph.DFS(q, m);
                break;
            }
        }

        System.out.println("digraph {");
        System.out.println("  rankdir = LR");
        for (Position q : min_automat) {
            for (int i = 0; i < m; i++) {
                System.out.printf("  %d -> %d [label = \"%s(%s)\"]%n", q.i, q.move[i].i, (char) (i + 97), q.possibleMoves[i]);
            }
        }
        System.out.println("}");
    }
}