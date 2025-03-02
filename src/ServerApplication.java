import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerApplication {

    private final static CopyOnWriteArrayList<String> messageStore = new CopyOnWriteArrayList<>();
    private final static List<PrintWriter> clientStore = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("server!");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Thread mainThread = Thread.currentThread();
            System.out.println("Thread: " + mainThread.getName());
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
                        clientStore.add(printWriter);
                        printWriter.println("Connected to server!");
                        InputStream is = finalSocket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String messageFromClient;
                        while ((messageFromClient = br.readLine()) != null) {
                            messageStore.add("user-" + currThread.getName()  + ": "+ messageFromClient);
                            System.out.println("From Client " + currThread.getName()  + ": "+ messageFromClient);
                            for (PrintWriter writer: clientStore) {
                                if (printWriter != writer) {
                                    writer.println(messageStore.get(messageStore.size() - 1));
                                }
                            }
                        }
                        finalSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}