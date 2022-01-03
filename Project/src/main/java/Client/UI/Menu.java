package Client.UI;

import Client.ClientServices;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Menu extends JFrame {
    private Thread thread;
    private ClientServices services;
    public  Menu(ClientServices services,Thread t) throws IOException {
        super("Pick one");
        thread = t;
        this.services = services;
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
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        btnSignUp.addActionListener(e -> {
            try {
                NewAccountPerformed(e);
            } catch (IOException | InterruptedException ex) {
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
    private void NewAccountPerformed(java.awt.event.ActionEvent evt) throws IOException, InterruptedException {
        this.setVisible(false);
        SignUp signUp = new SignUp(services,thread);
        signUp.run();
        this.dispose();

    }

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) throws IOException, InterruptedException {
        this.setVisible(false);
        Login login = new Login(services,thread);
        login.run();
        this.dispose();
    }

    public void run()  {
        this.setVisible(true);
    }
}
