package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Login extends JFrame {
    // variables
    //public variable for customer ID
    public static int customerID;
    // panels
    private JPanel emailJPanel;
    private JPanel passwordJPanel;
    private JPanel buttonJPanel;
    private JPanel signUpJPanel;
    // labels
    private JLabel emailJLabel;
    private JLabel passwordJLabel;
    private JLabel signUpJLabel;
    // text field
    private JTextField emailTextField;
    private JPasswordField passwordTextField;
    // buttons
    private JButton loginJButton;
    private JButton cancelJButton;
    private JButton signUpButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet;
    String email = "";
    String password = "";
    String DBPassword;
    boolean matchedPasswords;
    int deleteFlag;


    // constructor
    public Login(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // email panel
        emailJPanel = new JPanel(new FlowLayout());
        emailJLabel = new JLabel("Email");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(350,40));
        emailJPanel.add(emailJLabel);
        emailJPanel.add(emailTextField);

        // password panel
        passwordJPanel = new JPanel(new FlowLayout());
        passwordJLabel = new JLabel("Password");
        passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(new Dimension(350,40));
        passwordJPanel.add(passwordJLabel);
        passwordJPanel.add(passwordTextField);

        // log in and cancel number
        buttonJPanel = new JPanel(new FlowLayout());
        loginJButton = new JButton("Log in");
        cancelJButton = new JButton("Cancel");
        buttonJPanel.add(loginJButton);
        buttonJPanel.add(cancelJButton);

        // sign up panel
        signUpJPanel = new JPanel(new FlowLayout());
        signUpJLabel = new JLabel("Don't have an account?");
        signUpButton = new JButton("Sign up");
        signUpJPanel.add(signUpJLabel);
        signUpJPanel.add(signUpButton);

        // add panels to jframe
        add(emailJPanel);
        add(passwordJPanel);
        add(buttonJPanel);
        add(signUpJPanel);

        // login button listener
        // check if email and password is correct
        // create instance of CreateOrder class
        loginJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // check if inputs are correct
                try{
                    // establish connection to database
                    connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
                    email = emailTextField.getText();
                    password = new String(passwordTextField.getPassword());
                    pstat = connection.prepareStatement("SELECT idCust, password, deleteFlag FROM customer WHERE email=?");
                    pstat.setString(1, email);
                    resultSet = pstat.executeQuery();
                        if(resultSet.next()) {
                            DBPassword = resultSet.getString("password");
                            customerID = resultSet.getInt("idCust");
                            deleteFlag = resultSet.getInt("deleteFlag");
                        } else{
                            JOptionPane.showMessageDialog(null, "Incorrect email or password", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        // check if deleteFlag = 1
                        if(deleteFlag == 1){
                            JOptionPane.showMessageDialog(null, "Incorrect email or password", "Error", JOptionPane.ERROR_MESSAGE);
                        } else{
                            // error message for empty email textfield
                            if (emailTextField.getText().length() == 0) {
                                JOptionPane.showMessageDialog(null, "Please enter an email", "Error", JOptionPane.ERROR_MESSAGE);
                                // error message for empty password textfield
                            } else if (passwordTextField.getPassword().length == 0) {
                                JOptionPane.showMessageDialog(null, "Please enter a password", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            // check if new password matches password stored in database
                            matchedPasswords = HashPassword.checkPassword(password, DBPassword);
                            if (matchedPasswords) {
                                CreateOrder createOrder = new CreateOrder("Order a Product");
                                createOrder.setVisible(true);
                                createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                createOrder.setSize(550, 400);
                                createOrder.setLocation(500, 400);
                                dispose();
                            } else {
                                // password doesn't not match
                                JOptionPane.showMessageDialog(null, "Incorrect email or password", "Error", JOptionPane.ERROR_MESSAGE);
                                Login login = new Login("Login");
                                login.setVisible(true);
                                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                login.setSize(550,400);
                                login.setLocation(500,400);
                                dispose();
                            }
                        }

                }catch (SQLException sqlException){
                    sqlException.printStackTrace();
                } finally {
                    try {
                        connection.close();
                        pstat.close();
                        resultSet.close();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        });

        // cancel button listener
        // clear text fields
        cancelJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Login login = new Login("Login");
                login.setVisible(true);
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                login.setSize(550,400);
                login.setLocation(500,400);
                dispose();
            }
        });

        // sign up button listener
        // create instance of CreateAccount class
        signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreateAccount createAccount = new CreateAccount("Create Account");
                createAccount.setVisible(true);
                createAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createAccount.setSize(550,400);
                createAccount.setLocation(500,400);
                dispose();
            }
        });
    }// end constructor

    // main
    public static void main(String args[]){
        Login login = new Login("Login");
        login.setVisible(true);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setSize(550,400);
        login.setLocation(500,400);
    }// end main
}// end class
