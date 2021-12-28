package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    final ServerSocket server;
   // private final ExecutorService pool = Executors.newFixedThreadPool(5);
    private ArrayList<String> clients;

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
                Services ClientHandler = new Services(socket);
                ClientHandler.start();
                //Add to pool thread
               // pool.execute(ClientHandler);

            } catch (IOException e) {
                if (socket != null)
                    socket.close();
                if (!server.isClosed()) server.close();
                System.out.println("Connection closed: " + socket);
                e.printStackTrace();
            }
        }
    }
}