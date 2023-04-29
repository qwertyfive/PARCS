import parcs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class MandelbrotParcs implements AM {

    public static final int WIDTH = 3000;
    public static final int HEIGHT = 3000;
    public static final int MAX_ITER = 5000;
    public static final int NUM_WORKERS = 4;

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotParcs.jar");
        (new MandelbrotParcs()).run(new AMInfo(curtask, (channel)null));
        curtask.end();
    }

    public void run(AMInfo info) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        List<AMInfo> workersInfo = new ArrayList<>();
        for (int i = 0; i < NUM_WORKERS; i++) {
            point p = info.createPoint();
            channel c = p.createChannel();
            p.execute(new AM() {
                @Override
                public void run(AMInfo info) {
                    int workerId = info.channel.readInt();
                    int numWorkers = info.channel.readInt();

                    int[][] colors = calculateMandelbrot(workerId, numWorkers);
                    info.channel.writeObject(colors);
                }
            });
            c.write(i);
            c.write(NUM_WORKERS);
            workersInfo.add(new AMInfo(p, c));
        }

        for (AMInfo workerInfo : workersInfo) {
            int[][] colors = (int[][]) workerInfo.channel.readObject();
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    image.setRGB(x, y, colors[y][x] | (colors[y][x] << 8));
                }
            }
        }

        try {
            File output = new File("MandelbrotParcs.png");
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int[][] calculateMandelbrot(int workerId, int numWorkers) {
        int[][] colors = new int[HEIGHT][WIDTH];

        for (int y = workerId; y < HEIGHT; y += numWorkers) {
            for (int x = 0; x < WIDTH; x++) {
                double zx = 0;
                double zy = 0;
                double cX = (x - 400) / 200.0;
                double cY = (y - 400) / 200.0;
                int iter = MAX_ITER;
                double tmp;

                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }

                colors[y][x] = iter;
            }
        }

        return colors;
    }
}
