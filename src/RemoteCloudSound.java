import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class RemoteCloudSound implements CloudSoundnterface {
    private Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public RemoteCloudSound(String host, int port) throws IOException {
        this.socket = new Socket(InetAddress.getByName(host),port);
        System.out.println("> Connected to "+ socket.getRemoteSocketAddress());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }

    public int register(String username, String password){
        out.println("register "+username+" "+password);
        out.flush();
        int r = 0;
        try {
            r = Integer.parseInt(in.readLine());
        } catch (IOException e){
            e.printStackTrace();
        }

        return r;
    }
}
