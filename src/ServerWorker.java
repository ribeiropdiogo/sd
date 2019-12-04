import Exceptions.UserNotFound;
import Exceptions.WrongPasswordException;
import Exceptions.userExists;

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
                        System.out.println("> Registering user "+ops[1]+" with password "+ops[2]);
                        try {
                            cs.register(ops[1], ops[2]);
                            System.out.println("> User registered with success!");
                            out.println("0");
                        } catch (userExists u){
                            System.out.println("> Username already exists");
                            out.println("1");
                        } catch (Exception e){
                            out.println("-1");
                        } finally {
                            out.flush();
                        }
                        break;
                    case "login":
                        System.out.println("> Attempting to login user "+ops[1]);
                        try {
                            cs.login(ops[1], ops[2]);
                            System.out.println("> "+ops[1]+" logged in");
                            out.println("0");
                        } catch (UserNotFound u){
                            System.out.println("> User not found");
                            out.println("1");
                        } catch (WrongPasswordException w){
                            System.out.println("> User entered wrong password");
                            out.println("2");
                        } catch (Exception e){
                            out.println("-1");
                        } finally {
                            out.flush();
                        }
                        break;
                    default:
                        System.out.println("> Unrecognized operation");
                        break;
                }
                data = in.readLine();
            }

            socket.close();
            out.flush();
            System.out.println("> Connection ended");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
