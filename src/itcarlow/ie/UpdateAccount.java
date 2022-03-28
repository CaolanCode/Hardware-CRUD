package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class UpdateAccount extends JFrame {
    // variables
    // panels
    private JPanel nameJPanel;
    private JPanel addressJPanel;
    private JPanel emailJPanel;
    private JPanel telephoneJPanel;
    private JPanel passwordJPanel;
    private JPanel updateCancelJPanel;
    // labels
    private JLabel nameJLabel;
    private JLabel addressJLabel;
    private JLabel emailJLabel;
    private JLabel telephoneJLabel;
    private JLabel passwordJLabel;
    // text fields
    private JTextField nameTextField;
    private JTextField addressTextField;
    private JTextField emailTextField;
    private JTextField telephoneTextField;
    private JTextField passwordTextField;
    // buttons
    private JButton updateJButton;
    private JButton cancelJButton;
    private JButton deleteJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet;
    String name;
    String address;
    String email;
    String password;
    String telephone;
    int i = 0;

    // constructor
    public UpdateAccount(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // name panel
        nameJPanel = new JPanel(new FlowLayout());
        nameJLabel = new JLabel("Name");
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(350,40));
        nameJPanel.add(nameJLabel);
        nameJPanel.add(nameTextField);

        // address panel
        addressJPanel = new JPanel(new FlowLayout());
        addressJLabel = new JLabel("Address");
        addressTextField = new JTextField();
        addressTextField.setPreferredSize(new Dimension(350,40));
        addressJPanel.add(addressJLabel);
        addressJPanel.add(addressTextField);

        // email panel
        emailJPanel = new JPanel(new FlowLayout());
        emailJLabel = new JLabel("Email");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(350,40));
        emailJPanel.add(emailJLabel);
        emailJPanel.add(emailTextField);

        // telephone panel
        telephoneJPanel = new JPanel(new FlowLayout());
        telephoneJLabel = new JLabel("Telephone");
        telephoneTextField = new JTextField();
        telephoneTextField.setPreferredSize(new Dimension(350,40));
        telephoneJPanel.add(telephoneJLabel);
        telephoneJPanel.add(telephoneTextField);

        // password panel
        passwordJPanel = new JPanel(new FlowLayout());
        passwordJLabel = new JLabel("Password");
        passwordTextField = new JTextField();
        passwordTextField.setPreferredSize(new Dimension(350,40));
        passwordJPanel.add(passwordJLabel);
        passwordJPanel.add(passwordTextField);

        // button panel
        updateCancelJPanel = new JPanel(new FlowLayout());
        updateJButton = new JButton("Update");
        cancelJButton = new JButton("Cancel");
        deleteJButton = new JButton("Delete");
        updateCancelJPanel.add(updateJButton);
        updateCancelJPanel.add(cancelJButton);
        updateCancelJPanel.add(deleteJButton);

        // add panels to jframe
        add(nameJPanel);
        add(emailJPanel);
        add(passwordJPanel);
        add(addressJPanel);
        add(telephoneJPanel);
        add(updateCancelJPanel);

        try{
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
            // create prepared statement to select all data from product table
            pstat = connection.prepareStatement("SELECT * FROM customer WHERE idCust=?");
            pstat.setInt(1,Login.customerID);
            resultSet = pstat.executeQuery();
            while(resultSet.next()){
                nameTextField.setText(resultSet.getString("name"));
                emailTextField.setText(resultSet.getString("email"));
                passwordTextField.setText(resultSet.getString("password"));
                addressTextField.setText(resultSet.getString("address"));
                telephoneTextField.setText(resultSet.getString("telephone"));
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

        // update button listener
        // create instance of CreateOrder class
        updateJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    // establish connection to database
                    connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
                    // create prepared statement for inserting data into table
                    pstat = connection.prepareStatement("UPDATE customer (name,email,password,address,telephone) VALUES(?,?,?,?,?) WHERE idCust=?");
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
                    pstat.setInt(6, Login.customerID);
                    // insert data into table
                    i = pstat.executeUpdate();
                    System.out.println(i+"record succesfully updated");
            } catch (SQLException sqlException){
                sqlException.printStackTrace();
            } finally {
                try {
                    connection.close();
                    pstat.close();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }//end finally
                CreateOrder createOrder = new CreateOrder("Order a Product");
                createOrder.setVisible(true);
                createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createOrder.setSize(550,400);
                createOrder.setLocation(500,400);
                dispose();
            }
        });// end listener
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

        // delete button listener
        // create instance of DeleteAccount class
        deleteJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DeleteAccount deleteAccount = new DeleteAccount("Delete Account");
                deleteAccount.setVisible(true);
                deleteAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                deleteAccount.setSize(550,400);
                deleteAccount.setLocation(500,400);
                dispose();
            }
        });


    }// end constructor
    // main
    public static void main(String args[]){
        UpdateAccount updateAccount = new UpdateAccount("Update Account Details");
        updateAccount.setVisible(true);
        updateAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateAccount.setSize(550,400);
        updateAccount.setLocation(500,400);
    }// end main
}// end class
