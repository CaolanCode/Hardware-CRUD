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
    private JTextField passwordTextField;
    private JPanel passwordJPanel;
    private JLabel passwordJLabel;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    String name = "";
    String email = "";
    String password = "";
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
        nameTextField.setPreferredSize(new Dimension(350,40));
        nameJPanel.add(nameJLabel);
        nameJPanel.add(nameTextField);

        // address panel and add components
        addressJPanel = new JPanel(new FlowLayout());
        addressJLabel = new JLabel("Address:");
        addressTextField = new JTextField();
        addressTextField.setPreferredSize(new Dimension(350,40));
        addressJPanel.add(addressJLabel);
        addressJPanel.add(addressTextField);

        // telephone panel and add components
        telephoneJPanel = new JPanel(new FlowLayout());
        telephoneJLabel = new JLabel("Telephone:");
        telephoneTextField = new JTextField();
        telephoneTextField.setPreferredSize(new Dimension(350,40));
        telephoneJPanel.add(telephoneJLabel);
        telephoneJPanel.add(telephoneTextField);

        // email panel and add components
        emailJPanel = new JPanel(new FlowLayout());
        emailJLabel = new JLabel("Email:");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(350,40));
        emailJPanel.add(emailJLabel);
        emailJPanel.add(emailTextField);

        // password panel and add components
        passwordJPanel = new JPanel(new FlowLayout());
        passwordJLabel = new JLabel("Password:");
        passwordTextField = new JTextField();
        passwordTextField.setPreferredSize(new Dimension(350,40));
        passwordJPanel.add(passwordJLabel);
        passwordJPanel.add(passwordTextField);

        // button panel and add buttons
        buttonJPanel = new JPanel(new FlowLayout());
        submitJButton = new JButton("Submit");
        cancelJButton = new JButton("Cancel");
        buttonJPanel.add(submitJButton);
        buttonJPanel.add(cancelJButton);

        // add panels to JFrame top to bottom
        add(nameJPanel);
        add(addressJPanel);
        add(telephoneJPanel);
        add(emailJPanel);
        add(passwordJPanel);
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
                    password = passwordTextField.getText();
                    address = addressTextField.getText();
                    telephone = telephoneTextField.getText();
                    pstat.setString(1, name);
                    pstat.setString(2, email);
                    pstat.setString(3, password);
                    pstat.setString(4, address);
                    pstat.setString(5, telephone);
                    // insert data into table
                    i = pstat.executeUpdate();
                    System.out.println(i+"record succesfully added to the table");

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

    // main
    public static void main(String args[]){
        CreateAccount createAccount = new CreateAccount("Create Account");
        createAccount.setVisible(true);
        createAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createAccount.setSize(550,400);
        createAccount.setLocation(500,400);
    } // end main
}  // end class

