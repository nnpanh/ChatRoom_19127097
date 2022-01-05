package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ServerServices extends Thread {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final Socket socket;
    private String username = null;
    private final Server server;
    private final HashMap<String, String> accounts;

    // Constructor
    public ServerServices(Server server, Socket socket) throws IOException {
        this.socket = socket;
        this.server = server;

        //Communicate with client:
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        //Load accounts:
        accounts = new HashMap<>();
        readAccounts();
    }

    public String getLogin() {
        return username;
    }

    private void handleLogin(String user, String password) throws IOException {
        System.out.println("Client logins");
        username = user;
        boolean loginFlag = false;
        for (String otherClients : server.getOnlineUser(this)
        ) {
            System.out.println("1:" + otherClients);
            if (otherClients.equals(username)) {
                output.write("logged");
                output.newLine();
                output.flush();
                loginFlag = true;
                System.out.println("Client already logged");
            }
        }
        if (!loginFlag) {
            String pw = accounts.get(username);
            if (pw.equals(password)) {
                output.write("true");
                output.newLine();
                output.flush();
                System.out.println("Client login successfully");
                //Notify other clients:
                for (ServerServices clients : server.getClients()
                ) {
                    if (clients != this && clients.getLogin() != null)
                        clients.sendMessage("new " + username);
                }
            } else {
                output.write("false");
                output.newLine();
                output.flush();
                System.out.println("Client login failed");
            }
        }

    }

    private void handleRegister(String username, String password, String confirm) throws IOException {
        System.out.println("Client registers ");
        if (!password.equals(confirm)) {
            System.out.println("Failed to register - confirm");
            output.write("false");
            output.newLine();
            output.flush();
        } else {
            //Check account
            String existAccount = accounts.get(username);
            if (existAccount == null) {
                output.write("true");
                output.newLine();
                output.flush();
                System.out.println("User: " + username + " registers successfully!");
                saveAccount(username, password);
            } else {
                output.write("false");
                output.newLine();
                output.flush();
                System.out.println("Failed to register - account existed");
            }
        }


    }

    private void readAccounts() {
        try {
            FileInputStream fileInputStream = new FileInputStream("Project/resource/account.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, "`");
                if (tokenizer.countTokens() == 2) {
                    accounts.put(tokenizer.nextToken(), tokenizer.nextToken());
                }
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loading data complete");
    }

    private void saveAccount(String username, String password) {
        accounts.put(username, password);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("Project/resource/account.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            accounts.forEach((String k, String v) -> {
                try {
                    bufferedWriter.write(k + "`" + v);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            boolean stopFlag=false;
            String receivedMessage;
            do {
                receivedMessage = input.readLine();
                System.out.println("Received : " + receivedMessage);

                String[] token = receivedMessage.split(" ", 4);
                switch (token[0]) {
                    case "login" -> handleLogin(token[1], token[2]);
                    case "register" -> handleRegister(token[1], token[2], token[3]);
                    case "message" -> {
                        if (token.length == 4) token[2] = token[2] + " " + token[3];
                        handleMessage(token[1], token[2]);
                    }
                    case "load" -> handleLoad();
                    case "file" -> {
                        handleFile(token[1], token[2], token[3]);
                    }
                    case "quit"->{
                        handleLogOut();
                        stopFlag=true;
                    }

                    default -> System.out.println(token[0]);

                }
            }
            while (!stopFlag);

        } catch (IOException | InterruptedException e) {
            try {
                System.out.println("Client has closed");
                server.remove(this);
                socket.close();
                input.close();
                output.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleLogOut() throws IOException {
        sendMessage("close");
        for (ServerServices otherClient : server.getClients()){
            otherClient.sendMessage("quit "+username);
        }
    }

    private void handleMessage(String sendTo, String message) throws IOException {
        for (ServerServices otherClient : server.getClients()
        ) {
            if (otherClient.getLogin().equals(sendTo)) {
                otherClient.sendMessage("message " + username + " " + message);
            }
        }
    }

    private void handleLoad() throws IOException {
        //Send number of current online user
        ArrayList<String> onlineUser = server.getOnlineUser(this);
        output.write(String.valueOf(onlineUser.size()));
        output.newLine();
        output.flush();

        for (String s : onlineUser) {
            if (s != null) {
                output.write(s);
                output.newLine();
                output.flush();
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
    }

    private void handleFile(String sendTo, String fileSize, String fileName) throws IOException, InterruptedException {
        //Save file temp
        receivedFile(username, fileSize);
        //Send to other client

        for (ServerServices otherClient : server.getClients()
        ) {
            if (otherClient.getLogin().equals(sendTo)) {
                System.out.println("Send file to " + otherClient.getLogin());
                otherClient.sendMessage("file " + fileSize + " " + fileName +" "+username);
                otherClient.sendFile();
                break;
            }
        }
    }

    private void sendFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("Project/resource/temp/"+username);
        OutputStream outputStream = socket.getOutputStream();
        // break file into chunks
        byte[] buffer = new byte[4 * 1024];
        int bytes;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytes);
            outputStream.flush();
        }
        fileInputStream.close();
        System.out.println("File transfer complete");
    }

    private void receivedFile(String username, String fileSize) throws IOException {
        System.out.println("Sending files");

        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream("Project/resource/temp/" + username);
        InputStream inputStream = socket.getInputStream();
        long size = Long.parseLong(fileSize);

        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = inputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;      // read upto file size
        }
        fileOutputStream.close();
        System.out.println("File " + "downloaded from " +this.username);
    }


}
