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

public class clientRemote {

    public static void main(String[] args) throws Exception {

        Socket clsocket = new Socket("127.0.0.1", 12345);

        BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(clsocket.getInputStream()));
        PrintWriter socketWriter = new PrintWriter(clsocket.getOutputStream());

        String stdinLine = new String();
        String ServerOutput = new String();
        String serverAnswer = new String();
       
        File Avthemecpy = new File("avthemecopy2.mp3");
        
        while (serverAnswer != null) {

            try{
                stdinLine = stdinReader.readLine();
                socketWriter.println(stdinLine);
                System.out.println("Wrote " + stdinLine);
                socketWriter.flush();
                System.out.println("flushed " + stdinLine);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage()+" at 1st try");
            }


            try {
                
                ServerOutput = socketReader.readLine();
                byte fileBytes[] = Base64.getDecoder().decode(ServerOutput) ;
                
                System.out.println("Read byte buffer size "+fileBytes.length);
                Files.write(Avthemecpy.toPath(), fileBytes);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage() + " at 2nd try");
            }

        }

        clsocket.shutdownOutput();
        clsocket.shutdownInput();
        clsocket.close();

    }
}