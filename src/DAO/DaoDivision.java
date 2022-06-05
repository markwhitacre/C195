package DAO;

import Model.Division;
import Utils.DBConnection;
import Utils.MasterList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * The type Dao division.
 */
public class DaoDivision {


    /**
     * Instantiates a new Dao division.
     */
    public DaoDivision() {
    }

    /**
     * Loads the first level divisions from the database. Since these are provided to the user, there are no additional
     * CRUD functions in this class.
     */
    public static void loadDivisions() {
        System.out.println("Loading Divisions");

        try {
            String getDivisionListQuery = "Select * from first_level_divisions";
            PreparedStatement Query = DBConnection.getConnection().prepareStatement(getDivisionListQuery);
            ResultSet resultSet = Query.executeQuery();

            while (resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryID = resultSet.getInt("COUNTRY_ID");
                Division D = new Division(divisionID, divisionName, countryID);
                MasterList.divisions.add(D);

            }
            MasterList.daoReport("Division ", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}

