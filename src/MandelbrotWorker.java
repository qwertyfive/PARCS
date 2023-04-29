import parcs.*;

public class MandelbrotWorker implements AM {
    public void run(AMInfo info) {
        int startY = info.parent.readInt();
        int endY = info.parent.readInt();

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
                while (zx * zx +                zy * zy < 4 && iter > 0) {
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

