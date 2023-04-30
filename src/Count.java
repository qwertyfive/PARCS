import parcs.AM;
import parcs.AMInfo;

import java.math.BigInteger;

public class Count implements AM {

    public void run(AMInfo info) {
        int element = info.parent.readInt();
        System.out.println("Worker started for element: " + element);

        BigInteger result = fibonacci(element);
        info.parent.write(result);
    }

    private BigInteger fibonacci(int n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
        BigInteger prev1 = BigInteger.ZERO;
        BigInteger prev2 = BigInteger.ONE;
        BigInteger current = BigInteger.ZERO;

        for (int i = 2; i <= n; i++) {
            current = prev1.add(prev2);
            prev1 = prev2;
            prev2 = current;
        }

        return current;
    }
}
