import org.json.JSONObject;
import java.util.*;
import java.math.BigInteger;

public class SecretSharing {

    public static void main(String[] args) {
        // Example JSON input
        String jsonString = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 4,\n" +
                "        \"k\": 3\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"4\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"2\",\n" +
                "        \"value\": \"111\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"12\"\n" +
                "    },\n" +
                "    \"6\": {\n" +
                "        \"base\": \"4\",\n" +
                "        \"value\": \"213\"\n" +
                "    }\n" +
                "}";

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        List<Point> points = new ArrayList<>();
        for (String key : jsonObject.keySet()) {
            if (!key.equals("keys")) {
                JSONObject root = jsonObject.getJSONObject(key);
                int base = root.getInt("base");
                String value = root.getString("value");
                int y = Integer.parseInt(value, base);
                int x = Integer.parseInt(key);
                points.add(new Point(x, y));
            }
        }

        if (points.size() < k) {
            throw new IllegalArgumentException("Not enough points to determine the polynomial");
        }

        // Perform Lagrange interpolation
        double constantTerm = lagrangeInterpolation(points, k);
        System.out.println("Constant term (c): " + constantTerm);
    }

    private static double lagrangeInterpolation(List<Point> points, int k) {
        double c = 0.0;

        for (int i = 0; i < k; i++) {
            Point xi = points.get(i);
            double li = 1.0;
            double yi = xi.y;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    Point xj = points.get(j);
                    li *= (0 - xj.x) / (xi.x - xj.x);
                }
            }
            c += li * yi;
        }

        return c;
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
