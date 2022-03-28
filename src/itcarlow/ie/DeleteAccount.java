package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAccount extends JFrame {
    // variables
    // panels
    private JPanel customerIdJPanel;
    private JPanel nameJPanel;
    private JPanel buttonJPanel;
    // labels
    private JLabel customerIdJLabel;
    private JLabel nameJLabel;
    // text fields
    private JTextField customerIdTextField;
    private JTextField nameTextField;
    // buttons
    private JButton deleteJButton;
    private JButton cancelJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    int i = 0;
    String customerIDText;
    int customerID;

    // constructor
    public DeleteAccount(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // customer id panel and components
        customerIdJPanel = new JPanel(new FlowLayout());
        customerIdJLabel = new JLabel("Customer ID");
        customerIdTextField = new JTextField();
        customerIdTextField.setPreferredSize(new Dimension(350,40));
        customerIdJPanel.add(customerIdJLabel);
        customerIdJPanel.add(customerIdTextField);

        // customer name panel and components
        nameJPanel = new JPanel(new FlowLayout());
        nameJLabel = new JLabel("Name");
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(350,40));
        nameJPanel.add(nameJLabel);
        nameJPanel.add(nameTextField);

        // buttons panel and buttons
        buttonJPanel = new JPanel(new FlowLayout());
        deleteJButton = new JButton("Delete");
        cancelJButton = new JButton("Cancel");
        buttonJPanel.add(deleteJButton);
        buttonJPanel.add(cancelJButton);

        // add panels to JFrame
        add(customerIdJPanel);
        add(nameJPanel);
        add(buttonJPanel);

        // delete button listener
        // create instance of Login class
        deleteJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    // establish connection to database
                    connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
                    // create prepared statement for deleting data from the table
                    pstat = connection.prepareStatement("DELETE FROM customer WHERE idCust=?");
                    customerIDText = customerIdTextField.getText();
                    customerID = Integer.parseInt(customerIDText); // change string input into integer
                    pstat.setInt(1,customerID);
                    // delete data from the table
                    i = pstat.executeUpdate();
                    System.out.println(i + " record successfully removed from the table");
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
                Login login = new Login("Login");
                login.setVisible(true);
                login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                login.setSize(550,400);
                login.setLocation(500,400);
                dispose();
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
    // main
    public static void main(String args[]){
        DeleteAccount deleteAccount = new DeleteAccount("Delete Account");
        deleteAccount.setVisible(true);
        deleteAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteAccount.setSize(550,400);
        deleteAccount.setLocation(500,400);
    } // end main
}// end class
