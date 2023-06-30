import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Cond {
    int i;
    String y;
}

class Pair {
    Cond q;
    String x;
}

class Mealy {
    int[] T;
    String[] F;
}

public class Main {
    public static void main(String[] args) {
        System.out.println("digraph {\nrankdir = LR");
        Mealy[] A;
        int K, m;
        int n, N;
        String[] X, Y;

        Scanner scanner = new Scanner(System.in);

        K = scanner.nextInt();
        X = new String[K];
        for (int i = 0; i < K; i++) {
            X[i] = scanner.next();
        }

        Map<Pair,Cond> D = new HashMap<>();
        Map<Cond, Integer> Q1 = new HashMap<>();

        m = scanner.nextInt();
        Y = new String[m];
        for (int i = 0; i < m; i++) {
            Y[i] = scanner.next();
        }

        n = scanner.nextInt();
        A = new Mealy[n];
        for (int i = 0; i < n; i++) {
            A[i] = new Mealy();
            A[i].T = new int[K];
            for (int j = 0; j < K; j++) {
                int x = scanner.nextInt();
                A[i].T[j] = x;
            }
        }

        for (int i = 0; i < n; i++) {
            A[i].F = new String[K];
            for (int j = 0; j < K; j++) {
                String x = scanner.next();
                A[i].F[j] = x;
            }
        }
        Cond q = new Cond();
        Pair p = new Pair();
        Cond[] Q = new Cond[n*m];
        N = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int t = 0;
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < K; l++) {
                        if (A[k].F[l].equals(Y[j]) && A[k].T[l] == i) {
                            q = new Cond();
                            q.i = i;
                            q.y = Y[j];
                            Q[N] = q;
                            N++;
                            t = 1;
                            break;
                        }
                    }
                    if (t == 1) {
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < N; i++) {
            Q1.put(Q[i], i);
            for (int j = 0; j < K; j++) {
                q.i = A[Q[i].i].T[j];
                q.y = A[Q[i].i].F[j];
                p.q = Q[i];
                p.x = X[j];
                D.put(p, q);
            }
        }

        for (int i = 0; i < N; i++) {
            System.out.println(i + " [label = \"(" + Q[i].i + "," + Q[i].y + ")\"]");
        }
        for (int i = 0; i < N; i++) {
            p.q = Q[i];
            for (int j = 0; j < K; j++) {
                p.x = X[j];
                Cond t = D.get(p);
                if (t != null) {
                    System.out.println(i + " -> " + Q1.get(D.get(p)) + " [label = \"" + p.x + "\"]");
                }
            }
        }
        System.out.println("}");
    }
}