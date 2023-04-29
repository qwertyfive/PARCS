import parcs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

class MandelbrotWorker implements AM {
    @Override
    public void run(AMInfo info) {
        int startRow = info.parent.readInt();
        int endRow = info.parent.readInt();

        int[] results = new int[(endRow - startRow) * MandelbrotParcs.WIDTH];

        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < MandelbrotParcs.WIDTH; x++) {
                int index = (y - startRow) * MandelbrotParcs.WIDTH + x;
                results[index] = MandelbrotParcs.calculatePoint(x, y);
            }
        }

        info.parent.write(results);
    }
}

public class MandelbrotParcs implements AM {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int MAX_ITER = 1000;
    public static final int NUM_WORKERS = 4; // Кількість воркерів

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotParcs.jar");
        curtask.addJarFile("MandelbrotWorker.jar");
        (new MandelbrotParcs()).run(new AMInfo(curtask, (channel)null));
        curtask.end();
    }

    public void run(AMInfo info) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        int rowsPerWorker = HEIGHT / NUM_WORKERS;
        List<channel> channels = new ArrayList<>();

        for (int i = 0; i < NUM_WORKERS; i++) {
            int startRow = i * rowsPerWorker;
            int endRow = i == NUM_WORKERS - 1 ? HEIGHT : (i + 1) * rowsPerWorker;

            point p = info.createPoint();
            channel c = p.createChannel();
            channels.add(c);

            p.execute("MandelbrotWorker");

            c.write(startRow);
            c.write(endRow);
        }

        for (int i = 0; i < NUM_WORKERS; i++) {
            int startRow = i * rowsPerWorker;
            int[] results = (int[]) channels.get(i).readObject();

            for (int y = startRow; y < startRow + rowsPerWorker && y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    int index = (y - startRow) * WIDTH + x;
                    int iter = results[index];
                    image.setRGB(x, y, iter | (iter << 8));
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

    public static int calculatePoint(int x, int y) {
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

        return iter;
    }
}
