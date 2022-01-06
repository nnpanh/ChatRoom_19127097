package Client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 8002);
        if (clientSocket.isConnected()) {
            ClientServices client = new ClientServices(clientSocket);
            client.run();
        }
    }
}
