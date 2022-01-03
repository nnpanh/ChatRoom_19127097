package Client.UI;

import Client.ClientServices;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom extends  JFrame{
    private ArrayList<String> onlineUser;
    private ClientServices services;
    private String username;
    private final JLabel lbTitle;
    private final JLabel lbFooter = new JLabel("HCMUS-CLC-19KTPM3-Introduction to Java-19127097");
    private JTextArea taChat = new JTextArea();
    private JList<Object> list;

    public ChatRoom(ClientServices services, String username) {
        this.username=username;
        this.services=services;
        this.setTitle("Chat Room | Connect");
        this.setSize(new Dimension(800, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        lbTitle = new JLabel("CHAT ROOM - "+username);
        onlineUser = services.otherClients;
        addComponents();
    }

    public void run() throws IOException {
        this.setVisible(true);
        String receivedMessage;
        BufferedReader reader = services.input;
        do{
            receivedMessage = reader.readLine();
            System.out.println(receivedMessage);
            if (!receivedMessage.equals("quit")) {
                String[] msg = receivedMessage.split(" ",3);
                if (msg[0].equals("new")) {
                    onlineUser.add(msg[1]);
                    list.setListData(onlineUser.toArray());
                }
                else if (msg[0].equals("message")){
                    taChat.append(msg[1]+": "+msg[2]+"\n");
                }

            }

        } while (!receivedMessage.equals("quit"));

    }
    public void addComponents() {
        //Content
        JPanel pnForm = new JPanel(new BorderLayout());

        //List of user
        JPanel pnScroll = new JPanel();
        JLabel lbUser = new JLabel("   List of online users:   ");
        lbUser.setFont(new Font("Serif", Font.BOLD, 18));
        lbUser.setBackground(new Color(231, 255, 255));
        list = new JList<>(onlineUser.toArray());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setBackground(new Color(231, 255, 255));
        scrollPane.setBorder(new EmptyBorder(10,10,10,10));
        list.setLayoutOrientation(JList.VERTICAL);
        pnScroll.setLayout(new BorderLayout());
        pnScroll.add(lbUser,BorderLayout.NORTH);
        pnScroll.add(scrollPane,BorderLayout.CENTER);
        pnScroll.setBackground(new Color(231, 255, 255));
        pnScroll.setBorder(new EmptyBorder(0,0,0,0));

        //Chatbox
        taChat.setMargin(new Insets(20,20,20,20));
        taChat.setFont(new Font("Serif",Font.PLAIN,14));
        taChat.setEditable(false);
        taChat.setLineWrap(true);
        pnForm.add(taChat,BorderLayout.CENTER);

        //Text input
        JPanel pnChat = new JPanel(new FlowLayout());
        JTextField tfInput = new JTextField(40);
        tfInput.setFont(new Font("Serif",Font.PLAIN,14));
        JButton btnSend = new JButton("Send");
        JButton btnFile = new JButton("Files");
        pnChat.add(tfInput);
        pnChat.add(btnSend);
        pnChat.add(btnFile);
        pnChat.setBackground(new Color(231, 255, 255));
        pnForm.add(pnChat,BorderLayout.SOUTH);


        //Title
        JPanel pnTitle = new JPanel();
        pnTitle.setLayout(new FlowLayout());
        pnTitle.setBackground(new Color(12,113,195));
        pnTitle.add(lbTitle);
        lbTitle.setFont(new Font("Serif", Font.BOLD, 20));
        lbTitle.setForeground(Color.white);

        //Footer
        JPanel pnFooter = new JPanel();
        pnFooter.setBackground(Color.white);
        pnFooter.add(lbFooter);
        lbFooter.setFont(lbFooter.getFont().deriveFont(Font.ITALIC));

        //Layout to group all
        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout());


        //Add panel to frame
        pnMain.add(pnTitle,BorderLayout.NORTH);
        pnMain.add(pnForm,BorderLayout.CENTER);
        pnMain.add(pnFooter,BorderLayout.SOUTH);
        pnMain.add(pnScroll,BorderLayout.WEST);
        this.add(pnMain);

        //Action listener
        btnSend.addActionListener(e -> {
            String sendTo = list.getSelectedValue().toString();
            try {
                services.sendMessage(tfInput.getText(),sendTo);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            taChat.append(tfInput.getText()+"\n");
        });
        btnFile.addActionListener(e->{
            JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName() + ".");
                } else {
                    System.out.println("File already opened");
                }
        });
    }
}


