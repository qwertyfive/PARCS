import parcs.*;
class MandelbrotWorker implements AM {
    @Override
    public void run(AMInfo info) {
        int startRow = info.parent.readInt();
        int endRow = info.parent.readInt();

        int[] results = new int[(endRow - startRow) * MandelbrotParcs.WIDTH];

        for (int y = startRow; y < endRow; y++) {
            for (int x = 0; x < MandelbrotParcs.WIDTH; x++) {
                int index = (y - startRow) * MandelbrotParcs.WIDTH + x;
                results[index] = MandelbrotParcs.calculatePoint(x, y);
            }
        }

        info.parent.write(results);
    }
}
