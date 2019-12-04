import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static CloudSoundnterface csi;
    private static BufferedReader reader;

    public static void main(String[] args) throws InterruptedException, IOException {
        try {
            csi = new RemoteCloudSound("localhost",12345);
            reader = new BufferedReader(new InputStreamReader(System.in));

            String command = reader.readLine();


            while (command != null && !command.equals("exit")){
                switch (command){
                    case "register":
                        // Existe um bug! - qd os campos estão por preencher o ServerWorker estoura
                        register();
                        break;
                    case "login":
                        login();
                        break;
                    case "help":
                        help();
                        break;
                    default:
                        System.out.println("> Invalid Command");
                        break;
                }
                command = reader.readLine();
            }

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            System.out.println("> Shutting down");
        }
    }

    public static void register(){

        try{
            System.out.print("username: ");
            String username = reader.readLine();
            System.out.print("password: ");
            String password = reader.readLine();

            int r = csi.register(username,password);

            if (r == 0){
                System.out.println("> User registered with success!");
            } else if (r == 1){
                System.out.println("> Username already in use, try again.");
            } else {
                System.out.println("> Ups, something did not work :(");
            }
        } catch (IOException e){
            System.out.println("> An IO Exception has occured!");
        }
    }

    public static void login(){

        try{
            System.out.print("username: ");
            String username = reader.readLine();
            System.out.print("password: ");
            String password = reader.readLine();

            int r = csi.login(username,password);

            if (r == 0){
                System.out.println("> Access granted");
            } else if (r == 1) {
                System.out.println("> Username not found");
            } else if (r == 2){
                System.out.println("> Wrong password");
            } else {
                System.out.println("> Ups, something did not work :(");
            }
        } catch (IOException e){
            System.out.println("> An IO Exception has occured!");
        }
    }

    public static void help(){
        System.out.println("> List of available commands");
        System.out.println("register   register user in system");
        System.out.println("login      access the system");
    }
}
