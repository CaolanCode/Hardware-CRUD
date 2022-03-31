package itcarlow.ie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.*;

public class ListProducts extends JFrame {
    // variables
    // panels
    private JPanel tableJPanel;
    private JPanel buttonJPanel;
    // table model
    DefaultTableModel tableModel = new DefaultTableModel();
    private JTable productJTable;
    String[] columnNames = {"Product ID", "Name", "Price", "Quantity"};
    // return button
    private JButton returnJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet;
    int productId;
    String name;
    BigDecimal price;
    int quantity;
    int i = 0;

    // constructor
    public ListProducts(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // table panel
        tableJPanel = new JPanel();
        tableModel.setColumnIdentifiers(columnNames);
        productJTable = new JTable();
        productJTable.setPreferredSize(new Dimension(400,350));
        productJTable.setModel(tableModel);
        productJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productJTable.setFillsViewportHeight(true);
        tableJPanel.add(productJTable);

        // button panel
        buttonJPanel = new JPanel();
        returnJButton = new JButton("Return to Order");
        buttonJPanel.add(returnJButton);

        // add panels to jframe
        add(tableJPanel);
        add(new JScrollPane(productJTable));
        add(buttonJPanel);

        // try catch block to show table
        try{
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
            // create prepared statement to select all data from product table
            pstat = connection.prepareStatement("SELECT * FROM product");
            resultSet = pstat.executeQuery();
            while(resultSet.next()){
                productId = resultSet.getInt("idProd");
                name = resultSet.getString("name");
                price = resultSet.getBigDecimal("price");
                quantity = resultSet.getInt("quantity");
                tableModel.addRow(new Object[]{productId,name,price,quantity});
                i++;
            }
            // error handling
            if(i<1){
                JOptionPane.showMessageDialog(null,"No Products in Store", "Error", JOptionPane.ERROR_MESSAGE);
            } else if(i==1){
                System.out.println(i + " record found in the product table");
            } else{
                System.out.println(i + " records found in the product table");
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

        // return button listener
        // create instance of CreateOrder class
        returnJButton.addMouseListener(new MouseAdapter() {
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
        ListProducts listProducts = new ListProducts("Products in Store");
        listProducts.setVisible(true);
        listProducts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listProducts.setSize(550,400);
        listProducts.setLocation(500,400);
    }// end main
}// end class
