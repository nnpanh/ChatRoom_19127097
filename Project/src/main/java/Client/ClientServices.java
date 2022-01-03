package Client;

import Client.UI.ChatRoom;
import Client.UI.Menu;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientServices{
    public Thread t;
    private final Socket clientSocket;
    private final BufferedReader input;
    private final BufferedWriter output;
    public ArrayList<String> otherClients = new ArrayList<>();
    public ClientServices(Socket socket) throws IOException {
        this.clientSocket = socket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    public void signInForm() throws InterruptedException, IOException {
        Menu menu = new Menu(clientSocket,t);
        menu.run();
    }

    public void ChatRoom() throws IOException {
        ChatRoom chatRoom = new ChatRoom(this);
        chatRoom.run();
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
        synchronized (t) {
            try {
                signInForm();
                t.wait();
                loadData();
                //t.wait();
                ChatRoom();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() throws IOException {
        output.write("load");
        output.newLine();
        output.flush();
        String receivedMessage = input.readLine();
        System.out.println(receivedMessage);
        int users = Integer.parseInt(receivedMessage);
        for(int i=0;i<users;i++){
            receivedMessage = input.readLine();
            otherClients.add(receivedMessage);
        }
        synchronized (t){
          //  t.notifyAll();
        }
    }
}
