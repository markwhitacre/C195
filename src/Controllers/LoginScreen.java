package Controllers;

import Model.Appointment;
import Model.User;
import Utils.MasterList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;


/**
 * The type Login screen.
 */
public class LoginScreen implements Initializable {

    /**
     * Sets location label based on what the system is currently reporting.
     *
     * @param locationLabel changes what the label displays
     */
    public void setLocationLabel(Label locationLabel) {

        locationLabel.setText(String.valueOf(ZoneId.systemDefault()));
    }

    @FXML
    private TextField userIDTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label userIDLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Label locationLabel;

    @FXML
    private Label loginLabel;


    /**
     * Check credentials based on what was pulled from the database on loading.
     * Logs all attempts
     * If Invalid, Alerts the user in English or French based on the system language.
     * Loads the Appointment view
     *
     * @param event the button is pressed
     * @throws IOException the filepath cannot be found, the AppointmentView does not load correctly.
     */
    @FXML
    void checkCredentials(MouseEvent event) throws IOException {
        File file = new File("src/login_activity.txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter login = new BufferedWriter(fileWriter);
        login.write(userIDTextField.getText() + " " + ZonedDateTime.now(ZoneId.systemDefault()));
        if (validUser()) {
            login.write("Successful");
            login.newLine();
            login.close();
            checkForAppointments();
            if(!checkForAppointments()){
                noAppts();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentView.fxml"));
            Controllers.AppointmentView controller = new Controllers.AppointmentView();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            login.write("Unsuccessful");
            login.newLine();
            login.close();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (System.getProperty("user.language").equals("fr")) {
                alert.setTitle("Erreur");
                alert.setHeaderText("Les informations d'identification invalides");
            }
            else
            {
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Credentials");
            }
            alert.showAndWait();
        }
    }

    /**
     * Instantiates a new Login screen.
     */
    public LoginScreen() {

        }

    /**
     * determines if the username and password entered matches one of the users in the master list
     *
     * @return true if the user is valid
     */

    private boolean validUser() {
        for (User u : MasterList.users)
            if (u.getUserName().equals(userIDTextField.getText().trim()) && u.getUserPassword().equals(passwordTextField.getText().trim())) {
                MasterList.currentUser = u;
                return true;
            }
        return false;
    }

    /**
     * Instantiates the LoginScreen, Changes text if the system language is French
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MasterList.setMasterLists();

        setLocationLabel(locationLabel);
        if (System.getProperty("user.language").equals("fr")){
            loginLabel.setText("page d'accès");
            userIDLabel.setText("nom d'utilisateur");
            passwordLabel.setText("mot de passe");
            loginButton.setText("connexion");
        }
    }

    /**
     * checks if the current user has any appointments in the next 15 minutes.
     * Sends the relavent appointment to the alert constructor if that is the case
     *
     * @return true if there is an appointment
     */
    public boolean checkForAppointments(){

        boolean check = false;

        for (Appointment a: MasterList.appointments
        ) {

            if (a.getApptStart().toLocalDateTime().toLocalTime().isBefore(LocalTime.now().plusMinutes(15)) &&
                    a.getApptStart().toLocalDateTime().toLocalTime().isAfter(LocalTime.now())
                    && a.getApptStart().toLocalDateTime().toLocalDate().isEqual(LocalDate.now())
            && MasterList.currentUser.getUserID() == a.getApptUser())
            {
                check = true;
                nextAppt(a);
                break;

            }

        }
        return check;
    }

    /**
     * sets the alert and lets the user know if there
     *
     * @param appointment the appointment
     */
    public void nextAppt(Appointment appointment){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You have an upcoming appointment");
        alert.setHeaderText(appointment.getApptID() + " " + appointment.getApptStart().toLocalDateTime().toLocalDate() + " " +
                appointment.getApptStart().toLocalDateTime().toLocalTime());
        alert.showAndWait();

    }

    /**
     * Alerts the user that there are no upcoming appointments. Called by checkCredentials().
     */
    public void noAppts(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("No Appointments");
        alert.setTitle("You have no upcoming appointments");
        alert.showAndWait();

    }
}
