package Controllers;

import DAO.DaoAppointment;
import Model.Appointment;
import Model.Contact;
import Utils.MasterList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.temporal.IsoFields;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**
 * The type Appointment view.
 */
public class AppointmentView implements Initializable {

    /**
     * Observable wrapper of the Master list for collecting and editing in various functions
     */
    ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList(MasterList.appointments);

    /**
     * getter for the observable list for outside this class
     *
     * @return the Appointment Observable List
     */
    public ObservableList<Appointment> apptList(){
        return appointmentObservableList;
    }

    @FXML
    private TableView<Appointment> apptViewTable;

    @FXML
    private TableColumn<Appointment, Integer> apptViewCustomerIDColumn;

    @FXML
    private TableColumn<Appointment, Integer> apptViewApptIDColumn;

    @FXML
    private TableColumn<Appointment, String> apptViewTitleColumn;

    @FXML
    private TableColumn<Appointment, String> apptViewDescriptionColumn;

    @FXML
    private TableColumn<Appointment, String> apptViewLocationColumn;

    @FXML
    private TableColumn<Appointment, String> apptViewContactColumn;

    @FXML
    private TableColumn<Appointment, String> apptViewTypeColumn;

    @FXML
    private TableColumn<Appointment, Timestamp> apptViewStartColumn;

    @FXML
    private TableColumn<Appointment, Timestamp> apptViewEndColumn;

    @FXML
    private ToggleGroup weekOrMonth;

    @FXML
    private RadioButton apptViewWeekButton;

    @FXML
    private Button apptViewModifyButton;

    @FXML
    private Button apptViewDeleteButton;

    @FXML
    private RadioButton allRadioButton;

    @FXML
    private RadioButton futureRadioButton;

    @FXML
    private Button newAppointment;

    @FXML
    private Button reportsButton;

    @FXML
    private Button viewCustomersButton;

    /**
     * Deletes appointment, provides confirmation
     *
     * @param event the button is clicked
     */
    @FXML
    void deleteAppointment(MouseEvent event) {


        Appointment deleteMe = apptViewTable.getSelectionModel().getSelectedItem();
        MasterList.appointments.remove(deleteMe);
        DaoAppointment.deleteAppointment(deleteMe);
        apptViewTable.getItems().remove(deleteMe);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Deleted");
        alert.setHeaderText(deleteMe.getApptID() + " " + deleteMe.getApptType() + " has been deleted");
        alert.showAndWait();

    }

