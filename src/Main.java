import parcs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class MandelbrotParcs implements AM {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int MAX_ITER = 1000;
    public static final double ZOOM = 150;

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotParcs.jar");
        (new MandelbrotParcs()).run(new AMInfo(curtask, (channel[])null));
        curtask.end();
    }

    public void run(AMInfo info) {
        List<channel> channels = new ArrayList<>();
        int numOfWorkers = 4;
        for (int i = 0; i < numOfWorkers; i++) {
            point p = info.createPoint();
            channel c = p.createChannel();
            channels.add(c);
            p.execute("MandelbrotParcs");
        }

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        int deltaY = HEIGHT / numOfWorkers;

        for (int i = 0; i < numOfWorkers; i++) {
            channel c = channels.get(i);
            c.write(i * deltaY);
            c.write(Math.min((i + 1) * deltaY, HEIGHT));
        }

        for (int i = 0; i < numOfWorkers; i++) {
            channel c = channels.get(i);
            int startY = c.readInt();
            int endY = c.readInt();
            int[][] colors = (int[][])c.readObject();
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    image.setRGB(x, y, colors[y][x]);
                }
            }
        }

        try {
            ImageIO.write(image, "png", new File("mandelbrot_parcs.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MandelbrotWorker implements AM {
    public void run(AMInfo info) {
        int startY = info.parent.readInteger();
        int endY = info.parent.readInteger();

        int[][] colors = new int[MandelbrotParcs.HEIGHT][MandelbrotParcs.WIDTH];

        for (int y = startY; y < endY; y++) {
            for (int x = 0; x < MandelbrotParcs.WIDTH; x++) {
                double zx = x - MandelbrotParcs.WIDTH / 2;
                double zy = y - MandelbrotParcs.HEIGHT / 2;
                zx /= MandelbrotParcs.ZOOM;
                zy /= MandelbrotParcs.ZOOM;
                double cX = zx;
                double cY = zy;
                int iter = MandelbrotParcs.MAX_ITER;
                double tmp;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                colors[y][x] = iter | (iter << 8);
            }
        }

        info.parent.write(startY);
        info.parent.write(endY);
        info.parent.write(colors);
    }
}
