package Server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server main = new Server("127.0.0.1",8000);
        main.run();

    }
}
