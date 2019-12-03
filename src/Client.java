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

            String[] info = register();
            csi.register(info[0],info[1]);

        } catch (Exception e){
            e.printStackTrace();
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
