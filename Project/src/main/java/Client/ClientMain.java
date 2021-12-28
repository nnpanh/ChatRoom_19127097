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

        Socket clientSocket = new Socket("localhost", 8000);
        if (clientSocket.isConnected()) {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String command;
            System.out.println("Connected");
            do {
                Menu menu = new Menu(clientSocket);
                menu.run();
                System.out.println("Picking");
                command = input.readLine();
                System.out.println(command);

                switch (command) {
                    case "1" -> {
                        new Login(clientSocket).run();
                    }
                    case "2" -> {
                        new SignUp(clientSocket).run();
                    }
                    default -> System.out.println("ERROR in SIGNIN flag");
                }
            } while (!command.equals("1"));

            clientSocket.close();
        }
    }
}
