package Client;

import Client.UI.Login;
import Client.UI.Menu;
import Client.UI.SignUp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {

        Socket clientSocket = new Socket("localhost", 8001);
        if (clientSocket.isConnected()) {
            ClientServices client = new ClientServices(clientSocket);
            client.run();
        }
    }
}
