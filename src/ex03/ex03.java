import java.io.*;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class ex03 {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        FileInputStream fileInputStream = new FileInputStream("src/1.txt");
        Scanner scanner = new Scanner(fileInputStream);
        QueueTasks tasks = new QueueTasks();
        Consumer consumer = new Consumer(tasks);
        Thread[] threads = new Thread[Integer.parseInt(args[0])];
        for(int i = 0; i < threads.length; ++i){
            threads[i] = new Thread(consumer);
            threads[i].start();
        }
        while(scanner.hasNext()) {
            String str = scanner.nextLine();
            tasks.Put(str);
        }
        tasks.setNoMoreTasks();
        for(Thread t : threads) t.join();
    }
}

class QueueTasks {
    boolean noMoreTasks = false;
    public void setNoMoreTasks() {
        noMoreTasks = true;
    }
    Queue<String> Tasks = new ArrayDeque<>();

    public synchronized void Put(String s) {
        while(!Tasks.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Tasks.add(s);
        notify();
    }

    public void downloadFile(String line, String pathToDownload) throws IOException {
        String[] spl =  line.split("//")[1].split("/");
        try (BufferedInputStream in = new BufferedInputStream(new URL(line.split(" ")[1]).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(pathToDownload +  spl[spl.length - 1])) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }


    public synchronized String Get() {
        while (Tasks.isEmpty() || noMoreTasks) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String k = Tasks.remove();
        notify();
        return k;
    }
}

class Consumer implements Runnable {
    QueueTasks tasks;
    Consumer(QueueTasks Tasks) {
        tasks = Tasks;
    }
    @Override
    public void run() {
        while (!tasks.noMoreTasks || !tasks.Tasks.isEmpty()) {
            synchronized (tasks) {
                if (!tasks.Tasks.isEmpty()) {
                    try {
                        String line = tasks.Get();
                        tasks.downloadFile(line, "src/");
                        System.out.println(Thread.currentThread().getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}

