import model.model;

import java.net.ServerSocket;
import java.net.Socket;

public class mainServer {

    public static void main(String[] args) {

        model serverinfo = new model();
        String mediaFolderPath = "./mediaFiles/";
        try {
            ServerSocket ssSock = new ServerSocket(12345);

            while (true) {
                Socket clSock = ssSock.accept();
                new Thread(new serverWorker(clSock, serverinfo, mediaFolderPath)).start();
                System.out.println("Accepted Connection Request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
    }
}