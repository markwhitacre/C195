package DAO;

import Model.User;
import Utils.DBConnection;
import Utils.MasterList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The type Dao user.
 */
public class DaoUser {

    /**
     * Instantiates a new Dao user.
     */
    public DaoUser(){}

    /**
     * Load users from the database to the master list. Since these are authorized elsewhere, there are no additional
     * CRUD functions in this class.
     */
    public static void loadUsers(){
        System.out.println("Loading Users");

        try{
            String getUserListQuery = "Select * from users";
            PreparedStatement Query = DBConnection.getConnection().prepareStatement(getUserListQuery);
            ResultSet resultSet = Query.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt("User_ID");
                String userName = resultSet.getString("User_Name");
                String userPassword = resultSet.getString("Password");
                User U = new User(userID,userName,userPassword);
                MasterList.users.add(U);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
