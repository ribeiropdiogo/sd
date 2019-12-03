import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable{
    private Socket socket;

    public ServerWorker(Socket s) {
        this.socket = s;
    }

    public void run() {
        System.out.println("> New client has established connection from " + socket.getRemoteSocketAddress());

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String data = in.readLine();

            while (data != null && !data.equals("exit")) {

            }

            socket.close();
            System.out.println("> Connection ended");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
