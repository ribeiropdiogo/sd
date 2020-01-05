import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class clienteInterface {
    public static void main(String[] args) {
        try {
            Socket clsocket = new Socket("127.0.0.1", 12345);
            System.out.println("Coneção estabelecida");

            BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(clsocket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(clsocket.getOutputStream());
            socketWriter.flush();
            String mediaFolderPath = "./mediaFiles/";
            String stdinLine = new String();
            String remoteServerInputAnswer="";
            Boolean exitSwitch = false;

            serverRemote server = new serverRemote(socketWriter, mediaFolderPath);

            clienteReader cr = new clienteReader(socketReader, mediaFolderPath);
            Thread clientReader = new Thread(cr);
            clientReader.start();

            System.out.println("Digite a ação pretendida (Help para a lista de ações possiveis)");
            while (exitSwitch == false&&cr.isOpen()) {
                try {
                    stdinLine = stdinReader.readLine();
                    String input[] = stdinLine.split(" ");
                    switch (input[0]) {
                    case ("Register"):
                        remoteServerInputAnswer = server.register(input[1], input[2]);
                        break;
                    case ("Login"):
                        remoteServerInputAnswer = server.login(input[1], input[2]);
                        break;
                    case ("Publish"):
                        String[] newArray = Arrays.copyOfRange(input, 1, input.length);
                        remoteServerInputAnswer = server.publish(newArray);
                        break;
                    case ("Search"):
                        remoteServerInputAnswer = server.search(input[1]);
                        break;
                    case ("Download"):
                        remoteServerInputAnswer = server.download(input[1]);
                        break;
                    case ("Quit"):
                        exitSwitch = true;
                        break;
                    case ("Help"):
                    System.out.println("Register + Nome + Password");  
                    System.out.println("Login + Nome + Password");   
                    System.out.println("Publish +NomeDoFicheiro+ NomeDaMusica + NomeArtista + Ano + Tags");   
                    System.out.println("Search + Tag");   
                    System.out.println("Download + NomeDoFicheiro");
                    System.out.println("Quit");       
                    break; 
                    default:
                        remoteServerInputAnswer = "Input inválido";
                        break;
                    }
                    System.out.println(remoteServerInputAnswer);
                }

                catch (Exception e) {
                    System.out.println("Input inválido, provavelmente por falta de argumentos");
                }

            }
            if(exitSwitch==true){
                System.out.println("Quit recebido, saindo.");
            }
            else if(cr.isOpen()==false){
                System.out.println("Leitor do Servidor fechou, possivelmente porque o servidor foi desligado, o programa irá encerrar.");
            }

            clsocket.shutdownOutput();
            clsocket.shutdownInput();
            clientReader.join();
            clsocket.close();
        } catch (Exception e) {
            System.out.println("Ocorreu um erro, possivelmente o servidor não se encontra ativo.");
            //e.printStackTrace();
        }
    }
}