    /**
     * changes to the Add appointment screen
     *
     * @param event the button is pressed
     * @throws IOException the screen does not load correctly
     */
    @FXML
    void newAppointment(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddAppointment.fxml"));
        Controllers.AddAppointment controller = new Controllers.AddAppointment();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * heads to the modify appointment screen, loads the selected appointment
     *
     * @param event the button is pressed
     * @throws IOException the screen does not load correctly
     */
    @FXML
    void goToModifyApptScreen(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyAppointment.fxml"));
        Controllers.ModifyAppointment controller = new Controllers.ModifyAppointment();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        ModifyAppointment controller1 = loader.getController();
        Appointment appointment = apptViewTable.getSelectionModel().getSelectedItem();
        controller1.loadAppointment(appointment);
    }

    /**
     * Changes Table View to only show those appointments that are within the current month.
     *
     * @param event the button is selected
     */
    @FXML
    void viewByMonth(MouseEvent event) {
        ObservableList<Appointment> monthTable = FXCollections.observableArrayList();
        for (Appointment a:appointmentObservableList
             ) {
            if (a.getApptStart().toLocalDateTime().toLocalDate().getMonth() == LocalDate.now().getMonth()){

                        monthTable.add(a);
            }
        }
        setApptViewTable(monthTable);
    }

    /**
     * View by week.
     * Changes Table View to only show those appointments that are within the current week.
     *
     * @param event the button is selected
     */
    @FXML
    void viewByWeek(MouseEvent event) {
        ObservableList<Appointment> weekTable = FXCollections.observableArrayList();
        for (Appointment a:appointmentObservableList
        ) {
            if (
                    a.getApptStart().toLocalDateTime().toLocalDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)
                            == ZonedDateTime.now().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)){
                weekTable.add(a);


            }

        }setApptViewTable(weekTable);

    }

    /**
     * loads the view customer screen
     *
     * @param event the button is clicked
     * @throws IOException the screen does not load correctly
     */
    @FXML
    void viewCustomers(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyCustomer.fxml"));
        Controllers.ModifyCustomer modifyCustomer = new Controllers.ModifyCustomer();
        loader.setController(modifyCustomer);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setApptViewTable(appointmentObservableList);
        try {
            generateContactReport();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            generateApptReport();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Set the main table view with the observable list that is provided to it
     *
     * @param appointments the Observable list that is provided to it.
     */
    public void setApptViewTable(ObservableList<Appointment> appointments){
        apptViewTable.setItems(appointments);
        apptViewCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptContact"));
        apptViewApptIDColumn.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        apptViewTitleColumn.setCellValueFactory(new PropertyValueFactory<>("apptTitle"));
        apptViewDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("apptDesc"));
        apptViewLocationColumn.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        apptViewContactColumn.setCellValueFactory(new PropertyValueFactory<>("apptContact"));
        apptViewTypeColumn.setCellValueFactory(new PropertyValueFactory<>("apptLocation"));
        apptViewStartColumn.setCellValueFactory(new PropertyValueFactory<>("apptStart"));
        apptViewEndColumn.setCellValueFactory(new PropertyValueFactory<>("apptEnd"));


    }

    /**
     * generates the reports of all appointments that each contact has. resets on reload.
     * @throws IOException if the file does not exist
     */

    private void generateContactReport() throws IOException {
        File file = new File("src/contactAppts.txt");
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter custAppt = new BufferedWriter(fileWriter);
        if (file.createNewFile()){
            generateContactReport();
        }
        for (Contact c : MasterList.contacts
             ) {
            for (Appointment a : MasterList.appointments
                 ) {
                if (a.getApptContact() == c.getContactID()){
                    custAppt.write(
                            " Appt ID: " + a.getApptID() + " Title: " + a.getApptTitle() + " Type: "
                            + a.getApptType() + " Description: " + a.getApptDesc() + " Start: " + a.getApptStart() + " End: "
                            + a.getApptEnd() + " Customer: " + a.getApptCustomer()
                    );

                }
                custAppt.newLine();
            }
        } custAppt.close();


    }

    /**
     * Generates each appointment by Month and Type.
     *
     * The Lambda in here scans collects all of the distinct Appointment types, converts them to strings,
     * and adds them to a list, which is then compared against every appointment, and increments a counter,
     * which is then printed
     *
     * @throws IOException the file cannot be found
     */

    private void generateApptReport() throws IOException {
        File file = new File("src/apptReport.txt");
        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter apptReport = new BufferedWriter(fileWriter);
        if (file.createNewFile()){
            generateApptReport();
        }

        List<String> types = MasterList.appointments.stream().map(Appointment::getApptType).distinct().collect(Collectors.toList());
        for (String s:types

             ) {
            int i = 0;
            for (Appointment a:MasterList.appointments
                 ) {
                if (a.getApptType().equals(s)){
                    i++;
                }
            }
            apptReport.write("Type: " + s + " " + " Count " + i);
            apptReport.newLine();
        }

        for (Month m: Month.values()
             ) {
            int i = 0;
            for (Appointment a:MasterList.appointments
                 ) {
                if (a.getApptStart().toLocalDateTime().getMonth().equals(m)){
                    i++;
                }
            }
            if (i > 0) {
                apptReport.newLine();
                apptReport.write(i + " Appointments in " + m);
            }
        }
        apptReport.newLine();
        apptReport.close();
    }

    /**
     * Hides all appointments in the past, with the help of a lambda that prevents coding a full if loop.
     * <p>
     * A new Observable list is initiated, which then has every appointment with an ending date before the time it was
     * selected. That new list is then passed to the view table constructor.
     *
     * @param event the button is selected in some way
     */
    @FXML
    void hideOldAppts(ActionEvent event) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(MasterList.appointments);
        appointments.removeIf(appointment -> appointment.getApptEnd().before(Timestamp.valueOf(LocalDateTime.now())));
        setApptViewTable(appointments);


    }

    /**
     * resets the View Table by passing it the list at the top of the class.
     *
     * @param event the button is selected somehow.
     */
    @FXML
    void allAppts(ActionEvent event) {
        setApptViewTable(apptList());

    }

    /**
     * Go to reports screen.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    void goToReportsScreen(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ReportView.fxml"));
        Controllers.ReportView controller = new Controllers.ReportView();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

}
