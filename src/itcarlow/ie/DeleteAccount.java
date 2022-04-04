package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class DeleteAccount extends JFrame {
    // variables
    // panels
    private JPanel emailJPanel;
    private JPanel passwordJPanel;
    private JPanel buttonJPanel;
    // labels
    private JLabel emailJLabel;
    private JLabel passwordJLabel;
    // text fields
    private JTextField emailTextField;
    private JPasswordField passwordTextField;
    // buttons
    private JButton deleteJButton;
    private JButton cancelJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    PreparedStatement pstatInvoice = null;
    PreparedStatement pstatDelete = null;
    ResultSet resultSet = null;
    int i = 0;
    String email;
    String DBPassword;
    String password;
    boolean matchPasswords;
    int deleteFlag = 1;

    // constructor
    public DeleteAccount(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // email panel and components
        emailJPanel = new JPanel(new FlowLayout());
        emailJLabel = new JLabel("Email");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(350,40));
        emailJPanel.add(emailJLabel);
        emailJPanel.add(emailTextField);

        // customer name panel and components
        passwordJPanel = new JPanel(new FlowLayout());
        passwordJLabel = new JLabel("Password");
        passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(new Dimension(350,40));
        passwordJPanel.add(passwordJLabel);
        passwordJPanel.add(passwordTextField);

        // buttons panel and buttons
        buttonJPanel = new JPanel(new FlowLayout());
        deleteJButton = new JButton("Delete");
        cancelJButton = new JButton("Cancel");
        buttonJPanel.add(deleteJButton);
        buttonJPanel.add(cancelJButton);

        // add panels to JFrame
        add(emailJPanel);
        add(passwordJPanel);
        add(buttonJPanel);

        // retrieve email from database
        // publish customers email to the emailTextField
        try{
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
            // create prepared statement to select all data from product table
            pstat = connection.prepareStatement("SELECT email, password FROM customer WHERE idCust=?");
            pstat.setInt(1,Login.customerID);
            resultSet = pstat.executeQuery();
            if(resultSet.next()){
                emailTextField.setText(resultSet.getString("email"));
                DBPassword = resultSet.getString("password");
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

        // delete button listener
        // create instance of Login class
        deleteJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    email = emailTextField.getText();
                    password = new String(passwordTextField.getPassword());
                    // check if password is correct
                    matchPasswords = HashPassword.checkPassword(password, DBPassword);
                    if(matchPasswords){
                        // establish connection to database
                        connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
                        // create prepared statement to check if the customer has an order history
                        pstatInvoice = connection.prepareStatement("SELECT * FROM invoice WHERE customerFk=?");
                        pstatInvoice.setInt(1,Login.customerID);
                        resultSet = pstatInvoice.executeQuery();
                        if(!resultSet.next()){
                            // create prepared statement to delete account with no invoice record
                            pstatDelete = connection.prepareStatement("DELETE FROM customer WHERE idCust=?");
                            pstatDelete.setInt(1,Login.customerID);
                            // delete customer account
                            i = pstatDelete.executeUpdate();
                            System.out.println(i + " record successfully removed from the customer table");
                            login();
                            dispose();
                        } else{
                            // create prepared statement to update deleteFlag to 1
                            pstat = connection.prepareStatement("UPDATE customer SET deleteFlag=? WHERE email=?");
                            pstat.setInt(1,deleteFlag);
                            pstat.setString(2,email);
                            // update customer account
                            i = pstat.executeUpdate();
                            System.out.println(i + " record successfully updated in customer table");
                            login();
                            dispose();
                        }
                    } else {
                        // password doesn't match database
                        JOptionPane.showMessageDialog(null,"Incorrect Email or Password", "Error", JOptionPane.ERROR_MESSAGE);
                        deleteAccount();
                        dispose();
                    }
                }catch (SQLException sqlException){
                    sqlException.printStackTrace();
                } finally {
                    try {
                        connection.close();
                        pstat.close();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
        // cancel button listener
        // create instance of CreateOrder class
        cancelJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreateOrder createOrder = new CreateOrder("Order a Product");
                createOrder.setVisible(true);
                createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createOrder.setSize(550,400);
                createOrder.setLocation(500,400);
                dispose();
            }
        });
    }// end constructor

    // create instance of Login class
    private static void login(){
        Login login = new Login("Login");
        login.setVisible(true);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setSize(550,400);
        login.setLocation(500,400);
    }

    // create instance of deleteAccount class
    private static void deleteAccount(){
        DeleteAccount deleteAccount = new DeleteAccount("Delete Account");
        deleteAccount.setVisible(true);
        deleteAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteAccount.setSize(550,400);
        deleteAccount.setLocation(500,400);
    }

    // main
    public static void main(String args[]){
        DeleteAccount deleteAccount = new DeleteAccount("Delete Account");
        deleteAccount.setVisible(true);
        deleteAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteAccount.setSize(550,400);
        deleteAccount.setLocation(500,400);
    } // end main
}// end class
