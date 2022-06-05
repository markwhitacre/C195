package DAO;

import Model.Country;
import Utils.DBConnection;
import Utils.MasterList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


/**
 * The type Dao country.
 */
public class DaoCountry {

    /**
     * Instantiates a new Dao country.
     */
    public DaoCountry(){}

    /**
     * loads the countries from the database. Since these are provided to the user, there are no other
     * CRUD functions in this class.
     */
    public static void loadCountries(){
        System.out.println("Loading Countries");

        try{
            String getCountriesListQuery = "Select * from countries";
            PreparedStatement Query = DBConnection.getConnection().prepareStatement(getCountriesListQuery);
            ResultSet resultSet = Query.executeQuery();

            while (resultSet.next()) {
                int divisionID = resultSet.getInt("Country_ID");
                String divisionName = resultSet.getString("Country");
                Country C = new Country(divisionID, divisionName);
                MasterList.countries.add(C);
            }MasterList.daoReport("Country ", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }


}
