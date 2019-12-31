import java.io.PrintWriter;

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

    public String publish(String filename) {
        return "";
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