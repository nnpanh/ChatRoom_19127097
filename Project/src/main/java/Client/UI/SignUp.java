package Client.UI;

import Client.ClientServices;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class SignUp extends JFrame {
    private final BufferedWriter output;
    private final BufferedReader input;
    private final ClientServices services;
    private final Thread thread;
    private final JLabel lbUsername = new JLabel("Username");
    private final JLabel lbPassword = new JLabel("Password");
    private final JLabel lbConfirm = new JLabel("Confirm");
    private final JLabel lbTitle = new JLabel("CREATE A NEW ACCOUNT");
    private final JLabel lbFooter = new JLabel("HCMUS-CLC-19KTPM3-Introduction to Java-19127097");

    private final JTextField tfUsername = new JTextField(15);
    private final JPasswordField tfConfirm = new JPasswordField( 15);
    private final JPasswordField tfPassword = new JPasswordField(15);

    private final JButton btnSignUp = new JButton("Create");

    public SignUp(ClientServices services, Thread t) throws IOException {
        this.services = services;
        input = services.input;
        output = services.output;
        thread=t;
        this.setTitle("Chat Room | Sign Up");
        this.setSize(new Dimension(500, 400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        addComponents();
    }

    public void run() throws InterruptedException {
        this.setVisible(true);
    }
    public void addComponents() {
        //Set layout
        JPanel pnForm = new JPanel();
        pnForm.setLayout(new GridBagLayout());
        pnForm.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        pnForm.setBackground(new Color(231, 255, 255));

        JPanel pnTitle = new JPanel();
        pnTitle.setLayout(new FlowLayout());
        pnTitle.setBackground(new Color(12,113,195));
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
        btnSignUp.addActionListener(e -> {
            try {
                action(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Set position for components
        GridBagConstraints position = new GridBagConstraints();
        position.insets = new Insets(10, 30, 10, 0);
        position.anchor = GridBagConstraints.LINE_START;
        position.gridx = 0;
        position.gridy = 0;
        lbUsername.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(lbUsername, position);
        position.gridx = 0;
        position.gridy = 1;
        lbPassword.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(lbPassword, position);
        position.gridx = 0;
        position.gridy = 2;
        lbConfirm.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(lbConfirm, position);

        position.gridx = 1;
        position.gridy = 0;
        tfUsername.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(tfUsername, position);
        position.gridx = 1;
        position.gridy = 1;
        tfPassword.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(tfPassword, position);
        position.gridx = 1;
        position.gridy = 2;
        tfConfirm.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(tfConfirm, position);

        position.gridx = 0;
        position.gridy = 3;
        position.gridwidth = 2;
        position.anchor = GridBagConstraints.CENTER;
        btnSignUp.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(btnSignUp, position);
        this.add(pnForm);

        //Add panel to frame
        pnMain.add(pnTitle,BorderLayout.NORTH);
        pnMain.add(pnForm,BorderLayout.CENTER);
        pnMain.add(pnFooter,BorderLayout.SOUTH);
        this.add(pnMain);
    }

    private void action(ActionEvent e) throws IOException {
        if (e.getSource()== btnSignUp){
            try {
                output.write("register "+tfUsername.getText()+" "+String.valueOf(tfPassword.getPassword())+" "+String.valueOf(tfConfirm.getPassword()));
                output.newLine();
                output.flush();

                String value = input.readLine();
                if (value.equals("true")) {
                    JOptionPane.showMessageDialog(null, "Register successfully");
                    this.setVisible(false);
                    Login login = new Login(services,thread);
                    login.run();
                    this.dispose();
                }
                else
                    JOptionPane.showMessageDialog(null, "Failed to register");
            } catch (IOException | InterruptedException io) {
                System.out.println("Close GUI");
            }
        }
    }
}


