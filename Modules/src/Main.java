import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String program = scanner.next(); // считываем программу

        Set<String> functions = new HashSet<>(); // множество функций
        Map<String, Set<String>> graph = new HashMap<>(); // граф вызовов функций

        // парсим лексемы и строим граф вызовов функций
        while (!program.isEmpty()) {
            program = program.trim();

            // парсим заголовок функции
            int colonIndex = program.indexOf(":=");
            String functionName = program.substring(0, colonIndex).trim();
            functions.add(functionName); // добавляем функцию в множество
            program = program.substring(colonIndex + 2);

            // парсим список формальных параметров
            int argsListStartIndex = program.indexOf("(") + 1;
            int argsListEndIndex = program.indexOf(")");
            String[] formalArgs = program.substring(argsListStartIndex, argsListEndIndex).split(",");
            program = program.substring(argsListEndIndex + 1);

            // парсим тело функции
            int semicolonIndex = program.indexOf(";");
            String expression = program.substring(0, semicolonIndex);
            program = program.substring(semicolonIndex + 1);

            // добавляем вершины и рёбра в граф
            graph.putIfAbsent(functionName, new HashSet<>());
            for (String arg : formalArgs) {
                graph.putIfAbsent(arg, new HashSet<>());
                graph.get(functionName).add(arg);
            }
            parseExpression(expression, graph, functionName);
        }

        // находим компоненты связности в графе
        int maxComponents = 0;
        Set<String> visited = new HashSet<>();
        for (String function : functions) {
            if (!visited.contains(function)) {
                maxComponents++;
                dfs(graph, function, visited);
            }
        }

        System.out.println(maxComponents);
    }

    // парсим выражение на наличие вызовов функций и добавляем рёбра в граф
    private static void parseExpression(String expression, Map<String, Set<String>> graph, String caller) {
        int length = expression.length();
        int i = 0;
        while (i < length) {
            char ch = expression.charAt(i);
            if (Character.isLetter(ch)) {
                int j = i;
                while (j < length && (Character.isLetterOrDigit(expression.charAt(j)))) {
                    j++;
                }
                String identifier = expression.substring(i, j);
                if (graph.containsKey(identifier)) { // если идентификатор является функцией
                    graph.putIfAbsent(caller, new HashSet<>());
                    graph.get(caller).add(identifier); // добавляем ребро от вызывающей функции к вызываемой
                    i = j;
                }
            }
            i++;
        }
    }

    // алгоритм поиска компонент связности в графе
    private static void dfs(Map<String, Set<String>> graph, String vertex, Set<String> visited) {
        visited.add(vertex);
        for (String neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs(graph, neighbor, visited);
            }
        }
    }
}