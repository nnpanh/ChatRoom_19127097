package Server;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Services extends Thread {
    final DataInputStream in; //Take message from client
    final DataOutputStream out; //Print on client terminal
    final ObjectOutputStream oos;
    final Socket socket;
    private String username;
    private ArrayList<String> Clients;
    private String bookName;

    // Constructor
    public Services(Socket s, ArrayList<String> c) throws IOException {
        socket = s;
        Clients = c;

        //Communicate with client:
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        oos = new ObjectOutputStream(out);

    }

    private void Login() throws IOException {
        String password;
        Boolean isCorrected = false;
        System.out.println("Client logins");
        while (!isCorrected) {
            // Receive user account
            username = in.readUTF();
            password = in.readUTF();

            if(username.equals("guest") && (password.equals("123")))
                isCorrected=true;
            out.writeBoolean(isCorrected);
        }
        System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                + "] User: " + username + " logins successfully!");
        Clients.add(username);
    }

    private void Register() throws IOException {
        String password, confirm;
        Boolean Regis_Success = false;
        System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                + "] Client registers ");

       // DataHandler dataHandler = new DataHandler();

        while (!Regis_Success) {
            //Receive user account
            username = in.readUTF();
            password = in.readUTF();
            confirm = in.readUTF();

            if (!password.equals(confirm)) {
                System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                        + "] Failed to register");
                out.writeBoolean(false);

            } else {
               // Regis_Success = dataHandler.Register(username, password);
                out.writeBoolean(Regis_Success); //Gui ve ham regis cua client xu ly
                if (Regis_Success)
                    System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                            + "] User: " + username + " registers successfully!");
                else
                    System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                            + "] Failed to register");
            }
        }

    }

    private void SignIn_Form() throws IOException, SQLException {
        //Receive 1: Login, 2: Register
        String op;
        do {
            op = in.readUTF();
            out.writeUTF(op);
            if (op.equals("1"))
                Login();
            else
                Register();

        } while (!op.equals("1"));
    }


    private void View() throws IOException {
        int bytes;
        System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                + "] User: " + username + " views book " + bookName);

        File file = new File("Books\\Server\\" + bookName + ".txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        out.writeLong(file.length());

        // break file into chunks
        byte[] buffer = new byte[4 * 1024];

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytes);
            out.flush();
        }
        fileInputStream.close();
    }


    private void Download() throws IOException {
        System.out.println("[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                + "] User: " + username + " downloads book " + bookName);
        int bytes;

        File file = new File("Books\\Server\\" + bookName + ".txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        out.writeLong(file.length());

        // break file into chunks
        byte[] buffer = new byte[4 * 1024];

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytes);
            out.flush();
        }
        fileInputStream.close();
    }

    private void Main_Menu() throws IOException, SQLException {
        String option = in.readUTF();

        switch (option) {
            case "1" -> View();
            case "2" -> Download();
            default -> {
                View();
            }
        }
    }

    private void Disconnect() {
        System.out.println("\n" + "[" + LocalDate.now() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + ":" + LocalDateTime.now().getSecond()
                + "] Close connection with client: " + socket + "\n");
        try {
            in.close();
            out.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
      try {
            //SignIn_Form();
            Login();
           // Main_Menu();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Clients.remove(username);
            Disconnect();
        }

    }


}
