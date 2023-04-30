import parcs.AM;
import parcs.AMInfo;

public class Count implements AM {

    public void run(AMInfo info) {
        int start = info.parent.readInt();
        int end = info.parent.readInt();
        System.out.println("Worker started for range: " + start + " - " + end);

        int sum = 0;
        for (int i = start; i <= end; i++) {
            if (i % 3 == 0 || i % 5 == 0) {
                sum += i;
            }
        }

        info.parent.write(sum);
    }
}
