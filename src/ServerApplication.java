import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
    public static void main(String[] args) {
        System.out.println("server!");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Thread main = Thread.currentThread();
            System.out.println("Thread: " + main.getName());
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                Socket finalSocket = socket;
                Thread thread = new Thread(() -> {
                    try {
                        System.out.println("Connection Accepted!");
                        Thread currThread = Thread.currentThread();
                        System.out.println("Thread: " + currThread.getName());
                        OutputStream os = finalSocket.getOutputStream();
                        PrintWriter printWriter = new PrintWriter(os, true);
                        InputStream is = finalSocket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String messageFromClient;
                        while ((messageFromClient = br.readLine()) != null) {
                            System.out.println("From Client: " + messageFromClient);
                            printWriter.println(messageFromClient);
                        }
                        finalSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                thread.start();
                System.out.println("IT HAS ENDED!!!!!!!!!!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}