import java.io.BufferedReader;

public class clienteReader implements Runnable{

private String MediaPath;    
private String onGoingDownload;    
private BufferedReader socketReader;

public clienteReader(BufferedReader socketReaderIn,String MediaPathIn){
    this.MediaPath=MediaPathIn;
    this.onGoingDownload=null;
    this.socketReader=socketReaderIn;
}

public void run(){
    String Input;
    while(true){
        try{
        Input=this.socketReader.readLine();
    }
    catch(Exception e){
        e.printStackTrace();
    }

    }
}
}