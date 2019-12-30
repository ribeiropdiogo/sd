import java.io.PrintWriter;

public class serverRemote{
    
    private PrintWriter socketWriter;
    private String mediaPath;

    public serverRemote(PrintWriter sockWriterIn, String mediaPathIn){
        this.socketWriter=sockWriterIn;
        this.mediaPath=mediaPathIn;
    }

    public String register(String Nome, String Password){
        return "";
    }

    public String login(String Nome, String Password){
        return "";
    }
    public String publish(String filename){
        return "";
    }
    public String search(String Tag){
        return "";
    }
    public String download(String FileName){
        return "";
    }

}