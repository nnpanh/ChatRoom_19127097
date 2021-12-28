package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Services extends Thread {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final Socket socket;
    private String username;
    //private ArrayList<String> Clients;
    private final HashMap<String, String> accounts;

    // Constructor
    public Services(Socket socket) throws IOException {
        this.socket = socket;
        //Clients = c;

        //Communicate with client:
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        //Load accounts:
        accounts = new HashMap<>();
        readAccounts();
    }

    private void Login() throws IOException {
        String password;
        boolean isCorrected = false;
        System.out.println("Client logins");
        while (!isCorrected) {
            // Receive user account
            username = input.readLine();
            password = input.readLine();

            String pw = accounts.get(username);
            if (pw.equals(password)) {
                isCorrected = true;
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
        //Clients.add(username);
    }

    private void Register() throws IOException {
        String password, confirm;
        boolean Regis_Success = false;
        System.out.println("Client registers ");

        while (!Regis_Success) {
            //Receive user account
            username = input.readLine();
            password = input.readLine();
            confirm = input.readLine();

            if (!password.equals(confirm)) {
                System.out.println("Failed to register - confirm");
                output.write("false");
                output.newLine();
                output.flush();
            } else {
                //Check account
                String existAccount = accounts.get(username);
                if (existAccount == null)
                    Regis_Success = true;
                if (Regis_Success) {
                    output.write("true");
                    output.newLine();
                    output.flush();
                    System.out.println("User: " + username + " registers successfully!");
                } else {
                    output.write("false");
                    output.newLine();
                    output.flush();
                    System.out.println("Failed to register - account existed");
                }
            }
        }

    }

    private void SignIn_Form() throws IOException {
        //Receive 1: Login, 2: Register
        String op;
        do {
            op = input.readLine();
            output.write(op);
            output.newLine();
            output.flush();
            if (op.equals("1"))
                Login();
            else
                Register();

        } while (!op.equals("1"));
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

    private void Disconnect() {
        System.out.println("Close connection with client: " + socket + "\n");
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
      try {
          Scanner scanner = new Scanner(System.in);
          SignIn_Form();
      } catch (IOException e) {
            e.printStackTrace();
        } finally {
          //Clients.remove(username);
            Disconnect();
        }

    }


}
