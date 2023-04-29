import java.io.*;
import java.util.*;

import parcs.*;

public class MandelbrotSolver implements AM {

    public static void main(String[] args) {
        task mainTask = new task();

        mainTask.addJarFile("MandelbrotSolver.jar");
        mainTask.addJarFile("Mandelbrot.jar");

        (new MandelbrotSolver()).run(new AMInfo(mainTask, (channel)null));
        mainTask.end();
    }

    public void run(AMInfo info) {
        int width = 800;
        int height = 600;
        int numWorkers = 4;

        int xStart = 0;
        int xEnd = width;
        int yStep = height / numWorkers;

        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        for (int i = 0; i < numWorkers; i++) {
            int yStart = i * yStep;
            int yEnd = (i + 1) * yStep;

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            newPoint.execute("Mandelbrot");
            newChannel.write(xStart);
            newChannel.write(xEnd);
            newChannel.write(yStart);
            newChannel.write(yEnd);
            newChannel.write(width);
            newChannel.write(height);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (int i = 0; i < numWorkers; i++) {
                String result = channels.get(i).toString();
                writer.write(result);
            }
        } catch (IOException e) {
            System.out.println("Error writing to output file");
            e.printStackTrace();
        }
    }
}
