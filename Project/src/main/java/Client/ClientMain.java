package Client;

import Client.UI.Login;
import Client.UI.SignUp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        Socket clientSocket = new Socket("127.0.0.1", 8000);
        Login signUp = new Login(clientSocket);
        signUp.run();
        clientSocket.close();
        //s.run();
    }
}
