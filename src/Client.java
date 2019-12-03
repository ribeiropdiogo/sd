import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {
        /*
        --------> Cenas para efeitos de testes

        Socket s = new Socket("localhost",12345);

        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
        oos.flush();
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));

        System.out.println("> Connected to "+ s.getRemoteSocketAddress());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String str = in.readLine();
        PrintWriter pw = new PrintWriter(s.getOutputStream());

        while (str != null){
            pw.println();
            pw.flush();
            str = in.readLine();
        }

        s.shutdownOutput();
        s.shutdownInput();
        s.close();

         */
    }
}
