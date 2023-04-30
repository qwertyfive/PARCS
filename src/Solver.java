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
        int range = 1000000000;
        int workers = 8;
        int result = solve(info, range, workers);
        System.out.println("Sum of numbers divisible by 3 or 5 in range [1, " + range + "] = " + result);
    }

    static public int solve(AMInfo info, int range, int workers) {
        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        int step = range / workers;

        for (int index = 0; index < workers; ++index) {
            int currentStart = index * step + 1;
            int currentEnd = (index + 1) * step;

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            newPoint.execute("Count");
            newChannel.write(currentStart);
            newChannel.write(currentEnd);
        }

        int result = 0;
        for (int index = 0; index < workers; ++index) {
            int partialResult = channels.get(index).readInt();
            result += partialResult;
        }

        return result;
    }
}
