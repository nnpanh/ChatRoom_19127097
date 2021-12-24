package Client.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ChatRoom extends  JFrame{

    private Socket socket = null;
    private DataOutputStream output = null;
    private DataInputStream input = null;

    private final JLabel lbTitle = new JLabel("CREATE A NEW ACCOUNT");
    private final JLabel lbFooter = new JLabel("HCMUS-CLC-19KTPM3-Introduction to Java-19127097");

    private final JButton btnSignUp = new JButton("Sign up");

    // public SignUp(Socket socket) throws IOException {
    public ChatRoom() {
        //this.socket=socket;
        //output = new DataOutputStream(socket.getOutputStream());
        //input = new DataInputStream(socket.getInputStream());
        addComponents();
        this.setTitle("Chat Room | Connect");
        this.setSize(new Dimension(400, 300));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
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
        //Set layout
        JPanel pnForm = new JPanel();

        JPanel pnTitle = new JPanel();
        pnTitle.setLayout(new FlowLayout());
        pnTitle.setBackground(new Color(12,113,195));
        pnTitle.add(lbTitle);
        lbTitle.setFont(new Font("Serif", Font.BOLD, 20));
        lbTitle.setForeground(Color.white);

        JPanel pnFooter = new JPanel();
        pnFooter.setBackground(Color.white);
        pnFooter.add(lbFooter);
        lbFooter.setFont(lbFooter.getFont().deriveFont(Font.ITALIC));

        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout());


        //Add panel to frame
        pnMain.add(pnTitle,BorderLayout.NORTH);
        pnMain.add(pnForm,BorderLayout.CENTER);
        pnMain.add(pnFooter,BorderLayout.SOUTH);
        this.add(pnMain);
    }

    private void action(ActionEvent e) {
        if (e.getSource()== btnSignUp){
            synchronized (this) {
                notifyAll();
            }
        }
    }
}


