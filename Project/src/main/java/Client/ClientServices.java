package Client;

import Client.UI.ChatRoom;
import Client.UI.Menu;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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

    public void ChatRoom() throws IOException, InterruptedException {
        ChatRoom chatRoom = new ChatRoom(this,username);
        chatRoom.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                   disconnect();
                   System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        chatRoom.run();
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
        }
    }

    public void disconnect() throws IOException {
        if(!clientSocket.isClosed()) {
            System.out.println("Client closed");
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

    public void sendFile(File file, String sendTo) throws IOException {
        output.write("file "+sendTo+" "+file.length()+" "+file.getName());
        output.newLine();
        output.flush();

        FileInputStream fileInputStream = new FileInputStream(file);
        OutputStream outputStream = clientSocket.getOutputStream();
        // break file into chunks
        byte[] buffer = new byte[4 * 1024];
        int bytes;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
            outputStream.flush();
        }
        fileInputStream.close();
    }

    public void receivedFile(String fileSize, String fileName) throws IOException {
        System.out.println("Receiving files");

        int bytes = 0;
        File file;
        File folder = new File("Project/resource/Client/" + username+"/");
        if (!folder.isDirectory()){
            if(folder.mkdirs())
            System.out.println("Folder created");
        }
        file = new File("Project/resource/Client/" + username+"/"+fileName);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        InputStream inputStream = clientSocket.getInputStream();
        long size = Long.parseLong(fileSize);

        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = inputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
        System.out.println("File received from server");

    }
}
