import model.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
import java.util.Base64;

public class clientHelper implements Runnable {
    private int loggedUserID;
    private String mediaFolderPath;
    private Socket socket;
    private int lastUpdatedLog;
    private model serverInfo;

    public clientHelper(Socket cSock, model serverInfoIn,String mediaFolderIn) {
        this.socket = cSock;
        this.lastUpdatedLog = serverInfoIn.getNLogs();
        this.mediaFolderPath=mediaFolderIn;
    }

    public void processInput(String input, BufferedReader socketReader, PrintWriter socketWriter) {
          System.out.println(input);
        if (input.substring(0, 4).equals("DOWN")) {
            System.out.println("bout to download");
            this.Download(input.substring(5), socketWriter);
            
        }
    }

    public void Register(){}
    public void Login(){}

    // Falta respestar as restrições
    public void Download(String fileName, PrintWriter socketWriter) {
          String filePath= new StringBuilder(this.mediaFolderPath).append(fileName).toString();
       System.out.println(filePath);
          try {
            File file2download = new File(filePath);
            byte[] fileBytes = Files.readAllBytes(file2download.toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileBytes);

            socketWriter.println(encodedString);
            socketWriter.flush();
            System.out.println(encodedString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error writing" + e.getLocalizedMessage());
        }

    }

    public void publish(){}
    public void search(){}

    public void run() {
        // falta a thread para checkar as updates no model.uploadLog
        try {

            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(this.socket.getOutputStream());
            String s = null;

            while ((s = socketReader.readLine()) != null) {
                System.out.println("Client sent " + s);
                this.processInput(s, socketReader, socketWriter);
            }
            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}