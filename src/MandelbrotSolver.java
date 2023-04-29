import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import javax.imageio.ImageIO;
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

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < numWorkers; i++) {
            String result = channels.get(i).toString();
            String[] rows = result.trim().split("\n");
            for (int y = 0; y < rows.length; y++) {
                String[] pixels = rows[y].trim().split(" ");
                for (int x = 0; x < pixels.length; x++) {
                    int iter = Integer.parseInt(pixels[x]);
                    int color = iter < Mandelbrot.MAX_ITERATIONS ? iter : 0;
                    image.setRGB(x, y, new Color(color, color, color).getRGB());
                }
            }
        }

        File outputFile = new File("mandelbrot.png");
        try {
            ImageIO.write(image, "png", outputFile);
            System.out.println("Mandelbrot fractal image saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving Mandelbrot fractal image.");
            e.printStackTrace();
        }
    }
}
