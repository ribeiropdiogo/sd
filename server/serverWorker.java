import model.*;

import exceptions.duplicateUserException;
import exceptions.noSuchUserException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

        if (input.length() < 5) {
            this.sendNoInput(socketWriter);
            return;
        }

        switch (input.substring(0, 3)) {

        case ("REG"):
            System.out.println("Received Register Request");
            this.register(input.substring(4), socketWriter);
            System.out.println("Register Request Done");
            break;
        case ("LOG"):
            System.out.println("Received Login Request");
            this.login(input.substring(4), socketWriter);
            System.out.println("Login Request Done");
            break;
        case ("PUB"):
            System.out.println("Received Publish Request");
            this.publish(input.substring(4), socketWriter, socketReader);
            System.out.println("Publish Request Done");
            break;
        case ("SEK"):
            System.out.println("Received Search Request");
            this.search(input.substring(4), socketWriter);
            System.out.println("Search Request Done");
            break;
        case ("DOW"):
            System.out.println("Received Download Request");
            this.download(input.substring(4), socketWriter);
            System.out.println("Download Request Done");
            break;
        default:
            System.out.println("Received Improper Input");
            this.sendNoInput(socketWriter);
            System.out.println("Improper Input Answered");
            break;
        }
    }

    public void register(String input, PrintWriter socketWriter) {
        
        String[] infos = input.split(" ");

        if (infos.length != 2) {
            this.sendNoInput(socketWriter);
        }
        try {
            this.serverInfo.addUser(infos[0], infos[1]);
            socketWriter.println("REG SUCESS");
            socketWriter.flush();
        } catch (duplicateUserException e) {
            socketWriter.println("REG ERROR NAME");
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
            if (this.serverInfo.login(infos[0], infos[1])) {
                socketWriter.println("LOG SUCESS");
                socketWriter.flush();
                this.loggedUserName = infos[0];
            } else {
                socketWriter.println("LOG ERROR PASSWORD");
                socketWriter.flush();
            }
        } catch (noSuchUserException e) {
            socketWriter.println("LOG ERROR NAME");
            socketWriter.flush();
            e.printStackTrace();
        } catch (Exception e) {
            socketWriter.println("LOG ERROR UNKNOWN");
            socketWriter.flush();
            e.printStackTrace();
        }
    }

    public void publish(String Input, PrintWriter socketWriter, BufferedReader socketReader) {

        if (this.loggedUserName == "") {
            try {
                // Limpar o socketReader
                while (socketReader.readLine().equals("PUB END") == false) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            socketWriter.println("PUB ERROR NOT LOGGED");
            socketWriter.flush();
            return;
        }

        String encodedString;
        String[] metaData = Input.split(" ");

        if (metaData.length < 4) {
            this.sendNoInput(socketWriter);
            return;
        }
        String tagList[] = new String[metaData.length - 3];
        for (int i = 0; i < (metaData.length - 3); i++) {
            tagList[i] = metaData[i + 3];
        }

        String filePath = new StringBuilder(this.mediaFolderPath).append(this.serverInfo.getNextFileNString())
                .toString();
        File file2Upload = new File(filePath);

        // Elimina o ficheiro se já existir, se já cá está sem metadados, é lixo
        file2Upload.delete();

        try {
            FileOutputStream output = new FileOutputStream(filePath, true);
            while ((encodedString = socketReader.readLine()).equals("PUB END") == false) {

                output.write(Base64.getDecoder().decode(encodedString));
                output.flush();

            }
            output.close();
            this.serverInfo.addFile(metaData[0], metaData[1], metaData[2], tagList);
            socketWriter.println("PUB SUCCESS");
            socketWriter.flush();
        } catch (Exception e) {
            socketWriter.println("PUB ERROR UNKNOWN");
            socketWriter.flush();
            e.printStackTrace();
            // Elimina o ficheiro em caso de erro
            file2Upload.delete();
        }

    }

    public void search(String input, PrintWriter socketWriter) {
        if (this.loggedUserName == "") {
            socketWriter.println("SEK ERROR NOT LOGGED");
            socketWriter.flush();
            return;
        }
        ArrayList<String> songList = this.serverInfo.SearchOnTag(input);
        for (int i = 0; i < songList.size(); i++) {
            socketWriter.println("SEK " + songList.get(i));
        }
        socketWriter.println("SEK END");
        socketWriter.flush();
    }

    public void download(String fileId, PrintWriter socketWriter) {
        if (this.loggedUserName == "") {
            socketWriter.println("DOW ERROR NOT LOGGED");
            socketWriter.flush();
            return;
        }

        int fileNumber = Integer.parseInt(fileId);
        if (this.serverInfo.getFileTitle(fileNumber) == null) {
            socketWriter.println("DOW ERROR NO FILE");
            socketWriter.flush();
            return;
        }

        String filePath = new StringBuilder(this.mediaFolderPath).append(fileNumber).toString();
        String fileName = new StringBuilder(this.serverInfo.getFileTitle(fileNumber))
                .append("-" + serverInfo.getFileArtist(fileNumber)).toString();

        // QUEUE
        this.serverInfo.ldManager.waitDownload(this.serverInfo.ldManager.getTicket());

        try {
            // Para Testes
            // Thread.sleep(5000);
            socketWriter.println("DOW " + fileName);
            socketWriter.flush();
            File file2download = new File(filePath);

            FileInputStream FileReader = new FileInputStream(file2download);
            byte fileBytes[] = new byte[this.serverInfo.getMAXSIZE()];
            byte fileBytesRead[];
            String encodedString;
            int readBytesN = 0;
            while ((readBytesN = FileReader.read(fileBytes, 0, this.serverInfo.getMAXSIZE())) > 0) {
                fileBytesRead = Arrays.copyOfRange(fileBytes, 0, readBytesN);
                encodedString = Base64.getEncoder().encodeToString(fileBytesRead);
                socketWriter.println("DOW " + encodedString);
                socketWriter.flush();
            }
            socketWriter.println("DOW END");
            socketWriter.flush();
            FileReader.close();
            this.serverInfo.addDownloadToFile(fileNumber);
            this.serverInfo.ldManager.freeDownload();
        } catch (Exception e) {
            e.printStackTrace();
            this.serverInfo.ldManager.freeDownload();
        }

    }

    public void sendNoInput(PrintWriter socketWriter) {
        socketWriter.println("DATA TRANSFER ERROR");
        socketWriter.flush();
    }

    public void run() {
        try {

            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(this.socket.getOutputStream());
            notificationWorker notWorker = new notificationWorker(this.serverInfo, socketWriter);
            Thread notWorkerThread = new Thread(notWorker);
            notWorkerThread.start();
            String s = null;

            while ((s = socketReader.readLine()) != null) {
                System.out.println("Client sent " + s);
                this.processInput(s, socketReader, socketWriter);
            }
            
            //Para que o notWorker pare quando acordar
            notWorker.clientOff();

            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
