package Controllers;

import DAO.DaoCustomer;
import Model.Appointment;
import Model.Country;
import Model.Customer;
import Model.Division;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * The type Modify customer.
 */
public class ModifyCustomer implements Initializable{

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerViewCustomerIDColumn;

    @FXML
    private TableColumn<Customer, String> customerViewNameColumn;

    @FXML
    private TableColumn<Customer, String> customerViewAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerViewPostCodeColumn;

    @FXML
    private TableColumn<Customer, String> customerViewPhoneNumberColumn;

    @FXML
    private TableColumn<Country, String> countryColumn;

    @FXML
    private TableColumn<Country, String> divisionColumn;

    @FXML
    private TextField customerViewNameText;

    @FXML
    private TextField customerViewPostCodeText;

    @FXML
    private TextField customerViewAddressText;

    @FXML
    private TextField customerViewCustIDText;

    @FXML
    private TextField customerViewPhoneNumberText;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button selectCustomerButton;

    @FXML
    private Button newCustomerButton;

    @FXML
    private ComboBox<Country> countryCombo;

    @FXML
    private ComboBox<Division> divisionCombo;

    @FXML
    private Button cancelButton;

    /**
     * Deletes the customer after checking that they do not have any appointments.
     * Provides a confirmation or error message as appropriate.
     *
     * @param event the delete button is pressed
     */
    @FXML
    void deleteCustomer(MouseEvent event) {
        Customer deleteMe = customerTable.getSelectionModel().getSelectedItem();

        if (checkForAppts(deleteMe)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot Delete");
            alert.setHeaderText("Please delete all appointments before deleting Customer");
            alert.showAndWait();
        } else {
            MasterList.customers.remove(deleteMe);
            DaoCustomer.deleteCustomer(deleteMe);
            setTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer Deleted");
            alert.setHeaderText(deleteMe.getCustomerName() + " has been deleted");
            alert.showAndWait();
        }
    }

    /**
     * Sends the user to the new  custome screen
     *
     * @param event the new button was pressed
     * @throws IOException the screen does not load correctly.
     */
    @FXML
    void goToNewCustomerScreen(MouseEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddCustomer.fxml"));
        Controllers.AddCustomer controller = new Controllers.AddCustomer();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Selects the customer to be modified and enters the vales from that customer into the dropdowns and text boxes
     * for the user to edit.
     *
     * @param event the select button is pressed.
     */
    @FXML
    void selectCustomer(MouseEvent event) {

        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        customerViewCustIDText.setText(String.valueOf(customer.getCustomerID()));
        customerViewNameText.setText(customer.getCustomerName());
        customerViewAddressText.setText(customer.getCustomerAddress());
        customerViewPostCodeText.setText(customer.getCustomerPostCode());
        customerViewPhoneNumberText.setText(customer.getCustomerPhoneNumber());
        for(Division d: MasterList.divisions){
            if (d.getDivisionID() == customer.getCustomerDivisionID()){
                divisionCombo.setValue(d);
            }
        }
        for(Country c: MasterList.countries){
            if (c.getCountryID() == customer.getCustomerCountry()){
                countryCombo.setValue(c);
            }
        }


    }


    /**
     * Updates the customer after the user had entered and selected new information. Sets the table after
     *
     * @param event the update button is pressed.
     */
    @FXML
    void updateCustomer(MouseEvent event) {
        Customer customer = new Customer(
                Integer.parseInt(customerViewCustIDText.getText()),
                customerViewNameText.getText(),
                customerViewAddressText.getText(),
                customerViewPostCodeText.getText(),
                customerViewPhoneNumberText.getText(),
                divisionCombo.getValue().getDivisionID(),
                countryCombo.getValue().getCountryID());
        DaoCustomer.updateCustomer(customer);
        setTable();

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        setCombos();

    }

    /**
     * Returns the user to the appointment view.
     *
     * @param event the button is pressed
     * @throws IOException the screen does not load correctly.
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

    /**
     * Sets the table by getting the customers from the master list and placing them in an observable one.
     */
    public void setTable(){
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(MasterList.getCustomers());
        customerTable.setItems(customerObservableList);
        customerViewCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerViewNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerViewAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerViewPostCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostCode"));
        customerViewPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
    }

    /**
     * loads the combo boxes by placing master list information into observable lists.
     */
    private void setCombos(){
        ObservableList<Division> divisionObservableList = FXCollections.observableArrayList(MasterList.getDivisions());
        divisionCombo.setItems(divisionObservableList);
        ObservableList<Country> countryObservableList = FXCollections.observableArrayList(MasterList.getCountries());
        countryCombo.setItems(countryObservableList);

       }

    /**
     * second lambda function that counts each appointment that contains the ID of the customer to be deleted
     *
     * @param check the customer to be deleted
     * @return True if the customer has any appointments.
     */
    public boolean checkForAppts(Customer check){
        int appts = (int) MasterList.appointments.stream().filter(a -> check.getCustomerID() == a.getApptCustomer()).count();
        return appts != 0;
    }

    /**
     * Changes the value of the division combo box based on what country the user has selected
     *
     * @param event the value of the country combo box has changed.
     */
    @FXML
    void selectDivision(javafx.event.ActionEvent event) {

        ObservableList<Division> change = FXCollections.observableArrayList();
        for (Division d: MasterList.getDivisions())
        {

            if(countryCombo.getValue().getCountryID() == d.getCountryID()){
                change.add(d);
            }

        }
        divisionCombo.setItems(change);
    }
}
