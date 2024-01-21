import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class ex02 {
    private static int sumArray = 0;

    public static void main(String[] args) throws InterruptedException {

        int[] arr = new int[Integer.parseInt(args[0])];
        int numberOfThreads = Integer.parseInt(args[1]);
        Random random = new Random();
        IntStream.range(0, arr.length)
                .forEach(i -> arr[i] = random.nextInt(100));

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            final int start = i * arr.length / numberOfThreads;
            final int end = (i + 1) * arr.length / numberOfThreads;

            threads[i] = new Thread(() -> {
                sumArr(start, end, arr);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(Arrays.stream(arr).sum());
        System.out.println(sumArray);

    }

    private synchronized static void sumArr(int a, int b, int[] arr) {
        for(int i = a; i != b; ++i){
            sumArray += arr[i];
        }
    }
}
