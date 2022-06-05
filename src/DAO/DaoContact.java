package DAO;

import Model.Contact;
import Utils.DBConnection;
import Utils.MasterList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Dao contact.
 */
public class DaoContact {


    /**
     * Instantiates a new Dao contact.
     */
    public DaoContact(){}

    /**
     * Loads the contacts from the database. Since these are never updated or deleted, there are no other
     * CRUD functions in this class.
     */
    public static void loadContacts(){
        System.out.println("Loading Contacts");

        try{
            String getContactListQuery = "Select * from contacts";
            PreparedStatement Query = DBConnection.getConnection().prepareStatement(getContactListQuery);
            ResultSet resultSet = Query.executeQuery();

            while (resultSet.next()) {
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                Contact C = new Contact(contactID,contactName);
                MasterList.contacts.add(C);
            }MasterList.daoReport("Contact ", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }


}
