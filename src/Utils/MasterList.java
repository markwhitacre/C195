package Utils;

import Controllers.AddAppointment;
import DAO.*;
import Model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Master list.
 */
public class MasterList {


    /**
     * Instantiates a new Master list.
     */
    public MasterList() {
    }

    /**
     * Gets customers list.
     *
     * @return customer list
     */
    public static List<Customer> getCustomers() {
        return customers;
    }

    /**
     * Gets appointments.
     *
     * @return the appointment list
     */
    public static List<Appointment> getAppointments() {
             return appointments;

    }

    /**
     * Gets contacts.
     *
     * @return the contacts list
     */
    public static List<Contact> getContacts() {
        return contacts;
    }

    /**
     * Gets countries.
     *
     * @return the countries list
     */
    public static List<Country> getCountries() {
        return countries;
    }

    /**
     * Gets divisions.
     *
     * @return the divisions list
     */
    public static List<Division> getDivisions() {
        return divisions;
    }

    /**
     * Master customer list
     */
    public static List<Customer> customers = new ArrayList<>();
    /**
     * Master appointment list
     */
    public static List<Appointment> appointments = new ArrayList<>();
    /**
     * Master contact list
     */
    public static List<Contact> contacts = new ArrayList<>();
    /**
     * Master country list
     */
    public static List<Country> countries = new ArrayList<>();
    /**
     * Master first level division list
     */
    public static List<Division> divisions = new ArrayList<>();
    /**
     * Master User list
     */
    public static List<User> users = new ArrayList<>();
    /**
     * The current user
     */
    public static User currentUser;

    /**
     * Sets master lists. is called at the start of the program
     */
    public static void setMasterLists() {
        DaoUser.loadUsers();
        DaoAppointment.loadAppointments();
        DaoContact.loadContacts();
        DaoCountry.loadCountries();
        DaoDivision.loadDivisions();
        DaoCustomer.loadCustomers();

    }

    /**
     * Finds the country associated with the first level division. Used when filtering combo box selections
     *
     * @param index the first level division index
     * @return the Country ID
     */
    public static int findCountry(int index) {
        for (Division d: MasterList.divisions
             ) {
            if (index == d.getDivisionID()){
                return d.getCountryID();
            }
        }
        return 0;
    }

    /**
     * My selection for the extra report. Lists what database table was accessed, with a timestamp.
     *
     * @param s the Database table that was affected
     * @param t the timestamp that the table was accessed.
     * @throws IOException the file does not load correctly.
     */
    public static void daoReport (String s, Timestamp t) throws IOException {
        File file = new File("src/DAOreport.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter daoReport = new BufferedWriter(fileWriter);
        if (file.createNewFile()){
            daoReport(s,t);
        }

        daoReport.newLine();
        daoReport.write(s + " database interaction occurred at: " + t.toLocalDateTime().format(AddAppointment.dateTimeFormatter));
        daoReport.close();
    }







}
