package Client.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoom extends  JFrame{

    private Socket socket = null;
    private DataOutputStream output = null;
    private DataInputStream input = null;
    private ArrayList<String> onlineUser;

    private final JLabel lbTitle = new JLabel("CHAT ROOM");
    private final JLabel lbFooter = new JLabel("HCMUS-CLC-19KTPM3-Introduction to Java-19127097");

    // public SignUp(Socket socket) throws IOException {
    public ChatRoom() {
        //this.socket=socket;
        //output = new DataOutputStream(socket.getOutputStream());
        //input = new DataInputStream(socket.getInputStream());

        this.setTitle("Chat Room | Connect");
        this.setSize(new Dimension(800, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        onlineUser= new ArrayList<>();
        for (int i=0;i<10;i++)
        onlineUser.add("Player1");
        addComponents();
    }
    public void waitForInputs() throws InterruptedException {
        synchronized (this) {
            wait();
        }
    }
    public void run() {
        this.setVisible(true);

    }
    public void addComponents() {
        //Content
        JPanel pnForm = new JPanel(new BorderLayout());

        //List of user
        JPanel pnScroll = new JPanel();
        JLabel lbUser = new JLabel("   List of online users:   ");
        lbUser.setFont(new Font("Serif", Font.BOLD, 18));
        lbUser.setBackground(new Color(231, 255, 255));
        JList<Object> list = new JList<>(onlineUser.toArray());
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
        JTextArea taChat = new JTextArea();
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
                    System.out.println("File already opended");
                }
        });
    }

    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.run();
    }
}


