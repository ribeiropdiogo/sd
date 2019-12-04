import model.model;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class mainServer {

    public static void main(String[] args) {

        model serverinfo = new model();
        String mediaFolderPath = "./mediaFiles/";
        try {
            ServerSocket ssSock = new ServerSocket(12345);

            while (true) {
                Socket clSock = ssSock.accept();
                new Thread(new serverWorker(clSock, serverinfo, mediaFolderPath)).start();
                System.out.println("Acepted Request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
    }
}