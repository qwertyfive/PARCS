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
    public static final int NUM_WORKERS = 4; // Кількість воркерів

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
            if (i == NUM_WORKERS - 1) {
                endRow = HEIGHT;
            }

            channel c = info.createPoint().createChannel(); // Використовуйте createChannel() з info
            channels.add(c);

            info.parent.write(new point(startRow, endRow)); // Використовуйте info.parent.write() з point

            // Використовуйте c.write() замість p.write()
            c.write(startRow);
            c.write(endRow);
        }

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < NUM_WORKERS; i++) {
            point p = (point) info.parent.readObject();
            channel c = (channel) info.parent.readObject();

            int startRow = p.startRow;
            int endRow = p.endRow;

            int[] results = (int[]) c.readObject();

            for (int y = startRow; y < endRow; y++) {
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

    private static class point implements java.io.Serializable {
        int startRow, endRow;

        public point(int startRow, int endRow) {
            this.startRow = startRow;
            this.endRow = endRow;
        }
    }
}
