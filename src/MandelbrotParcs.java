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

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotParcs.jar");
        (new MandelbrotParcs()).run(new AMInfo(curtask, (channel)null));
        curtask.end();
    }

    public void run(AMInfo info) {
        List<point> points = new ArrayList<>();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                points.add(new point(j, i));
            }
        }

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (point p : points) {
            int x = p.x;
            int y = p.y;

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

            image.setRGB(x, y, iter | (iter << 8));
        }

        try {
            File output = new File("MandelbrotParcs.png");
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static class point {
        int x, y;

        public point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
