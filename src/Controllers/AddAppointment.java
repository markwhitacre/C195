package Controllers;

import DAO.DaoAppointment;
import Model.Appointment;
import Model.Contact;
import Model.Customer;
import Utils.MasterList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Class to add appointments
 */
public class AddAppointment implements Initializable {


    /**
     * The constant time formatter to keep all DateTimes Consistent.
     */
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private TextField newApptUserIDText;

    @FXML
    private TextField newApptTypeText;

    @FXML
    private TextField newApptLocationText;

    @FXML
    private TextField newApptDescriptionText;

    @FXML
    private TextField newApptTitleText;

    @FXML
    private ComboBox<Contact> newApptContactDropdown;

    @FXML
    private ComboBox<Customer> newApptCustomerIDDropdown;

    @FXML
    private TextField newApptIDText;

    @FXML
    private DatePicker newApptEndDate;

    @FXML
    private DatePicker newApptStartDate;

    @FXML
    private ComboBox<LocalTime> newApptStartTimeDropdown;

    @FXML
    private ComboBox<LocalTime> newApptEndTimeDropdown;

    @FXML
    private Button apptSaveButton;

    /**
     * Checks if the appointment is valid or overlaps another, then adds it to the Master list and the Database.
     * Throws appropriate errors if it is not.
     *
     * @param event the button is clicked
     * @throws IOException when then next screen cannot be loaded properly
     */
    @FXML
    void addAppointment(MouseEvent event) throws IOException {

        Appointment appointment = new Appointment(
                Integer.parseInt(newApptIDText.getText()),
                newApptTitleText.getText(),
                newApptDescriptionText.getText(),
                newApptLocationText.getText(),
                newApptTypeText.getText(),
                timestamp(newApptStartDate.getValue(),newApptStartTimeDropdown.getValue()),
                timestamp(newApptEndDate.getValue(),newApptEndTimeDropdown.getValue()),
                newApptContactDropdown.getValue().getContactID(),
                newApptCustomerIDDropdown.getValue().getCustomerID(),
                MasterList.currentUser.getUserID());



        if((validAppointment(appointment)) && (!overlappingAppointment(appointment))) {

            DaoAppointment.addAppointment(appointment);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentView.fxml"));
            Controllers.AppointmentView controller = new Controllers.AppointmentView();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else{
            if(!validAppointment(appointment)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Appointment Time");
                alert.setHeaderText("Please ensure your appointment is between the hours of 8 and 22 EST, Monday through Friday");
                alert.showAndWait();

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Appointment Time");
                alert.setHeaderText("This appointment overlaps with another");
                alert.showAndWait();


            }
        }
    }

    /**
     * checks if the appointment is valid
     *
     * @param appointment the appointment that is being checked
     * @return true if the appointment is valid
     */
    public static boolean validAppointment(Appointment appointment) {

        ZonedDateTime start = appointment.getApptStart().toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime end = appointment.getApptEnd().toLocalDateTime().atZone(ZoneId.systemDefault());
        start = start.withZoneSameInstant(ZoneId.of("America/New_York"));
        end = end.withZoneSameInstant(ZoneId.of("America/New_York"));


        return( start.getDayOfWeek() != DayOfWeek.SUNDAY && start.getDayOfWeek() != DayOfWeek.SATURDAY
                && end.getDayOfWeek() != DayOfWeek.SUNDAY && end.getDayOfWeek() != DayOfWeek.SATURDAY
                && start.getHour() <= 22 && start.getHour() >= 8 && end.getHour() <= 22 && end.getHour() >= 8);
    }

    /**
     * checks if the appointment overlaps another one in the master list
     *
     * @param appointment the appointment to be checked
     * @return false if the appointment does not overlap
     */
    public static boolean overlappingAppointment(Appointment appointment){
        boolean check = false;
        for (Appointment a: MasterList.appointments
             ) {
           if  (((appointment.getApptEnd().after(a.getApptStart())) || (appointment.getApptStart().before(a.getApptEnd())))
            && (appointment.getApptCustomer() == a.getApptCustomer())
                    && ((appointment.getApptStart().toLocalDateTime().toLocalDate().equals(a.getApptStart().toLocalDateTime().toLocalDate()))
                    || (appointment.getApptEnd().toLocalDateTime().toLocalDate().equals(a.getApptEnd().toLocalDateTime().toLocalDate())))){


                check = true;
                break;
            }
        }
        return check;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setNewApptCustomerIDDropdown();
        setNewApptUserIDText();
        setNewApptContactDropdown();
        setApptID();
        setTimeDropdowns();
    }
    private void setNewApptCustomerIDDropdown(){
        ObservableList<Customer> appointmentObservableList = FXCollections.observableArrayList(MasterList.getCustomers());
        newApptCustomerIDDropdown.setItems(appointmentObservableList);

    }

    private void setNewApptUserIDText(){
     newApptUserIDText.setText(MasterList.currentUser.getUserName());
     newApptUserIDText.setDisable(true);
    }

    private void setNewApptContactDropdown(){
        ObservableList<Contact> contactObservableList = FXCollections.observableArrayList(MasterList.getContacts());
        newApptContactDropdown.setItems(contactObservableList);
    }

    /**
     * sets the appointment ID by adding 2 to the largest existing Appointment ID in the master list
     */
    private void setApptID(){
        int max = -1;
        for (Appointment a : MasterList.appointments
             ) {
            if (a.getApptID() >= max){
                max = a.getApptID() + 2;
            }
        }
        newApptIDText.setText(String.valueOf(max));
    }

    /**
     * Collects start or end date and time, and converts them to Timestamp
     * @param date  start or end date
     * @param time start or end time
     * @return timestamp
     */
    private Timestamp timestamp(LocalDate date, LocalTime time){

        return Timestamp.valueOf(LocalDateTime.of(date,time).format(dateTimeFormatter));


    }

    /**
     * sets the dropdowns by creating a list and incrementing down hourly from 23 to 0
     */
    private void setTimeDropdowns(){
        LocalTime time = LocalTime.of(0, 0);
        ObservableList<LocalTime> timeObservableList = FXCollections.observableArrayList(time);
        while (!time.equals(LocalTime.of(23,0))){
            time = time.plusMinutes(60);
            timeObservableList.add(time);
        }
        newApptStartTimeDropdown.setItems(timeObservableList);
        newApptEndTimeDropdown.setItems(timeObservableList);

    }



}
