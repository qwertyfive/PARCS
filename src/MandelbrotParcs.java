import parcs.AM;
import parcs.AMInfo;
import parcs.channel;
import parcs.point;
import parcs.task;

public class MandelbrotParcs implements AM {

    public static final int HEIGHT = 800;
    public static final int WIDTH = 800;
    public static final double ZOOM = 100.0;
    public static final int MAX_ITER = 1000;

    public static void main(String[] args) {
        task curtask = new task();
        curtask.addJarFile("MandelbrotWorker.jar");
        (new MandelbrotParcs()).run(new AMInfo(curtask, (channel)null));
    }

    public void run(AMInfo info) {
        int num_points = 4;
        point[] points = new point[num_points];
        channel[] channels = new channel[num_points];

        for (int i = 0; i < num_points; i++) {
            points[i] = info.createPoint();
            channels[i] = points[i].createChannel();
            points[i].execute("MandelbrotWorker");
            channels[i].write(WIDTH);
            channels[i].write(HEIGHT);
            channels[i].write(ZOOM);
            channels[i].write(MAX_ITER);
        }

        int[][][] results = new int[num_points][][];
        for (int i = 0; i < num_points; i++) {
            results[i] = (int[][])channels[i].readObject();
        }

        // Об'єднання результатів та подальша робота з ними (наприклад, відображення результатів)
    }
}
