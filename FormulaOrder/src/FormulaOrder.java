import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class FormulaOrder {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> formulaList = new ArrayList<>();
        while (sc.hasNextLine()) {
            formulaList.add(sc.nextLine().trim());
        }
        sc.close();

        Map<String, Set<String>> graph = new HashMap<>(); // граф зависимостей между формулами
        Map<String, Integer> indegrees = new HashMap<>(); // степени входа вершин в графе
        Set<String> allVariables = new HashSet<>(); // множество всех переменных
        Map<String, String> variableToFormula = new HashMap<>(); // отображение переменных на формулы, в которых они используются
        for (String formula : formulaList) {
            int equalsIndex = formula.indexOf('=');
            if (equalsIndex == -1) {
                System.out.println("syntax error");
                return;
            }
            String lhs = formula.substring(0, equalsIndex).trim(); // левая часть формулы
            String rhs = formula.substring(equalsIndex + 1).trim(); // правая часть формулы
            String[] variables = lhs.split(","); // переменные в левой части формулы
            for (String variable : variables) {
                if (!variable.matches("[a-zA-Z]\\w*")) { // проверка корректности имени переменной
                    System.out.println("syntax error");
                    return;
                }
                if (allVariables.contains(variable)) { // проверка на повторное объявление переменной
                    System.out.println("syntax error");
                    return;
                }
                allVariables.add(variable);
                variableToFormula.put(variable, formula);
                indegrees.put(variable, 0);
            }
            List<String> rhsVariables = extractVariables(rhs); // получение всех переменных, используемых в правой части формулы
            for (String startVariable : variables) {
                for (String endVariable : rhsVariables) {
                    if (startVariable.equals(endVariable)) { // переменная зависит сама от себя
                        System.out.println("cycle");
                        return;
                    }
                    graph.computeIfAbsent(endVariable, k -> new HashSet<>()).add(startVariable); // добавление дуги в граф зависимостей
                    indegrees.put(startVariable, indegrees.getOrDefault(startVariable, 0) + 1); // увеличение степени входа для вершины
                }
            }
        }

        Stack<String> stack = new Stack<>();
        for (String variable : allVariables) { // добавление вершин со степенью входа 0 в стек
            if (indegrees.get(variable) == 0) {
                stack.push(variable);
            }
        }

        List<String> sortedFormulas = new ArrayList<>();
        while (!stack.isEmpty()) {
            String variable = stack.pop();
            String formula = variableToFormula.get(variable);
            sortedFormulas.add(formula);
            for (String dependentVariable : graph.getOrDefault(variable, new HashSet<>())) {
                indegrees.put(dependentVariable, indegrees.get(dependentVariable) - 1);
                if (indegrees.get(dependentVariable) == 0) {
                    stack.push(dependentVariable);
                }
            }
        }

        if (sortedFormulas.size() != formulaList.size()) { // не удалось отсортировать все формулы (циклическая зависимость)
            System.out.println("cycle");
        } else { // все формулы отсортированы верно
            for (String formula : sortedFormulas) {
                System.out.println(formula);
            }
        }
    }

/**
 * Извлекает все переменные, используемые в заданной строке.
 */
private static List<String> extractVariables(String str) {
    List<String> variables = new ArrayList<>();
    String[] tokens = str.split("\\b");
    for (String token : tokens) {
        if (token.matches("[a-zA-Z]\\w*")) { // проверка, является ли токен именем переменной
            variables.add(token);
        }
    }
    return variables;
}
}