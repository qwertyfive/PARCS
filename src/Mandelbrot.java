import parcs.*;

public class Mandelbrot implements AM {
    public static final int MAX_ITERATIONS = 1000;

    public void run(AMInfo info) {
        int xStart = info.parent.readInt();
        int xEnd = info.parent.readInt();
        int yStart = info.parent.readInt();
        int yEnd = info.parent.readInt();
        int width = info.parent.readInt();
        int height = info.parent.readInt();

        System.out.println("Worker started");

        StringBuilder result = new StringBuilder();
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                double zx = x * 3.0 / width - 2;
                double zy = y * 2.0 / height - 1;
                double cX = zx;
                double cY = zy;
                int iter = 0;
                while (iter < MAX_ITERATIONS && zx * zx + zy * zy < 4) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter++;
                }
                result.append(iter);
                result.append(" ");
            }
            result.append("\n");
        }

        info.parent.write(result.toString());
    }
}
