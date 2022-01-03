package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    final ServerSocket server;
    private ArrayList<ServerServices> clients = new ArrayList<>();

    public ArrayList<ServerServices> getClients() {
        return clients;
    }

    public ArrayList<String> getOnlineUser(){
        ArrayList<String> user = new ArrayList<>();
        for (ServerServices client: clients
             ) {
            user.add(client.getLogin());
        }
        return user;
    }

    //Establish a connection between server and clients
    public Server(int port) throws IOException {
        server = new ServerSocket(port);
        Connect();
    }

    public void Connect() throws IOException {
        System.out.println("Waiting for clients.... \n ");
        while (server.isBound() && !server.isClosed()) {
            Socket socket = null;
            try {
                socket = server.accept();

                System.out.println("A new client connected : " + socket + "\n");
                // create a new thread object
                ServerServices ClientHandler = new ServerServices(this, socket);
                clients.add(ClientHandler);
                ClientHandler.start();
                //Add to pool thread
               // pool.execute(ClientHandler);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
