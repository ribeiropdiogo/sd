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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class serverWorker implements Runnable {
    private String loggedUserName;
    private String mediaFolderPath;
    private Socket socket;
    private model serverInfo;

    public serverWorker(Socket cSock, model serverInfoIn, String mediaFolderIn) {
        this.serverInfo = serverInfoIn;
        this.socket = cSock;
        this.mediaFolderPath = mediaFolderIn;
        this.loggedUserName = "";
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
            this.publish(input.substring(4), socketWriter, socketReader);
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
            //check duplicate user
            this.serverInfo.addUser(infos[0], infos[1]);
            socketWriter.println("REG SUCESS");
            socketWriter.flush();
        } catch (Exception e) {
            socketWriter.println("REG ERROR UNKNOWN");
            socketWriter.flush();
            e.printStackTrace();
        }

    }

    public void login(String input, PrintWriter socketWriter) {
        String[] infos = input.split(" ");
        if (infos.length != 2) {
            this.sendNoInput(socketWriter);
        }
        try {
            //check name
            if (this.serverInfo.login(infos[0], infos[1])) {
                socketWriter.println("LOG SUCESS");
                socketWriter.flush();
                this.loggedUserName = infos[0];
            } else {
                socketWriter.println("LOG ERROR PASSWORD");
                socketWriter.flush();
            }
        } catch (Exception e) {
            socketWriter.println("LOG ERROR UNKNOWN");
            socketWriter.flush();
            e.printStackTrace();
        }
    }

    public void publish(String Input, PrintWriter socketWriter, BufferedReader socketReader) {
        if (this.loggedUserName == "") {
            socketWriter.println("PUB ERROR NOT LOGGED");
            return;
        }

        String encodedString;
        String[] infos = Input.split(" ");
        if (infos.length < 4) {
            this.sendNoInput(socketWriter);
            return;
        }
        String tagList[] = new String[infos.length - 3];
        for (int i = 0; i < (infos.length - 3); i++) {
            tagList[i] = infos[i + 3];
        }

        String filePath = new StringBuilder(this.mediaFolderPath).append(this.serverInfo.getNextFileNString())
                .toString();
        System.out.println(filePath);
        File file2Upload = new File(filePath);

        try {
            FileOutputStream output = new FileOutputStream(filePath, true);
            while ((encodedString = socketReader.readLine()).equals("PUB END") == false) {

                output.write(Base64.getDecoder().decode(encodedString));
                output.flush();

            }
            output.close();
            this.serverInfo.addFile(infos[0], infos[1], infos[2], tagList);
            socketWriter.println("PUB SUCCESS");
            socketWriter.flush();
        } catch (Exception e) {
            socketWriter.println("PUB ERROR UNKNOWN");
            socketWriter.flush();
            e.printStackTrace();
            file2Upload.delete();
        }
        
    }

    public void search(String input, PrintWriter socketWriter) {
        if (this.loggedUserName == "") {
            socketWriter.println("SEK ERROR NOT LOGGED");
            return;
        }
        ArrayList<String> songList = this.serverInfo.SearchOnTag(input);
        for (int i = 0; i < songList.size(); i++) {
            socketWriter.println("SEK "+songList.get(i));
        }
        socketWriter.println("SEK END");
        socketWriter.flush();
    }

    // Falta respestar as restrições
    // recebe um id, tem se sacar o nome do model ou guardamos com id
    public void download(String fileId, PrintWriter socketWriter) {
        if (this.loggedUserName == "") {
            socketWriter.println("ERROR NOT LOGGED");
            return;
        }
        int fileNumber=Integer.parseInt(fileId);
        String filePath = new StringBuilder(this.mediaFolderPath).append(fileNumber).toString();
        String fileName = new StringBuilder(this.serverInfo.getFileTitle(fileNumber)).append(" "+serverInfo.getFileArtist(fileNumber)).toString();
        System.out.println(filePath);

        //QUEUE
        this.serverInfo.ldManager.waitDownload(this.serverInfo.ldManager.getTicket());
        
        try {
            Thread.sleep(5000);
            socketWriter.println("DOW "+fileName);
            socketWriter.flush();
            File file2download = new File(filePath);

            FileInputStream FileReader=new FileInputStream(file2download);
            byte fileBytes[]=new byte[this.serverInfo.getMAXSIZE()];
            byte fileBytesRead[];
            String encodedString;
            int readBytesN=0;
            while((readBytesN=FileReader.read(fileBytes,0, this.serverInfo.getMAXSIZE()))>0){
                fileBytesRead=Arrays.copyOfRange(fileBytes,0,readBytesN);
                encodedString = Base64.getEncoder().encodeToString(fileBytesRead);
                socketWriter.println("DOW "+encodedString);
                socketWriter.flush();
                System.out.println(encodedString);
            }
            socketWriter.println("DOW END");
            socketWriter.flush();
            FileReader.close();
            //byte[] fileBytes = Files.readAllBytes(file2download.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error writing" + e.getLocalizedMessage());
        }
        this.serverInfo.ldManager.freeDownload();

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
            new Thread(new notificationWorker(this.serverInfo, socketWriter)).start();
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