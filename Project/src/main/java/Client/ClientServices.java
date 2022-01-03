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
    public final BufferedReader input;
    public final BufferedWriter output;

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    public ArrayList<String> otherClients = new ArrayList<>();
    public ClientServices(Socket socket) throws IOException {
        this.clientSocket = socket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    public void signInForm() throws InterruptedException, IOException {
        Menu menu = new Menu(this,t);
        menu.run();
    }

    public void ChatRoom() throws IOException {
        ChatRoom chatRoom = new ChatRoom(this,username);
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

    public void run() throws IOException {
        t = new Thread();
        synchronized (t) {
            try {
                signInForm();
                t.wait();
                loadData();
                ChatRoom();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            finally {
                //Tell server to close
                output.write("quit");
                output.newLine();
                output.flush();
                //Close
                input.close();
                output.close();
                clientSocket.close();
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
    }

    public void sendMessage(String text, String user) throws IOException {
        output.write("message "+user+" "+text);
        output.newLine();
        output.flush();
    }
}
