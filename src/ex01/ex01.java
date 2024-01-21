import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ex01 {
    private static volatile boolean isThread1Turn = true;

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            printRepeatably("Egg", 50);
        });
        thread.start();

        Thread thread1 = new Thread(() -> {
           printRepeatably("Hen", 50);
        });
        thread1.start();

        thread.join();
        thread1.join();
    }

    private static void printRepeatably(String message, int count) {
        for(int i = 0; i < count; ++i){
            while ((isThread1Turn && message.equals("Hen")) || (!isThread1Turn && message.equals("Egg"))) {
                Thread.yield();
            }
            System.out.println(message);
            isThread1Turn = !isThread1Turn;
        }
    }
}
