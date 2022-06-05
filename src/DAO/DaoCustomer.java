package DAO;

import Model.Customer;
import Utils.DBConnection;
import Utils.MasterList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Dao customer.
 */
public class DaoCustomer {


    /**
     * Creates the customer to be inserted in the database.
     *
     * @param customer the customer to be created
     */
    public static void createCustomers(Customer customer){
        List<Customer> customerArrayList = MasterList.customers;
        try {
        System.out.println("Creating Customer");
        String create ="INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID ) VALUES (?,?,?,?,?,?," +
                "?, ?, ?, ? )";
        PreparedStatement Query;

        Query = DBConnection.getConnection().prepareStatement(create);
        Query.setInt(1, customer.getCustomerID());
        Query.setString(2,customer.getCustomerName());
        Query.setString(3,customer.getCustomerAddress());
        Query.setString(4, customer.getCustomerPostCode());
        Query.setString(5, customer.getCustomerPhoneNumber());
        Query.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        Query.setString(7, MasterList.currentUser.getUserName());
        Query.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        Query.setString(9, MasterList.currentUser.getUserName());
        Query.setInt(10, customer.getCustomerDivisionID());
        Query.executeUpdate();
        customerArrayList.add(customer);
        MasterList.customers = customerArrayList;
        System.out.println("Customer Added");
            MasterList.daoReport("Customer ", Timestamp.valueOf(LocalDateTime.now()));
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
            }


    /**
     * Instantiates a new Dao customer.
     */
    public DaoCustomer(){}

    /**
     * Loads customers from the database and places them into the master list for use in the program
     */
    public static void loadCustomers(){
        System.out.println("Loading Customers");

        try{
            String getCustomerListQuery = "Select * from customers";
            PreparedStatement Query = DBConnection.getConnection().prepareStatement(getCustomerListQuery);
            ResultSet resultSet = Query.executeQuery();

            while (resultSet.next()) {
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String customerAddress = resultSet.getString("Address");
                String customerPostCode = resultSet.getString("Postal_Code");
                String customerPhoneNumber = resultSet.getString("Phone");
                int customerDivisionID = resultSet.getInt("Division_ID");
                int customerCountry = MasterList.findCountry(resultSet.getInt("Division_ID"));
                Customer C = new Customer(customerID,customerName,customerAddress,customerPostCode,customerPhoneNumber,customerDivisionID, customerCountry);
                MasterList.customers.add(C);
            }MasterList.daoReport("Customer ", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates the customer in the database and the master list
     *
     * @param customer the customer to be updated
     */
    public static void updateCustomer (Customer customer)  {


        try {
            System.out.println("Updating Appointment");
            String update = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement Query;

            Query = DBConnection.getConnection().prepareStatement(update);
            Query.setString(1, customer.getCustomerName());
            Query.setString(2,customer.getCustomerAddress());
            Query.setString(3,customer.getCustomerPostCode());
            Query.setString(4,customer.getCustomerPhoneNumber());
            Query.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            Query.setString(6, MasterList.currentUser.getUserName());
            Query.setInt(7,customer.getCustomerDivisionID());
            Query.setInt(8,customer.getCustomerID());
            Query.executeUpdate();
            Customer.updateCustomer(customer.getCustomerID(), customer);
            //System.out.println(MasterList.appointments.size());
            System.out.println("Customer Updated");
            MasterList.daoReport("Customer ", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the customer from the database and the master list. Error checking is done before calling this method.
     *
     * @param customer the customer to be deleted
     */
    public static void deleteCustomer(Customer customer) {

        List<Customer> customerList = MasterList.customers;

        try {
            System.out.println("Deleting Customer");
            String delete =
                    "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement Query;

            Query = DBConnection.getConnection().prepareStatement(delete);
            Query.setInt(1,customer.getCustomerID());
            int resultSet = Query.executeUpdate();

            customerList.remove(customer);
            MasterList.customers = customerList;
            System.out.println("Customer Removed");
            MasterList.daoReport("Customer ", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



}
