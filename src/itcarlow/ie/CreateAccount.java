package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateAccount extends JFrame{
    private JPanel nameJPanel;
    private JLabel nameJLabel;
    private JTextField nameTextField;
    private JPanel addressJPanel;
    private JLabel addressJLabel;
    private JTextField addressTextField;
    private JPanel emailJPanel;
    private JTextField emailTextField;
    private JLabel emailJLabel;
    private JPanel telephoneJPanel;
    private JLabel telephoneJLabel;
    private JTextField telephoneTextField;
    private JButton submitJButton;
    private JButton cancelJButton;
    private JPanel buttonJPanel;
    private JPasswordField passwordTextField;
    private JPanel passwordJPanel;
    private JLabel passwordJLabel;
    private JPanel confirmEmailJPanel;
    private JTextField confirmEmailJTextField;
    private JLabel confirmEmailJLabel;
    private JPanel confirmPasswordJPanel;
    private JPasswordField confirmPasswordTextField;
    private JLabel confirmPasswordJLabel;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    String name = "";
    String email = "";
    String confirmEmail = "";
    String password = "";
    String confirmPassword = "";
    String address = "";
    String telephone = "";
    int i = 0;

    // constructor
    public CreateAccount(String title){
        // title
        super(title);
        // set layout of JFrame
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // name panel and add components
        nameJPanel = new JPanel(new FlowLayout());
        nameJLabel = new JLabel("Full Name:");
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(350,30));
        nameJPanel.add(nameJLabel);
        nameJPanel.add(nameTextField);

        // address panel and add components
        addressJPanel = new JPanel(new FlowLayout());
        addressJLabel = new JLabel("Address:");
        addressTextField = new JTextField();
        addressTextField.setPreferredSize(new Dimension(350,30));
        addressJPanel.add(addressJLabel);
        addressJPanel.add(addressTextField);

        // telephone panel and add components
        telephoneJPanel = new JPanel(new FlowLayout());
        telephoneJLabel = new JLabel("Telephone:");
        telephoneTextField = new JTextField();
        telephoneTextField.setPreferredSize(new Dimension(350,30));
        telephoneJPanel.add(telephoneJLabel);
        telephoneJPanel.add(telephoneTextField);

        // email panel and add components
        emailJPanel = new JPanel(new FlowLayout());
        emailJLabel = new JLabel("Email:");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(350,30));
        emailJPanel.add(emailJLabel);
        emailJPanel.add(emailTextField);

        // password panel and add components
        passwordJPanel = new JPanel(new FlowLayout());
        passwordJLabel = new JLabel("Password:");
        passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(new Dimension(350,30));
        passwordJPanel.add(passwordJLabel);
        passwordJPanel.add(passwordTextField);

        // confirm email panel and components
        confirmEmailJPanel = new JPanel(new FlowLayout());
        confirmEmailJLabel = new JLabel("Confirm Email");
        confirmEmailJTextField = new JTextField();
        confirmEmailJTextField.setPreferredSize(new Dimension(350,30));
        confirmEmailJPanel.add(confirmEmailJLabel);
        confirmEmailJPanel.add(confirmEmailJTextField);

        // confirm password panel and components
        confirmPasswordJPanel = new JPanel(new FlowLayout());
        confirmPasswordJLabel = new JLabel("Confirm Password");
        confirmPasswordTextField = new JPasswordField();
        confirmPasswordTextField.setPreferredSize(new Dimension(350,30));
        confirmPasswordJPanel.add(confirmPasswordJLabel);
        confirmPasswordJPanel.add(confirmPasswordTextField);

        // button panel and add buttons
        buttonJPanel = new JPanel(new FlowLayout());
        submitJButton = new JButton("Submit");
        cancelJButton = new JButton("Cancel");
        buttonJPanel.add(submitJButton);
        buttonJPanel.add(cancelJButton);

        // add panels to JFrame top to bottom
        add(nameJPanel);
        add(emailJPanel);
        add(confirmEmailJPanel);
        add(passwordJPanel);
        add(confirmPasswordJPanel);
        add(addressJPanel);
        add(telephoneJPanel);
        add(buttonJPanel);



        // submit button listener
        // if account is created, create instance of Login class
        submitJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    // establish connection to database
                    connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
                    // create prepared statement for inserting data into table
                    pstat = connection.prepareStatement("INSERT INTO customer (name,email,password,address,telephone) VALUES(?,?,?,?,?)");
                    name = nameTextField.getText();
                    email = emailTextField.getText();
                    confirmEmail = confirmEmailJTextField.getText();
                    password = new String(passwordTextField.getPassword());
                    confirmPassword = new String(confirmPasswordTextField.getPassword());
                    address = addressTextField.getText();
                    telephone = telephoneTextField.getText();
                    pstat.setString(1, name);
                    pstat.setString(2, email);
                    pstat.setString(3, password);
                    pstat.setString(4, address);
                    pstat.setString(5, telephone);
                    // compare email to confirm email
                    if(!email.equals(confirmEmail)){
                        JOptionPane.showMessageDialog(null,"Emails do not match", "Error", JOptionPane.ERROR_MESSAGE);
                        // compare password with confirmPassword
                    } else if(!password.equals(confirmPassword)){
                        JOptionPane.showMessageDialog(null,"Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                        // call validEmail method to check if email if valid
                    } else if(!validEmail(email)) {
                        JOptionPane.showMessageDialog(null,"Not a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
                    } else{
                            // insert data into table
                            i = pstat.executeUpdate();
                            System.out.println(i + " record successfully added to the customer table");
                        }
                } catch (SQLException sqlException){
                    sqlException.printStackTrace();
                } finally {
                    try {
                        connection.close();
                        pstat.close();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
                Login login = new Login("Log in");
                login.setVisible(true);
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                login.setSize(550,400);
                login.setLocation(500,400);
                dispose();
            }
        });
        // cancel button listener
        // create instance of Login class
        cancelJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Login login = new Login("Log in");
                login.setVisible(true);
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                login.setSize(550,400);
                login.setLocation(500,400);
                dispose();
            }
        });
    }// end constructor

    // validate inputted email
    // using java.util.regex.patterns.match()
    public static boolean validEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    // main
    public static void main(String args[]){
        CreateAccount createAccount = new CreateAccount("Create Account");
        createAccount.setVisible(true);
        createAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAccount.setSize(550,400);
        createAccount.setLocation(500,400);
    } // end main
}  // end class

