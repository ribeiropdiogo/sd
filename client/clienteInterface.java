import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clienteInterface {
    public static void main(String[] args) {
        try {
            Socket clsocket = new Socket("127.0.0.1", 12345);
            BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(clsocket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(clsocket.getOutputStream());
            String mediaFolderPath = "./mediaFiles/";
            String stdinLine = new String();
            String serverInputAnswer;
            Boolean exitSwitch = false;

            serverRemote server = new ServerRemote(socketWriter, mediaFolderPath);
            Thread clientReader = new Thread(new clienteReader(socketReader, mediaFolderPath));
            clientReader.run();
        
        } catch (Exception e) {
            System.out.println("Ocorreu um erro");
        }

            while (exitSwitch == false) {
                try{
                System.out.println("Digite a ação pretendida (Help para a lista de ações possiveis)");
                stdinLine = stdinReader.readLine();
                String input[] = stdinLine.split(" ");
                switch (input[0]) {
                case ("Register"):
                    serverInputAnswer=server.register(input[1],input[2]);
                    break;
                case ("Login"):
                    serverInputAnswer=server.login(input[1],input[2]);
                    break;
                case ("Publish"):
                    serverInputAnswer=server.publish(input[1]);
                    break;
                case ("Search"):
                    serverInputAnswer=server.search(input[1]);
                    break;
                case ("Download"):
                    serverInputAnswer=server.download(input[1]);
                    break;
                case ("Quit"):
                    exitSwitch = true;
                    break;
                default:
                serverInputAnswer="Input inválido";
                    break;
                }
                System.out.println(serverInputAnswer);
            }
                
                catch(Exception e){
                    System.out.println("Input inválido, provavelmente por falta de argumentos");
                }

            }

            clsocket.shutdownOutput();
            clsocket.shutdownInput();
            clsocket.close();
    }
}