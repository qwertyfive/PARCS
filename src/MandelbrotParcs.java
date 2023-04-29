import parcs.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class MandelbrotParcs {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int MAX_ITER = 1000;
    public static final double ZOOM = 150;

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotParcs.jar");
        (new MandelbrotAM()).run(new AMInfo(curtask, null));
        curtask.end();
    }
}

class MandelbrotAM implements AM {
    public void run(AMInfo info) {
        List<channel> channels = new ArrayList<>();
        int numOfWorkers = 4;
        for (int i = 0; i < numOfWorkers; i++) {
            point p = info.createPoint();
            channel c = p.createChannel();
            channels.add(c);
            p.execute("MandelbrotWorker");
        }

        BufferedImage image = new BufferedImage(MandelbrotParcs.WIDTH, MandelbrotParcs.HEIGHT, BufferedImage.TYPE_INT_RGB);
        int deltaY = MandelbrotParcs.HEIGHT / numOfWorkers;

        for (int i = 0; i < numOfWorkers; i++) {
            channel c = channels.get(i);
            c.write(i * deltaY);
            c.write(Math.min((i + 1) * deltaY, MandelbrotParcs.HEIGHT));
        }

        for (int i = 0; i < numOfWorkers; i++) {
            channel c = channels.get(i);
            int startY = c.readInt();
            int endY = c.readInt();
            int[][] colors = (int[][])c.readObject();
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < MandelbrotParcs.WIDTH; x++) {
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
