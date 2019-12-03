import java.net.Socket;

public class ServerWorker implements Runnable{
    private Socket socket;

    public ServerWorker(Socket s) {
        this.socket = s;
    }

    public void run() {
        System.out.println("> Client has established connection from " + socket.getRemoteSocketAddress());
    }
}
