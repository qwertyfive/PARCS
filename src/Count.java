import parcs.AM;
import parcs.AMInfo;

public class Count implements AM {
    public void run(AMInfo info) {
        long start, end;

        start = info.parent.readLong();
        end = info.parent.readLong();

        System.out.println("Worker started" + start + ", " + end);

        long count = 0;
        for (long num = start; num <= end; num++) {
            if (isArmstrong(num)) {
                count++;
            }
        }

        info.parent.write(count);
    }

    private boolean isArmstrong(long num) {
        long sum = 0;
        long temp = num;
        int numberOfDigits = String.valueOf(num).length();

        while (temp != 0) {
            long digit = temp % 10;
            sum += Math.pow(digit, numberOfDigits);
            temp /= 10;
        }

        return sum == num;
    }
}
