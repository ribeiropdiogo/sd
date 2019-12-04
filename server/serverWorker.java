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
import java.util.ArrayList;
import java.util.Base64;

public class serverWorker implements Runnable {
    private String loggedUserName;
    private String mediaFolderPath;
    private Socket socket;
    private int lastUpdatedLog;
    private model serverInfo;

    public serverWorker(Socket cSock, model serverInfoIn, String mediaFolderIn) {
        this.serverInfo = serverInfoIn;
        this.socket = cSock;
        this.lastUpdatedLog = serverInfoIn.getNLogs();
        this.mediaFolderPath = mediaFolderIn;
    }

    public void processInput(String input, BufferedReader socketReader, PrintWriter socketWriter) {
        System.out.println(input);

        if (input.length() < 5) {
            this.sendNoInput(socketWriter);
            return;
        }

        // falta testar o length do input pa todos
        switch (input.substring(0, 3)) {

        case ("REG"):
            System.out.println("registering user");
            this.register(input.substring(4), socketWriter);
            break;
        case ("LOG"):
            System.out.println("trying to login");
            this.login(input.substring(4), socketWriter);
            break;
        case ("PUB"):
            System.out.println("user wants to publish");
            this.publish(input.substring(4), socketWriter);
            break;
        case ("SEK"):
            System.out.println("user searching");
            this.search(input.substring(4), socketWriter);
            break;
        case ("DOW"):
            System.out.println("bout to download");
            this.download(input.substring(4), socketWriter);
            break;
        default:
            this.sendNoInput(socketWriter);
            break;
        }
    }

    public void register(String input, PrintWriter socketWriter) {
        String[] infos = input.split(" ");
        System.out.println(infos[0] + infos[1]);
        if (infos.length != 2) {
            this.sendNoInput(socketWriter);
        }
        try {
            this.serverInfo.addUser(infos[0], infos[1]);
        } catch (Exception e) {
            this.sendNoInput(socketWriter);
            e.printStackTrace();
        }
        socketWriter.println("SUCESS");
        socketWriter.flush();

    }

    public void login(String input, PrintWriter socketWriter) {
        String[] infos = input.split(" ");
        if (infos.length != 2) {
            this.sendNoInput(socketWriter);
        }
        try {
            if (this.serverInfo.login(infos[0], infos[1])) {
                socketWriter.println("SUCESS LOGGED IN");
                this.loggedUserName = infos[0];
            } else {
                socketWriter.println("ERROR WRONG PASSWORD");
            }
        } catch (Exception e) {
            socketWriter.println("ERROR NO USER");
            e.printStackTrace();
        }
        socketWriter.flush();
    }

    public void publish(String Input, PrintWriter socketWriter) {
        String[] infos = Input.split(" ");
        if (infos.length < 4) {
            this.sendNoInput(socketWriter);
            return;
        }
        String tagList[] = new String[infos.length - 3];
        for (int i = 0; i < (infos.length - 3); i++) {
            tagList[i] = infos[i + 3];
        }
        //Deve agora fazer o download e só depois adicionar a metadata

        this.serverInfo.addFile(infos[0], infos[1], infos[2], tagList);
        socketWriter.println("SUCCESS UPLOADED");
        socketWriter.flush();
    }

    public void search(String input, PrintWriter socketWriter) {
        ArrayList<String> songList = this.serverInfo.SearchOnTag(input);
        for (int i = 0; i < songList.size(); i++) {
            socketWriter.println(songList.get(i));
        }
        socketWriter.println("END");
        socketWriter.flush();
    }

    // Falta respestar as restrições
    // recebe um id, tem se sacar o nome do model ou guardamos com id
    public void download(String fileName, PrintWriter socketWriter) {
        String filePath = new StringBuilder(this.mediaFolderPath).append(fileName).toString();
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

    public void sendNoInput(PrintWriter socketWriter) {
        socketWriter.println("hey");
        socketWriter.flush();
    }

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