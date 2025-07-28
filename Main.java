import java.io.*;
import java.math.BigInteger;
import java.util.*;

class Point {
    int x;
    BigInteger y;

    Point(int x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
}

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("input2.json"));

        int n = 0, k = 0;
        List<Point> points = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.contains("\"n\"")) {
                String[] parts = line.split(":");
                n = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim());
            } else if (line.contains("\"k\"")) {
                String[] parts = line.split(":");
                k = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim());
            } else if (line.matches("^\"\\d+\"\\s*:\\s*\\{")) {
                int x = Integer.parseInt(line.replaceAll("[^0-9]", "").trim());

                String baseLine = reader.readLine().trim();
                String valueLine = reader.readLine().trim();

                int base = Integer.parseInt(baseLine.split(":")[1].replaceAll("[^0-9]", "").trim());
                String valueStr = valueLine.split(":")[1].replaceAll("[\"{},]", "").trim();

                BigInteger y = new BigInteger(valueStr, base);
                points.add(new Point(x, y));
            }
        }

        reader.close();

        points.sort(Comparator.comparingInt(p -> p.x));
        List<Point> selected = points.subList(0, k);

        BigInteger secret = lagrangeConstantTerm(selected);
        System.out.println("Secret (constant term): " + secret.toString());
    }

    public static BigInteger lagrangeConstantTerm(List<Point> points) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < points.size(); i++) {
            BigInteger xi = BigInteger.valueOf(points.get(i).x);
            BigInteger yi = points.get(i).y;

            BigInteger term = yi;
            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(points.get(j).x);
                    BigInteger numerator = xj.negate(); // -xj
                    BigInteger denominator = xi.subtract(xj); // xi - xj

                    term = term.multiply(numerator).divide(denominator);
                }
            }

            result = result.add(term);
        }

        return result;
    }
}
