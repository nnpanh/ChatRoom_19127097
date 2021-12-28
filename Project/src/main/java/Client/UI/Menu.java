package Client.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class Menu extends JFrame {
    private Socket socket;
    private BufferedWriter output;
    public  Menu(Socket socket) throws IOException {
        super("Pick one");
        this.socket=socket;
        output= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        addComponents();
    }
    public void addComponents() throws IOException {
        JPanel pnFill = new JPanel(new BorderLayout());
        JPanel pnArea = new JPanel();
        BoxLayout boxLayout = new BoxLayout(pnArea,BoxLayout.X_AXIS);
        pnArea.setLayout(boxLayout);
        JPanel pnMain = new JPanel(new FlowLayout());
        JButton btnLogin = new JButton("Login");
        JButton btnSignUp = new JButton("Sign up");
        pnMain.setBackground(Color.white);

        btnLogin.setFont(new Font("Serif", Font.PLAIN, 20));
        btnSignUp.setFont(new Font("Serif", Font.PLAIN,20));
        pnArea.add(btnLogin);
        pnArea.add(Box.createRigidArea(new Dimension(40,0)));
        pnArea.add(btnSignUp);
        pnMain.add(pnArea);

        //Action listener
        btnLogin.addActionListener(e -> {
            try {
                LoginActionPerformed(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        btnSignUp.addActionListener(e -> {
            try {
                NewAccountPerformed(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        JPanel pnTitle = new JPanel(new FlowLayout());
        JLabel picLabel = new JLabel("Welcome to Chat-room");
        picLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        pnTitle.add(picLabel);
        pnTitle.setBackground(Color.white);

        pnFill.add(pnTitle,BorderLayout.CENTER);
        pnFill.setPreferredSize(new Dimension(300,100));
        pnFill.setBackground(Color.white);
        pnArea.setBackground(Color.white);
        pnMain.setBackground(Color.white);
        pnFill.add(pnMain,BorderLayout.SOUTH);
        add(pnFill);
        pack();
        setLocationRelativeTo(null);
    }
    private void NewAccountPerformed(java.awt.event.ActionEvent evt) throws IOException {
        try {
            output.write("2");
            output.newLine();
            output.flush();
        } catch (IOException io) {
            System.out.println("Close GUi");
        } finally {
            this.dispose();
        }
    }

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        try {
            output.write("1");
            output.newLine();
            output.flush();
        } catch (IOException io) {
            System.out.println("Close GUI");
        } finally {
            this.dispose();
        }
    }

    public void run() throws InterruptedException {
        this.setVisible(true);
    }
}
