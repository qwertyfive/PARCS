import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import parcs.AM;
import parcs.AMInfo;
import parcs.channel;
import parcs.point;
import parcs.task;

public class Solver implements AM {

    public static void main(String[] args) {
        System.out.print("class Solver start method main\n");

        task mainTask = new task();
        mainTask.addJarFile("Solver.jar");
        mainTask.addJarFile("Count.jar");

        System.out.print("class Solver method main adder jars\n");

        (new Solver()).run(new AMInfo(mainTask, (channel) null));

        System.out.print("class Solver method main finish work\n");

        mainTask.end();
    }

    public void run(AMInfo info) {
        int n = 20;
        int workers = 4;
        BigInteger result = solve(info, n, workers);
        System.out.println("Fibonacci(" + n + ") = " + result);
    }

    static public BigInteger solve(AMInfo info, int n, int workers) {
        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        int step = n / workers;

        for (int index = 0; index < workers; ++index) {
            int currentElement = (index + 1) * step;

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            newPoint.execute("Count");
            newChannel.write(currentElement);
        }

        BigInteger result = BigInteger.ZERO;
        for (int index = 0; index < workers; ++index) {
            BigInteger partialResult = (BigInteger) channels.get(index).readObject();
            result = result.add(partialResult);
        }

        return result;
    }
}
