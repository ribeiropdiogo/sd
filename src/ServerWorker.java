import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable{
    private Socket socket;
    private CloudSound cs;

    public ServerWorker(Socket s, CloudSound clouds) {
        this.socket = s;
        this.cs = clouds;
    }

    public void run() {
        System.out.println("> New client has established connection from " + socket.getRemoteSocketAddress());

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            String data = in.readLine();

            while (data != null && !data.equals("exit")) {
                String[] ops = data.split(" ");
                switch (ops[0]){
                    case "register":
                        System.out.println("> Registering client "+data);
                        try {
                            cs.register(ops[1],ops[2]);
                            out.println(0);
                        } catch (Exception e){
                            out.println(1);
                        }
                        break;
                    default:
                        System.out.println("> Unrecognized operation");
                        break;
                }
                data = in.readLine();
            }

            socket.close();
            System.out.println("> Connection ended");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
