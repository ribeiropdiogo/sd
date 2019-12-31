import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class clienteReader implements Runnable {

    private boolean isOpen;
    private String MediaPath;
    private String onGoingDownload;
    private ArrayList<String> searchList;
    private BufferedReader socketReader;

    public clienteReader(BufferedReader socketReaderIn, String MediaPathIn) {
        this.MediaPath = MediaPathIn;
        this.onGoingDownload = null;
        this.searchList= new ArrayList<String>();
        this.socketReader = socketReaderIn;
        this.isOpen=true;
    }

    public boolean isOpen(){
        return this.isOpen;
    }

    public void processInput(String input) {

        switch (input.substring(0, 3)) {
        case ("REG"):
            this.register(input.substring(4));
            break;
        case ("LOG"):
            this.login(input.substring(4));
            break;
        case ("PUB"):
            this.publish(input.substring(4));
            break;
        case ("SEK"):
            this.search(input.substring(4));
            break;
        case ("DOW"):
            this.download(input.substring(4));
            break;
        case ("NEW"):
            this.notification(input.substring(4));
            break;
        default:
            break;
        }
    }

    public void register(String Input) {
        if (Input.equals("SUCESS")) {
            System.out.println("Registo efetuado com sucesso");
        } else if (Input.equals("ERROR NAME")) {
            System.out.println("Ocorreu um erro durante Registo, o nome é invalido ou já estã em uso");
        } else if (Input.equals("ERROR UNKNOWN")) {
            System.out.println("Ocorreu um erro durante Registo");
        }
    }

    public void login(String Input) {
        if (Input.equals("SUCESS")) {
            System.out.println("Login efetuado com sucesso");
        } else if (Input.equals("ERROR NAME")) {
            System.out.println("Ocorreu um erro durante Login, o nome é invalido");
        } else if (Input.equals("ERROR PASSWORD")) {
            System.out.println("Ocorreu um erro durante Login, a password é incorreta");
        } else if (Input.equals("ERROR UNKNOWN")) {
            System.out.println("Ocorreu um erro durante o pedido de Login");
        }
    }

    public void publish(String Input) {
        if (Input.equals("SUCESS")) {
            System.out.println("Publicação efetuada com sucesso");
        } else if (Input.equals("ERROR UNKNOWN")) {
            System.out.println("Ocorreu um erro durante a Publicação");
        }
    }

    public void search(String Input) {
        if(Input.equals("ERROR NOT LOGGED")){
            System.out.println("Ocorreu um erro a efetuar a procura, não tem sessão inciada");
            return;
        }
        if(Input.equals("END")){
            System.out.println("Lista de musicas que satisfazem a procura:");
            for(String song:this.searchList){
                System.out.println(song);
            }
            this.searchList.clear();
            return;
        }
        else{
            this.searchList.add(Input);
        }
    }

    public void download(String Input) {
        if(Input.equals("END")){
            this.onGoingDownload=null;
            return;
        }

        if (this.onGoingDownload == null) {
            this.onGoingDownload = Input;
        } else {
            try{
            String filePath = new StringBuilder(this.MediaPath).append(this.onGoingDownload+".mp3").toString();
            File file2Upload = new File(filePath);
            FileOutputStream output = new FileOutputStream(filePath, true);
            output.write(Base64.getDecoder().decode(Input));
            output.flush();
            output.close();
            }
            catch(Exception e){
                System.out.println("Ocorreu uma "+e.getClass()+"exception");
            }
        }

    }

    public void notification(String Input){
        System.out.println("Nova música "+ Input);

    } 

    public void run() {
        String Input="";
        while (Input!=null) {
            try {
                Input = this.socketReader.readLine();
                System.out.println("Input is "+Input);
                this.processInput(Input);
            } catch (Exception e) {
                //e.printStackTrace();
            }

        }
        //System.out.println("Leitor de Servidor Fechou");
        this.isOpen=false;
    }
}