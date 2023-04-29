import parcs.AM;
import parcs.AMInfo;
import parcs.channel;
import parcs.point;

import java.awt.Color;

public class MandelbrotWorker implements AM {

    private final int width;
    private final int height;
    private final double zoom;
    private final int maxIter;

    public MandelbrotWorker(int width, int height, double zoom, int maxIter) {
        this.width = width;
        this.height = height;
        this.zoom = zoom;
        this.maxIter = maxIter;
    }

    public void run(AMInfo info) {
        int[][] colors = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double zx = x - width / 2;
                double zy = y - height / 2;
                zx /= zoom;
                zy /= zoom;

                double cX = zx;
                double cY = zy;
                int iter = maxIter;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                colors[y][x] = iter | (iter << 8);
            }
        }
        info.parent.write(colors);
    }
}
