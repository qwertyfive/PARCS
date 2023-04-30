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
        long range = 1000000;
        int workers = 1;
        long tStart = System.nanoTime();

        long res = solve(info, n, a, b);

        long tEnd = System.nanoTime();

        System.out.println("Count of Armstrong Numbers in range [1, " + range + "] = " + result);
        System.out.println("time = " + ((tEnd - tStart) / 1000000) + "ms");
    }

    static public long solve(AMInfo info, long range, int workers) {
        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        long step = range / workers;

        for (int index = 0; index < workers; ++index) {
            long currentStart = index * step + 1;
            long currentEnd = (index + 1) * step;

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            System.out.println("Worker " + index + ": " + currentStart + " to " + currentEnd);
            newPoint.execute("Count");
            newChannel.write(currentStart);
            newChannel.write(currentEnd);
        }

        long result = 0;
        for (int index = 0; index < workers; ++index) {
            long partialResult = channels.get(index).readLong();
            result += partialResult;
        }

        return result;
    }
}
