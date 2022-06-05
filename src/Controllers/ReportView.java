package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Shows the selected reports to the user.
 */
public class ReportView {


    @FXML
    private TextArea textArea;

    /**
     * Instantiates a new Report view.
     */
    public ReportView() {
    }

    /**
     * Appt report.
     *
     * @param event the event
     */
    @FXML
    void apptReport(MouseEvent event) {

        textArea.clear();

        try {
            Scanner scanner = new Scanner(new File("src/apptReport.txt"));
            while(scanner.hasNextLine()){
                textArea.appendText(scanner.nextLine());
                textArea.appendText("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Contact report.
     *
     * @param event the event
     */
    @FXML
    void contactReport(MouseEvent event) {
        textArea.clear();

        try {
            Scanner scanner = new Scanner(new File("src/contactAppts.txt"));
            while(scanner.hasNextLine()){
                textArea.appendText(scanner.nextLine());
                textArea.appendText("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Database report.
     *
     * @param event the event
     */
    @FXML
    void databaseReport(MouseEvent event) {

        textArea.clear();

        try {
            Scanner scanner = new Scanner(new File("src/DAOreport.txt"));
            while(scanner.hasNextLine()){
                textArea.appendText(scanner.nextLine());
                textArea.appendText("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Login report.
     *
     * @param event the event
     */
    @FXML
    void loginReport(MouseEvent event) {

        textArea.clear();

        try {
            Scanner scanner = new Scanner(new File("src/login_activity.txt"));
            while(scanner.hasNextLine()){
                textArea.appendText(scanner.nextLine());
                textArea.appendText("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Return to main.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    void returnToMain(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AppointmentView.fxml"));
        Controllers.AppointmentView controller = new Controllers.AppointmentView();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }


}
