package DAO;

import Model.Appointment;
import Utils.DBConnection;
import Utils.MasterList;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * The type Dao appointment.
 */
public class DaoAppointment {


    /**
     * Instantiates a new Dao appointment.
     */
    public DaoAppointment() {
    }

    /**
     * Loads appointments from the database, and places them in a master list, and the appointment report.
     */
    public static void loadAppointments(){
           System.out.println("Loading Appointments");
        try

    {
        String getAppointmentListQuery = "Select * from appointments";
        PreparedStatement Query = DBConnection.getConnection().prepareStatement(getAppointmentListQuery);
        ResultSet resultSet = Query.executeQuery();

        while (resultSet.next()) {

            int apptID = resultSet.getInt("Appointment_ID");
            String apptTitle = resultSet.getString("Title");
            String apptDesc = resultSet.getString("Description");
            String apptLocation = resultSet.getString("Location");
            int apptContact = resultSet.getInt("Contact_ID");
            String apptType = resultSet.getString("Type");
            Timestamp apptStart = resultSet.getTimestamp("Start");
            Timestamp apptEnd = resultSet.getTimestamp("End");
            int apptCustomer = resultSet.getInt("Customer_ID");
            int apptUser = resultSet.getInt("User_ID");
            Appointment A = new Appointment(apptID, apptTitle, apptDesc, apptLocation,
                    apptType, apptStart, apptEnd, apptContact, apptCustomer, apptUser);
            MasterList.appointments.add(A);

            }MasterList.daoReport("Appointment", Timestamp.valueOf(LocalDateTime.now()));

    } catch(
                SQLException | IOException e)

    {
        e.printStackTrace();
    }

}

    /**
     * Adds and appointment to the master list and the database. Will trigger an error if the instance already exists.
     *
     * @param appointment the appointment to be added
     */
    public static void addAppointment(Appointment appointment) {

        List<Appointment> appointmentArrayList = MasterList.appointments;


        if(MasterList.appointments.contains(appointment)){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("This Appointment already exists");
            error.showAndWait();}
            else
                try {
                    System.out.println("Creating Appointment");
                    String create = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start," +
                            " End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID," +
                            " Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement Query = DBConnection.getConnection().prepareStatement(create);
                    Query.setInt(1, appointment.getApptID());
                    Query.setString(2, appointment.getApptTitle());
                    Query.setString(3, appointment.getApptDesc());
                    Query.setString(4, appointment.getApptLocation());
                    Query.setString(5, appointment.getApptType());
                    Query.setTimestamp(6, appointment.getApptStart());
                    Query.setTimestamp(7, appointment.getApptEnd());
                    Query.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    Query.setString(9, MasterList.currentUser.getUserName());
                    Query.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                    Query.setString(11, MasterList.currentUser.getUserName());
                    Query.setInt(12, appointment.getApptCustomer());
                    Query.setInt(13, appointment.getApptUser());
                    Query.setInt(14, appointment.getApptContact());


                    int resultSet = Query.executeUpdate();
                    appointmentArrayList.add(appointment);
                    MasterList.appointments = appointmentArrayList;
                    System.out.println(resultSet + " Appointment Added");
                    MasterList.daoReport("Appointment", Timestamp.valueOf(LocalDateTime.now()));


                }

                catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
        }

    /**
     * Updates the appointment in the master list and the database
     *
     * @param appointment the appointment to be updated
     */
    public static void updateAppointment(Appointment appointment)  {


        try {
                System.out.println("Updating Appointment");
                String update = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?," +
                        "Last_Update = CURRENT_TIMESTAMP, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                        "WHERE Appointment_ID = ?";
                PreparedStatement Query;

                Query = DBConnection.getConnection().prepareStatement(update);
                Query.setString(1, appointment.getApptTitle());
                Query.setString(2, appointment.getApptDesc());
                Query.setString(3, appointment.getApptLocation());
                Query.setString(4, appointment.getApptType());
                Query.setTimestamp(5, appointment.getApptStart());
                Query.setTimestamp(6, appointment.getApptEnd());
                Query.setString(7, MasterList.currentUser.getUserName());
                Query.setInt(8, appointment.getApptCustomer());
                Query.setInt(9, appointment.getApptUser());
                Query.setInt(10, appointment.getApptContact());
                Query.setInt(11, appointment.getApptID());
                Query.executeUpdate();
                Appointment.updateAppt(appointment.getApptID(), appointment);
                System.out.println("Customer Updated");
            MasterList.daoReport("Appointment", Timestamp.valueOf(LocalDateTime.now()));

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * Deletes the appointment from the database and the master list
     *
     * @param appointment the appointment to be deleted
     */
    public static void deleteAppointment(Appointment appointment) {

        List<Appointment> appointmentArrayList = MasterList.appointments;

        try {
            System.out.println("Deleting Appointment");
            String delete =
                    "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement Query;

            Query = DBConnection.getConnection().prepareStatement(delete);
            Query.setInt(1,appointment.getApptID());
            int resultSet = Query.executeUpdate();

            appointmentArrayList.remove(appointment);
            MasterList.appointments = appointmentArrayList;
            System.out.println("Customer Removed");
            MasterList.daoReport("Appointment", Timestamp.valueOf(LocalDateTime.now()));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
