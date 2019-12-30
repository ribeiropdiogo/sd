import java.io.PrintWriter;

public class serverRemote{
    
    private PrintWriter socketWriter;
    private String mediaPath;

    public serverRemote(PrintWriter sockWriterIn, String mediaPathIn){
        this.socketWriter=sockWriterIn;
        this.mediaPath=mediaPathIn;
    }

    public void login(){

    }
    public void publish(){

    }
    public void search(){

    }
    public void download(){

    }

}