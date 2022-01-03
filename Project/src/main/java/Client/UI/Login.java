package Client.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Login extends JFrame {
    private final JLabel lbUsername = new JLabel("Username: ");
    private final JLabel lbPassword = new JLabel("Password");
    private final JLabel lbTitle = new JLabel("LOGIN FORM");
    private final JLabel lbFooter = new JLabel("HCMUS-CLC-19KTPM3-Introduction to Java-19127097");
    private final JTextField tfUsername = new JTextField("guest", 15);
    private final JPasswordField tfPassword = new JPasswordField("123", 15);
    private final JButton btnLogin = new JButton("Login");
    private Socket socket = null;
    private BufferedReader input = null;
    private BufferedWriter output = null;
    private final Thread thread;

    public Login(Socket socket,Thread t) throws IOException {
        this.socket = socket;
        thread = t;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        addComponents();
        this.setTitle("Chat Room | Login");
        this.setSize(new Dimension(400, 350));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
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
        btnLogin.addActionListener(new ActionListener() {
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
        btnLogin.setFont(new Font("Serif", Font.PLAIN, 18));
        pnForm.add(btnLogin, position);
        this.add(pnForm);

        //Add panel to frame
        pnMain.add(pnTitle, BorderLayout.NORTH);
        pnMain.add(pnForm, BorderLayout.CENTER);
        pnMain.add(pnFooter, BorderLayout.SOUTH);
        this.add(pnMain);
    }

    private void action(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            try {
                output.write("login "+tfUsername.getText()+" "+String.valueOf(tfPassword.getPassword()));
                output.newLine();
                output.flush();

                boolean LoginSuccess = false;
                String value= input.readLine(); //Receive regis_flag from sv
                if (value.equals("true")) LoginSuccess=true;
                if (LoginSuccess) {
                    JOptionPane.showMessageDialog(null, "Login successfully");
                    this.setVisible(false);
                    synchronized (thread){
                        thread.notifyAll();
                    }
                    this.dispose();
                } else if (value.equals("false")){
                    JOptionPane.showMessageDialog(null, "Login fail: Wrong username/password");
                }
                else JOptionPane.showMessageDialog(null, "Account already logged in");

            } catch (IOException io) {
                System.out.println("Close GUI");
            }
        }
    }

}


