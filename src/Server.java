import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);
        CloudSound cs = new CloudSound();
        System.out.println("> Server is running and waiting for connection");

        while (true) {
            Socket socket = ss.accept();

            ServerWorker sw = new ServerWorker(socket,cs);
            new Thread(sw).start();
        }
    }
}
