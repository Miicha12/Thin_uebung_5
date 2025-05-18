import java.util.HashMap;
import java.util.Map;

public class CollatzP {
    // Cache für die optimierte Collatz-Längen-Berechnung
    private Map<Long, Integer> cache;

    public CollatzP() {
        cache = new HashMap<>();
        cache.put(1L, 0); // Collatz-Länge von 1 ist 0
    }

    /**
     * Naive Berechnung der Collatz-Folge-Länge für n.
     */
    public static int collatzLengthNaive(long n) {
        int count = 0;
        while (n != 1) {
            if ((n & 1) == 0) {
                n = n / 2;
            } else {
                n = 3 * n + 1;
            }
            count++;
        }
        return count;
    }

    /**
     * Optimierte (rekursive + Memo) Berechnung der Collatz-Länge für n.
     */
    public int collatzLengthOpt(long n) {
        if (cache.containsKey(n)) {
            return cache.get(n);
        }
        long next = (n % 2 == 0) ? (n / 2) : (3 * n + 1);
        int len = 1 + collatzLengthOpt(next);
        cache.put(n, len);
        return len;
    }

    /**
     * Naive Variante von P: sucht im Intervall [1..x] die Zahl mit maximaler Collatz-Länge.
     */
    public static int P_naive(int x) {
        int bestN = 1;
        int bestLen = collatzLengthNaive(1);
        for (int n = 2; n <= x; n++) {
            int L = collatzLengthNaive(n);
            if (L > bestLen) {
                bestLen = L;
                bestN = n;
            }
        }
        return bestN;
    }

    /**
     * Optimierte Variante von P: nutzt Memoisierung.
     */
    public int P_opt(int x) {
        // Cache neu initialisieren für jeden Aufruf
        cache.clear();
        cache.put(1L, 0);

        int bestN = 1;
        int bestLen = 0;
        for (int n = 1; n <= x; n++) {
            int L = collatzLengthOpt(n);
            if (L > bestLen) {
                bestLen = L;
                bestN = n;
            }
        }
        return bestN;
    }

    public static void main(String[] args) {
        int[] testValues = {1_000, 10_000, 100_000, 1_000_000, 5_000_000, 10_000_000};
        CollatzP solver = new CollatzP();

        System.out.println("x\tP_naive\tTime_naive [ms]\tP_opt\tTime_opt [ms]");
        for (int x : testValues) {
            // Naive
            long t0 = System.nanoTime();
            int resNaive = P_naive(x);
            long t1 = System.nanoTime();

            // Optimiert
            long t2 = System.nanoTime();
            int resOpt = solver.P_opt(x);
            long t3 = System.nanoTime();

            double timeNaiveMs = (t1 - t0) / 1_000_000.0;
            double timeOptMs   = (t3 - t2) / 1_000_000.0;

            System.out.printf("%,d\t%,d\t%8.3f\t\t%,d\t%8.3f%n",
                    x, resNaive, timeNaiveMs, resOpt, timeOptMs);
        }

        // Nur optimierte Variante für x = 90_000_000
        int xLarge = 90_000_000;
        long t4 = System.nanoTime();
        int resLarge = solver.P_opt(xLarge);
        long t5 = System.nanoTime();
        double timeLargeMs = (t5 - t4) / 1_000_000.0;
        System.out.printf("%,d\t%s\t\t\t%,d\t%8.3f%n",
                xLarge, "-", resLarge, timeLargeMs);
    }
}

