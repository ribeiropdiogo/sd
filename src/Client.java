import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private CloudSoundnterface csi;

    public static void main(String[] args) throws InterruptedException, IOException {
        try {
            CloudSoundnterface csi = new RemoteCloudSound("localhost",12345);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String command = reader.readLine();


            while (command != null && !command.equals("exit")){
                switch (command){
                    case "register":
                        String[] info = register();
                        csi.register(info[0],info[1]);
                        break;
                    case "help":
                        System.out.println("> List of available commands");
                        System.out.println("register   register user in system");
                        System.out.println("login      login in system");
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

    public static String[] register(){
        Scanner input = new Scanner(System.in);

        System.out.print("username: ");
        String username = input.nextLine();
        System.out.print("password: ");
        String password = input.nextLine();

        String[] info = new String[2];
        info[0] = username;
        info[1] = password;

        return info;
    }

    public static void login(){
        System.out.println("username: ");

        System.out.println("password: ");
    }
}
