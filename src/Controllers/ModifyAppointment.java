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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


/**
 * The type Modify appointment.
 */
public class ModifyAppointment implements Initializable {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private TextField modifyApptUserIDText;

    @FXML
    private TextField modifyApptTypeText;

    @FXML
    private TextField modifyApptLocationText;

    @FXML
    private TextField modifyApptDescText;

    @FXML
    private TextField modifyApptTitleText;

    @FXML
    private ComboBox<Contact> modifyApptContactDropdown;

    @FXML
    private ComboBox<Customer> modifyApptCustomerDropdown;

    @FXML
    private TextField modifyApptIDText;

    @FXML
    private Button modifySaveButton;

    @FXML
    private DatePicker modifyApptEndDate;

    @FXML
    private DatePicker modifyApptStartDate;

    @FXML
    private ComboBox<LocalTime> modifyApptStartTimeDropdown;

    @FXML
    private ComboBox<LocalTime> modifyApptEndTimeDropdown;

    @FXML
    private Button cancelButton;

    /**
     * Updates the appointment with the information entered by the user, after running validation checks.
     *
     * @param event the button is pressed
     * @throws IOException the appointment view screen does not load correctly
     */
    @FXML
    void modifyAppointment(MouseEvent event) throws IOException {

        Appointment appointment = new Appointment(
                Integer.parseInt(modifyApptIDText.getText()),
                modifyApptTitleText.getText(),
                modifyApptDescText.getText(),
                modifyApptLocationText.getText(),
                modifyApptTypeText.getText(),
                startTime(modifyApptStartDate.getValue(),modifyApptStartTimeDropdown.getValue()),
                endTime(modifyApptEndDate.getValue(),modifyApptEndTimeDropdown.getValue()),
                modifyApptContactDropdown.getValue().getContactID(),
                modifyApptCustomerDropdown.getValue().getCustomerID(),
                MasterList.currentUser.getUserID());

        if((AddAppointment.validAppointment(appointment)) && (!AddAppointment.overlappingAppointment(appointment))){
            DaoAppointment.updateAppointment(appointment);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentView.fxml"));
            Controllers.AppointmentView controller = new Controllers.AppointmentView();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else {
            if (!AddAppointment.validAppointment(appointment)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Appointment Time");
                alert.setHeaderText("Please ensure your appointment is between the hours of 8 and 22 EST, Monday through Friday");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Appointment Time");
                alert.setHeaderText("This appointment overlaps with another");
                alert.showAndWait();
            }
        }
    }

    /**
     * Returns the user to the appointment view
     *
     * @param event the cancel button is pressed
     * @throws IOException the Appointment view does not load correctly.
     */
    @FXML
    void returnToApptView(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentView.fxml"));
        Controllers.AppointmentView controller = new Controllers.AppointmentView();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    setModifyApptCustomerIDDropdown();
    setModifyApptUserIDText();
    setModifyApptContactDropdown();
    setTimeDropdowns();
}

    /**
     * Loads the appointment data into the textboxes and dropdowns on the modify appointment screen
     *
     * @param appointment the appointment selected by the user on the appointment view screen
     */
    void loadAppointment(Appointment appointment){
        modifyApptUserIDText.setText(Integer.toString(appointment.getApptUser()));
        modifyApptIDText.setText(Integer.toString(appointment.getApptID()));
        modifyApptTitleText.setText(appointment.getApptTitle());
        modifyApptDescText.setText(appointment.getApptDesc());
        modifyApptLocationText.setText(appointment.getApptLocation());
        modifyApptTypeText.setText(appointment.getApptType());
        for(Contact c: MasterList.contacts){
            if (c.getContactID() == appointment.getApptContact()){
                modifyApptContactDropdown.setValue(c);
            }
        }
        for(Customer c: MasterList.customers){
            if (c.getCustomerID() == appointment.getApptCustomer()){
                modifyApptCustomerDropdown.setValue(c);
            }
        }
        modifyApptStartDate.setValue(appointment.getApptStart().toLocalDateTime().toLocalDate());
        modifyApptStartTimeDropdown.setValue(appointment.getApptStart().toLocalDateTime().toLocalTime());
        modifyApptEndDate.setValue(appointment.getApptEnd().toLocalDateTime().toLocalDate());
        modifyApptEndTimeDropdown.setValue(appointment.getApptEnd().toLocalDateTime().toLocalTime());





}

    /**
     * retrieves tha master customer list and places the values into an observable list so that it maybe used in dropdowns.
     */
    private void setModifyApptCustomerIDDropdown(){
        ObservableList<Customer> appointmentObservableList = FXCollections.observableArrayList(MasterList.getCustomers());
        modifyApptCustomerDropdown.setItems(appointmentObservableList);

    }

    /**
     * retrieves the current users username from the master list and disables the text box.
     */
    private void setModifyApptUserIDText(){
        modifyApptUserIDText.setText(MasterList.currentUser.getUserName());
        modifyApptUserIDText.isDisabled();
    }

    /**
     * retrieves the contact list and places the values in an observable list for use in the dropdown
     */
    private void setModifyApptContactDropdown(){
        ObservableList<Contact> contactObservableList = FXCollections.observableArrayList(MasterList.getContacts());
        modifyApptContactDropdown.setItems(contactObservableList);
    }

    /**
     * formats the date and time selections and returns a timestamp
     * @param date the date chosen by the user from the dropdown
     * @param time the time selected by the user from the dropdown
     * @return the timestamp for the Appointment instance.
     */

    private Timestamp startTime(LocalDate date, LocalTime time){
        date.format(dateFormatter);
        time.format(timeFormatter);
        return Timestamp.valueOf(LocalDateTime.of(date,time).format(dateTimeFormatter));

    }

    /**
     * performs the same function of startTime(). Parses date and time selections into timestamps, and returns
     * @param date selected by user
     * @param time selected by user
     * @return timestamp for appointment instance.
     */
    private Timestamp endTime(LocalDate date, LocalTime time){
        date.format(dateFormatter);
        time.format(timeFormatter);
        return Timestamp.valueOf(LocalDateTime.of(date,time).format(dateTimeFormatter));

    }

    /**
     * sets the time comboboxes by loading hourly time increments into an observable list.
     */
    private void setTimeDropdowns(){
        LocalTime time = LocalTime.of(23, 0);
        ObservableList<LocalTime> timeObservableList = FXCollections.observableArrayList(time);
        while (!time.equals(LocalTime.of(0,0))){
            time = time.minusMinutes(60);
            timeObservableList.add(time);
        }
        modifyApptStartTimeDropdown.setItems(timeObservableList);
        modifyApptEndTimeDropdown.setItems(timeObservableList);

    }

}
