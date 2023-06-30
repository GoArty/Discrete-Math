import java.util.Scanner;

public class VisMealy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();   // количество состояний
        int m = sc.nextInt();   // размер входного алфавита
        int q0 = sc.nextInt();  // номер начального состояния

        int[][] delta = new int[n][m];   // матрица переходов
        String[][] phi = new String[n][m];   // матрица выходов

        // считываем матрицы переходов и выходов
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                delta[i][j] = sc.nextInt();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                phi[i][j] = sc.next();
            }
        }

        // выводим описание автомата в формате DOT
        System.out.println("digraph {");
        System.out.println("    rankdir = LR");

        // выводим вершины
        /*
        for (int i = 0; i < n; i++) {
            System.out.println("    " + i + " [label = \"" + i + "\"]");
        }
        */

        // выводим ребра
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int nextState = delta[i][j];
                String output = phi[i][j];
                char inputChar = (char) ('a' + j);
                System.out.println("    " + i + " -> " + nextState + " [label = \"" + inputChar + "(" + output + ")\"]");
            }
        }

        System.out.println("}");
    }
}