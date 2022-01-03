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
    private String username;
    private  Server server;
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

    public String getLogin(){
        return username;
    }

    private void handleLogin(String user,String password) throws IOException {
        System.out.println("Client logins");
        username=user;
            boolean loginFlag = false;
            for (ServerServices client: server.getClients()
            ) {
                if (client!=this&&client.getLogin().equals(username)){
                    output.write("logged");
                    output.newLine();
                    output.flush();
                    loginFlag=true;
                }
            }
            if (!loginFlag) {
                String pw = accounts.get(username);
                if (pw.equals(password)) {
                    output.write("true");
                    output.newLine();
                    output.flush();
                } else {
                    output.write("false");
                    output.newLine();
                    output.flush();
                }
            }

        System.out.println("User: " + username + " logins successfully!");

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
                }
                else {
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
                    bufferedWriter.write(k+"`"+v);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bufferedWriter.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
      try {
          String receivedMessage;
          do
          {
              receivedMessage=input.readLine();
              System.out.println("Received : " + receivedMessage);
              if (receivedMessage.equalsIgnoreCase("quit"))
              {
                  System.out.println("Client has left !");
                  break;
              }
              else
              {
                  String[] token = receivedMessage.split(" ",4);
                  switch (token[0]) {
                      case "login" -> handleLogin(token[1],token[2]);
                      case "register" -> handleRegister(token[1], token[2], token[3]);
                      case "message" -> System.out.println("Chat room start");
                      case "load" -> handleLoad();
                      default -> System.out.println(token[0]);
                  }
              }
          }
          while (true);
          input.close();
          output.close();
      } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLoad() throws IOException {
        //Send number of current online user
        ArrayList<String> onlineUser = server.getOnlineUser();
        output.write(String.valueOf(onlineUser.size()));
        output.newLine();
        output.flush();
        System.out.println(String.valueOf(onlineUser.size()));
        for (int i=0;i<onlineUser.size();i++) {
            output.write(onlineUser.get(i));
            output.newLine();
            output.flush();
        }
    }


}
