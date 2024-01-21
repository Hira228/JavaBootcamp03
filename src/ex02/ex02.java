import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class ex02 {
    private static long sumArray = 0;

    public static void main(String[] args) throws InterruptedException {
        int[] arr = new int[Integer.parseInt(args[0])];
        int numberOfThreads = Integer.parseInt(args[1]);
        Random random = new Random();
        IntStream.range(0, arr.length)
                .forEach(i -> arr[i] = random.nextInt(100));

        System.out.println("Sum: " + Arrays.stream(arr).sum());

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            final int start = i * arr.length / numberOfThreads;
            final int end = (i + 1) * arr.length / numberOfThreads;
            final int num = i;

            threads[i] = new Thread(() -> {
                sumArr(start, end, arr, num);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Sum by threads: " + sumArray);

    }

    private synchronized static void sumArr(int a, int b, int[] arr, int num) {
        for(int i = a; i != b; ++i){
            sumArray += arr[i];
        }
        System.out.println("Thread " + num + ": from " + a + " to " + b + " sum is " + (a + b));
    }
}
