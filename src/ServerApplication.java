import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
    public static void main(String[] args) {
        System.out.println("server!");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Socket socket = serverSocket.accept();
            System.out.println("Connection Accepted!");
            OutputStream os = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(os,true);
//            os.write("Connected to server.".getBytes());
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String messageFromClient;
            while ((messageFromClient = br.readLine()) != null) {
                System.out.println("From Client: " + messageFromClient);
                printWriter.println(messageFromClient);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}