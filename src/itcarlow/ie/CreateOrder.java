package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.*;

public class CreateOrder extends JFrame{
    // variables
    // panels
    private JPanel mainJPanel;
    private JPanel productJPanel;
    private JPanel quantityJPanel;
    private JPanel submitClearJPanel;
    private JPanel listShowJPanel;
    private JPanel updateLogoutJPanel;
    // labels
    private JLabel productJLabel;
    private JLabel quantityJLabel;
    // combobox
    private JComboBox productCombobox;
    // text field
    private JTextField quantityTextField;
    // buttons
    private JButton submitJButton;
    private JButton clearJButton;
    private JButton listJButton;
    private JButton showBasketJButton;
    private JButton updateAccJButton;
    private JButton logOutJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    PreparedStatement pstatInvoice = null;
    PreparedStatement pstatProduct = null;
    PreparedStatement pstatStock = null;
    ResultSet resultSet;
    int i = 0;
    String name;
    int quantity;
    int productFk;
    BigDecimal cost;
    int stock;

    // constructor
    public CreateOrder(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // product panel and components
        productJPanel = new JPanel(new FlowLayout());
        productJLabel = new JLabel("Product");
        productCombobox = new JComboBox();
        productCombobox.setPreferredSize(new Dimension(350,40));
        productJPanel.add(productJLabel);
        productJPanel.add(productCombobox);

        // quantity panel and components
        quantityJPanel = new JPanel(new FlowLayout());
        quantityJLabel = new JLabel("Quantity");
        quantityTextField = new JTextField();
        quantityTextField.setPreferredSize(new Dimension(200,40));
        quantityJPanel.add(quantityJLabel);
        quantityJPanel.add(quantityTextField);

        // submit clear panel and buttons
        submitClearJPanel = new JPanel(new FlowLayout());
        submitJButton = new JButton("Submit Order");
        clearJButton = new JButton("Clear Screen");
        submitClearJPanel.add(submitJButton);
        submitClearJPanel.add(clearJButton);

        // list products and show basket panel
        listShowJPanel = new JPanel(new FlowLayout());
        listJButton = new JButton("List Products");
        showBasketJButton = new JButton("Show Basket");
        listShowJPanel.add(listJButton);
        listShowJPanel.add(showBasketJButton);

        // update account and logout buttons
        updateLogoutJPanel = new JPanel(new FlowLayout());
        updateAccJButton = new JButton("Update Account Details");
        logOutJButton = new JButton("Log Out");
        updateLogoutJPanel.add(updateAccJButton);
        updateLogoutJPanel.add(logOutJButton);

        // add panels to jframe top to bottoms
        add(productJPanel);
        add(quantityJPanel);
        add(submitClearJPanel);
        add(listShowJPanel);
        add(updateLogoutJPanel);

        // fill combobox with product names
        try{
            // establish connection with database
            connection = DriverManager.getConnection(DATABASE_URL, "root","root");
            // create prepared statement for retrieve all product names
            pstat = connection.prepareStatement("SELECT name FROM product");
            resultSet = pstat.executeQuery();
            while(resultSet.next()){
                productCombobox.addItem(resultSet.getString(1));
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

        // submit order button listener
        // create order in order table
        // create instance of create order class
        submitJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    // selected combobox item
                    name = (String)productCombobox.getSelectedItem();
                    // quantity int entered
                    quantity = Integer.parseInt(quantityTextField.getText());
                    // establish connection with database
                    connection = DriverManager.getConnection(DATABASE_URL, "root","root");
                    // create prepared statement to retrieve product details of order information
                    pstatProduct = connection.prepareStatement("SELECT idProd, price, quantity FROM product WHERE name=?");
                    pstatProduct.setString(1,name);
                    resultSet =  pstatProduct.executeQuery();
                    if(resultSet.next()){
                        productFk = resultSet.getInt("idProd");
                        cost = resultSet.getBigDecimal("price");
                        stock = resultSet.getInt("quantity");
                    }
                    // calculate cost of order
                    cost = cost.multiply(BigDecimal.valueOf(quantity));
                    // create prepared statement for create order
                    pstatInvoice = connection.prepareStatement("INSERT INTO invoice (quantity,cost,customerFk,productFk) VALUES(?,?,?,?)");
                    pstatInvoice.setInt(1,quantity);
                    pstatInvoice.setBigDecimal(2, cost);
                    pstatInvoice.setInt(3,Login.customerID);
                    pstatInvoice.setInt(4, productFk);
                    // insert data into table
                    i = pstatInvoice.executeUpdate();
                    System.out.println(i + " successful orders");

                    // reduce the quantity of product in the products table
                    // subtract quantity purchased from current stock
                    stock = stock - quantity;
                    pstatStock = connection.prepareStatement("UPDATE product SET quantity=? WHERE name=?");
                    pstatStock.setInt(1,stock);
                    pstatStock.setString(2,name);
                    // update data in the table
                    i = pstatStock.executeUpdate();
                    System.out.println(i + " record successfully updated in the product table");
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
                CreateOrder createOrder = new CreateOrder("Order a Product");
                createOrder.setVisible(true);
                createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createOrder.setSize(550,400);
                createOrder.setLocation(500,400);
                dispose();
            }
        });

        // clear screen button listener
        // create instance of CreateOrder class
        clearJButton.addMouseListener(new MouseAdapter() {
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

        // list products button listener
        // create instance of ListProducts class
        listJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ListProducts listProducts = new ListProducts("Products in Store");
                listProducts.setVisible(true);
                listProducts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                listProducts.setSize(550,400);
                listProducts.setLocation(500,400);
                dispose();
            }
        });

        // show basket button listener
        // create instance of ListProducts class
        showBasketJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ShowBasket showBasket = new ShowBasket("Account Basket");
                showBasket.setVisible(true);
                showBasket.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                showBasket.setSize(550,400);
                showBasket.setLocation(500,400);
                dispose();
            }
        });

        // update account details button listener
        // create instance of UpdateAccount class
        updateAccJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                UpdateAccount updateAccount = new UpdateAccount("Update Account Details");
                updateAccount.setVisible(true);
                updateAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                updateAccount.setSize(550,400);
                updateAccount.setLocation(500,400);
                dispose();
            }
        });

        // cancel button listener
        // create instance of Login class
        logOutJButton.addMouseListener(new MouseAdapter() {
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
        CreateOrder createOrder = new CreateOrder("Order a Product");
        createOrder.setVisible(true);
        createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createOrder.setSize(550,400);
        createOrder.setLocation(500,400);
    }// end main
}// end class
