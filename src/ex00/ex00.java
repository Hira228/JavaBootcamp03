public class ex00 {
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
        for(int i = 0; i < count; ++i) System.out.println(message);
    }
}
