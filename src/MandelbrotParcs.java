import parcs.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class MandelbrotParcs implements AM {

    public static final int WIDTH = 3000;
    public static final int HEIGHT = 3000;
    public static final int MAX_ITER = 5000;
    public static final int NUM_WORKERS = 4;

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotParcs.jar");
        (new MandelbrotParcs()).run(new AMInfo(curtask, null));
        curtask.end();
    }

    public void run(AMInfo info) {
        int rowsPerWorker = HEIGHT / NUM_WORKERS;
        List<channel> channels = new ArrayList<>();

        for (int i = 0; i < NUM_WORKERS; i++) {
            int startRow = i * rowsPerWorker;
            int endRow = (i + 1) * rowsPerWorker;

            channel c = info.createPoint().createChannel();
            channels.add(c);
            info.createPoint().execute("MandelbrotParcs");

            c.write(startRow);
            c.write(endRow);
            c.write(MAX_ITER);
        }

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (channel ch : channels) {
            List<point> points = (List<point>) ch.readObject();
            for (point p : points) {
                image.setRGB(p.x, p.y, p.iter | (p.iter << 8));
            }
        }

        try {
            File output = new File("MandelbrotParcs.png");
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static class point implements java.io.Serializable {
        int x, y, iter;

        public point(int x, int y, int iter) {
            this.x = x;
            this.y = y;
            this.iter = iter;
        }
    }
}
