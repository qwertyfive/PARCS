import parcs.*;

public class PiDigits implements AM {

    public static void main(String[] args) {
        task mainTask = new task();

        mainTask.addJarFile("PiDigits.jar");

        (new PiDigits()).run(new AMInfo(mainTask, null));
        mainTask.end();
    }

    public void run(AMInfo info) {
        long n = 1000; // Знак, який треба знайти

        point p = info.createPoint();
        channel c = p.createChannel();

        p.execute("PiDigits");
        c.write(n);

        double sum = 0.0;
        for (long k = 0; k < n; k++) {
            double sign = (k % 2 == 0) ? 1.0 : -1.0;
            double term = 1.0 / (2.0 * k + 1.0);
            sum += sign * term;
        }

        double pi = 4.0 * sum;

        c.write(pi);
        double result = c.readDouble();
        System.out.println("Pi to " + n + " digits: " + result);
    }
}
