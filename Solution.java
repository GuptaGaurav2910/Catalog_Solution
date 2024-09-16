import java.util.*;

public class PolynomialConstantFinder {
    public static void main(String[] args) {
        String inputString = "{\"keys\": {\"n\": 4, \"k\": 3}, \"1\": {\"base\": \"10\", \"value\": \"4\"}, \"2\": {\"base\": \"2\", \"value\": \"111\"}, \"3\": {\"base\": \"10\", \"value\": \"12\"}, \"6\": {\"base\": \"4\", \"value\": \"213\"}}";

        Map<Integer, Integer> points = extractPoints(inputString);
        double constantTerm = findConstantTerm(points);
        System.out.println("Constant term: " + constantTerm);
    }

    private static Map<Integer, Integer> extractPoints(String inputString) {
        Map<Integer, Integer> points = new HashMap<>();

        try {
            // Remove JSON-like structure and split by commas
            String[] parts = inputString.replaceAll("\\{|}|\"", "").split(",");

            // Extract number of points (n) and minimum roots required (k)
            int n = Integer.parseInt(parts[1].split(":")[1].trim());
            int k = Integer.parseInt(parts[2].split(":")[1].trim());

            // Iterate through parts to extract x, base (skip), and value
            for (String part : parts) {
                if (part.contains(":")) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length >= 3) {
                        int x = Integer.parseInt(keyValue[0].trim());
                        // Skip base (it's not needed)
                        int y = Integer.parseInt(keyValue[2].trim(), Integer.parseInt(keyValue[1].trim()));
                        points.put(x, y);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing input string: " + e.getMessage());
            e.printStackTrace();
        }

        return points;
    }

    private static double findConstantTerm(Map<Integer, Integer> points) {
        int n = points.size();
        double constantTerm = 0;

        // Convert points to arrays
        Integer[] xValues = points.keySet().toArray(new Integer[0]);
        Integer[] yValues = points.values().toArray(new Integer[0]);

        // Compute the Lagrange polynomial and evaluate at x = 0
        for (int i = 0; i < n; i++) {
            double term = yValues[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term *= (0 - xValues[j]) / (double) (xValues[i] - xValues[j]);
                }
            }
            constantTerm += term;
        }

        return constantTerm;
    }
}
