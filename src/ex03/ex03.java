import java.io.*;
import java.net.URL;

public class ex03 {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[Integer.parseInt(args[0])];
        int i = 0;
        String path = "src/1.txt";
        String pathToDownload = "src/";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String finalLine = line;
                threads[i] = new Thread(() -> {
                    try {
                        downloadFile(finalLine, pathToDownload);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threads[i++].start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for(Thread thread : threads) {
            thread.join();
        }
    }
    private static synchronized void downloadFile(String line, String pathToDownload) throws IOException, InterruptedException {
        String[] spl =  line.split("//")[1].split("/");
        try (BufferedInputStream in = new BufferedInputStream(new URL(line.split(" ")[1]).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(pathToDownload +  spl[line.split("//")[1].split("/").length - 1])) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
    }

}
