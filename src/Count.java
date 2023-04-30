import parcs.*;
public class Count implements AM {
    public void run(AMInfo info) {
        long startRow, endRow;

        startRow = info.parent.readLong();
        endRow = info.parent.readLong();
        int index = info.parent.readInt();
        double xMin = info.parent.readDouble();
        double xMax = info.parent.readDouble();
        double yMin = info.parent.readDouble();
        double yMax = info.parent.readDouble();
        int width = info.parent.readInt();
        int height = info.parent.readInt();

        System.out.println("Worker started");

        long[][] result = new long[(int)(endRow - startRow)][width];
        for (int row = (int)startRow; row < endRow; row++) {
            for (int col = 0; col < width; col++) {
                double x = xMin + (xMax - xMin) * col / width;
                double y = yMin + (yMax - yMin) * row / height;
                result[row - (int)startRow][col] = mandelbrot(x, y);
            }
        }

        info.parent.write(result);
        info.parent.write(startRow);
    }

    private long mandelbrot(double x, double y) {
        double real = 0.0;
        double imaginary = 0.0;
        int iteration = 0;
        int maxIterations = 1000;

        while (iteration < maxIterations && real * real + imaginary * imaginary < 4) {
            double realTemp = real * real - imaginary * imaginary + x;
            imaginary = 2 * real * imaginary + y;
            real = realTemp;
            iteration++;
        }

        return iteration;
    }
}
