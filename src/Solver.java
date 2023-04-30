import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.*;
import java.util.*;
import javax.imageio.ImageIO;

import parcs.*;
public class Solver implements AM
{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    public static void main(String[] args)
    {
        System.out.print("class Solver start method main\n");

        task mainTask = new task();

        mainTask.addJarFile("Solver.jar");
        mainTask.addJarFile("Count.jar");

        System.out.print("class Solver method main adder jars\n");

        (new Solver()).run(new AMInfo(mainTask, (channel)null));

        System.out.print("class Solver method main finish work\n");


        mainTask.end();
    }

    public void run(AMInfo info)
    {
        long n = 4;

        double xMin = -2.0;
        double xMax = 1.0;
        double yMin = -1.5;
        double yMax = 1.5;

        long tStart = System.nanoTime();

        long[][] res = solve(info, n, xMin, xMax, yMin, yMax);

        long tEnd = System.nanoTime();

        saveImage(res, "mandelbrot.png");

        System.out.println("time = " + ((tEnd - tStart) / 1000000) + "ms");
    }

    static public long[][] solve(AMInfo info, long n, double xMin, double xMax, double yMin, double yMax) {
        List<point> points = new ArrayList<>();
        List<channel> channels = new ArrayList<>();

        long remainder = (HEIGHT - 0) % n;
        long length = (HEIGHT - 0) / n;

        for (int index = 0; index < n; ++index) {
            long currentStart = index * length;
            long currentEnd = (index + 1) * length + ((n - index - 1 < remainder) ? 1 : 0);

            System.out.println(index + " worker range: " + currentStart + " - " + currentEnd);

            point newPoint = info.createPoint();
            channel newChannel = newPoint.createChannel();

            channels.add(newChannel);
            points.add(newPoint);

            newPoint.execute("Count");
            newChannel.write(currentStart);
            newChannel.write(currentEnd);
            newChannel.write(index);
            newChannel.write(xMin);
            newChannel.write(xMax);
            newChannel.write(yMin);
            newChannel.write(yMax);
            newChannel.write(WIDTH);
            newChannel.write(HEIGHT);
        }

        long[][] results = new long[HEIGHT][WIDTH];
        for (int index = 0; index < n; ++index) {
            long[][] threadResult = (long[][]) channels.get(index).readObject();
            System.arraycopy(threadResult, 0, results, (int) channels.get(index).readLong(), threadResult.length);
        }

        return results;
    }

    public static void saveImage(long[][] data, String fileName) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                float hue = (float) data[y][x] / 50.0f;
                Color color = Color.getHSBColor(hue, 1.0f, 1.0f);
                image.setRGB(x, y, color.getRGB());
            }
        }

        try {
            ImageIO.write(image, "PNG", new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}