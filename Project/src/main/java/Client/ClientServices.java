package Client;

import Client.UI.Login;
import Client.UI.Menu;
import Client.UI.SignUp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientServices{
    public Thread t;
    private final Socket clientSocket;
    private final BufferedReader input;
    private final BufferedWriter output;
    private ArrayList<String> otherClients;
    public ClientServices(Socket socket) throws IOException {
        this.clientSocket = socket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    public void Menu() throws InterruptedException, IOException {
        Menu menu = new Menu(clientSocket,t);
        menu.run();
    }

    public void ChatRoom() throws IOException {
        do
        {
            System.out.println("Welcome");
            String sentMessage;
            String receivedMessage;
            Scanner scanner = new Scanner(System.in);
            sentMessage=scanner.nextLine();
            output.write(sentMessage);
            output.newLine();
            output.flush();

            if (sentMessage.equalsIgnoreCase("quit"))
                break;
            else
            {
                receivedMessage=input.readLine();
                System.out.println("Received : " + receivedMessage);
            }

        }
        while(true);
    }

    public void run() {
        t = new Thread();
        try {
            Menu();
            t.suspend();
            ChatRoom();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}
