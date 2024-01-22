import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class ex03 {
    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream("src/1.txt");
        Scanner scanner = new Scanner(fis);
        QueueTasks tasks = new QueueTasks();
        TaskConsumer consumer = new TaskConsumer(tasks);
        Thread[] threads;
        Thread t = new Thread(consumer);
        t.start();
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            tasks.put(str);
        }
        tasks.setNoMoreTasks();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

class QueueTasks {
    Queue<String> t = new ArrayDeque<>();
    Thread[] threads;
    boolean noMoreTasks = false;

    public void setNoMoreTasks() {
        this.noMoreTasks = true;
    }

    public synchronized String get() {
        while (noMoreTasks || t.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String k = t.remove();
        notify();
        return k;
    }

    public synchronized void put(String s) {
        while (!t.isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        t.add(s);
        notify();
    }
}


class TaskConsumer implements Runnable {
    QueueTasks Tasks;
    TaskConsumer(QueueTasks Tasks){
        this.Tasks = Tasks;
    }

    public void run() {
        while (!Tasks.noMoreTasks || !Tasks.t.isEmpty()) {
            synchronized (Tasks) {
                if (!Tasks.t.isEmpty()) {
                    System.out.println(Tasks.get());
                }
            }
        }
    }
}