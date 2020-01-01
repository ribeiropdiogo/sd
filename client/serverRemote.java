import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;
import java.io.File;
import java.io.FileInputStream;

public class serverRemote {

    private PrintWriter socketWriter;
    private String mediaPath;

    public serverRemote(PrintWriter sockWriterIn, String mediaPathIn) {
        this.socketWriter = sockWriterIn;
        this.mediaPath = mediaPathIn;
    }

    public String register(String Nome, String Password) {
        try {
            this.socketWriter.println("REG " + Nome + " " + Password);
            this.socketWriter.flush();
            return "Pedido de Registo Efetuado";
        } catch (Exception e) {
            return "Ocorreu um erro ao efetuar o pedido de Registo";
        }
    }

    public String login(String Nome, String Password) {
        try {
            this.socketWriter.println("LOG " + Nome + " " + Password);
            this.socketWriter.flush();
            return "Pedido de Login Efetuado";
        } catch (Exception e) {
            return "Ocorreu um erro ao efetuar o pedido de Login";
        }
        
    }

    public String publish(String[] parametros) {
        try {
            String params = String.join(" ", parametros);
            this.socketWriter.println("PUB " + params);
            this.socketWriter.flush();

            String filename = parametros[0]+".mp3";
            File file2publish = new File(filename);

            FileInputStream FileReader=new FileInputStream(file2publish);
            byte fileBytes[]=new byte[1000000];
            byte fileBytesRead[];
            String encodedString;
            int readBytesN=0;
            while((readBytesN=FileReader.read(fileBytes,0, 1000000))>0){
                fileBytesRead=Arrays.copyOfRange(fileBytes,0,readBytesN);
                encodedString = Base64.getEncoder().encodeToString(fileBytesRead);
                socketWriter.println(encodedString);
                socketWriter.flush();
                System.out.println(encodedString);
            }
            socketWriter.println("PUB END");
            socketWriter.flush();
            FileReader.close();

            return "Pedido de Publicação Efetuado";
        } catch (Exception e) {
            return "Ocorreu um erro ao efetuar o pedido de Publicação";
        }
    }

    public String search(String Tag) {
        try {
            this.socketWriter.println("SEK " + Tag);
            this.socketWriter.flush();
            return "Pedido de Procura Efetuado";
        } catch (Exception e) {
            return "Ocorreu um erro ao efetuar o pedido de Procura";
        }
    }
    
    public String download(String FileName) {
        try {
            this.socketWriter.println("DOW " + FileName);
            this.socketWriter.flush();
            return "Pedido de Download Efetuado";
        } catch (Exception e) {
            return "Ocorreu um erro ao efetuar o pedido de Download";
        }
    }

}