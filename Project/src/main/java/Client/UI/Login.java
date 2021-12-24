package Client.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Login extends JFrame {
    private final JLabel lbUsername = new JLabel("Username: ");
    private final JLabel lbPassword = new JLabel("Password");
    private final JLabel lbTitle = new JLabel("LOGIN FORM");
    private final JLabel lbFooter = new JLabel("HCMUS-CLC-19KTPM3-Introduction to Java-19127097");
    private final JTextField tfUsername = new JTextField("guest", 15);
    private final JPasswordField tfPassword = new JPasswordField("123", 15);
    private final JButton btnSignUp = new JButton("Login");
    private Socket socket = null;
    private DataOutputStream output = null;
    private DataInputStream input = null;

    public Login(Socket socket) throws IOException {
        this.socket = socket;
        output = new DataOutputStream(socket.getOutputStream());
        input = new DataInputStream(socket.getInputStream());
        addComponents();
        this.setTitle("Chat Room | Login");
        this.setSize(new Dimension(400, 350));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    public void waitForInputs() throws InterruptedException {
        synchronized (this) {
            wait();
        }
    }

    public void run() throws InterruptedException {
        this.setVisible(true);
        waitForInputs();

    }

    public void addComponents() {
        //Set layout
        JPanel pnForm = new JPanel();
        pnForm.setLayout(new GridBagLayout());
        pnForm.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pnForm.setBackground(new Color(231, 255, 255));


        JPanel pnTitle = new JPanel();
        pnTitle.setLayout(new FlowLayout());
        pnTitle.setBackground(new Color(12, 113, 195));
        pnTitle.add(lbTitle);
        lbTitle.setFont(new Font("Serif", Font.BOLD, 30));
        lbTitle.setForeground(Color.white);

        JPanel pnFooter = new JPanel();
        pnFooter.setBackground(Color.white);
        pnFooter.add(lbFooter);
        lbFooter.setFont(lbFooter.getFont().deriveFont(Font.ITALIC));

        JPanel pnMain = new JPanel();
        pnMain.setLayout(new BorderLayout());

        //Add actionListener for button
        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action(e);
            }
        });

        //Set position for components
        GridBagConstraints position = new GridBagConstraints();
        position.insets = new Insets(10, 20, 10, 0);
        position.anchor = GridBagConstraints.LINE_START;
        position.gridx = 0;
        position.gridy = 0;
        lbUsername.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(lbUsername, position);
        position.gridx = 0;
        position.gridy = 1;
        lbPassword.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(lbPassword, position);

        position.gridx = 1;
        position.gridy = 0;
        tfUsername.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(tfUsername, position);
        position.gridx = 1;
        position.gridy = 1;
        tfPassword.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(tfPassword, position);

        position.gridx = 0;
        position.gridy = 3;
        position.gridwidth = 2;
        position.anchor = GridBagConstraints.CENTER;
        btnSignUp.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(btnSignUp, position);
        this.add(pnForm);

        //Add panel to frame
        pnMain.add(pnTitle, BorderLayout.NORTH);
        pnMain.add(pnForm, BorderLayout.CENTER);
        pnMain.add(pnFooter, BorderLayout.SOUTH);
        this.add(pnMain);
    }

    private void action(ActionEvent e) {
        if (e.getSource() == btnSignUp) {
            try {
                output.writeUTF(tfUsername.getText());
                output.writeUTF(String.valueOf(tfPassword.getPassword()));
                output.flush();

                boolean LoginSuccess = false;
                LoginSuccess = input.readBoolean(); //Receive regis_flag from sv

                if (LoginSuccess) {
                    JOptionPane.showMessageDialog(null, "Login successfully");
                    this.setVisible(false);
                    synchronized (this) {
                        notifyAll();
                    }
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to login");
                }

            } catch (IOException io) {
                System.out.println("Close GUI");
            }
        }
    }

}


