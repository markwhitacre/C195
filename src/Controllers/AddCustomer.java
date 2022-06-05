package Controllers;

import DAO.DaoCustomer;
import Model.Country;
import Model.Customer;
import Model.Division;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Adds a customer based on the information provided on the form
 */
public class AddCustomer implements Initializable {


    @FXML
    private TextField newCustomerPhoneText;

    @FXML
    private TextField newCustomerPostText;

    @FXML
    private TextField newCustomerIDText;

    @FXML
    private TextField newCustomerNameText;

    @FXML
    private TextField newCustomerAddressText;

    @FXML
    private Label customerIDLabel;

    @FXML
    private Label newCustomerTitle;

    @FXML
    private ComboBox<Division> newCustomerDivisionDropdown;

    @FXML
    private ComboBox<Country> newCustomerCountryDropdown;

    @FXML
    private Button newCustSaveButton;

    /**
     * Adds a customer to the Master list and the Database based on the information provided
     *
     * @param event the button is clicked
     * @throws IOException the next screen cannot be loaded
     */
    @FXML
    void addCustomer(MouseEvent event) throws IOException {

        Customer customer = new Customer(
                Integer.parseInt(newCustomerIDText.getText()),
                newCustomerNameText.getText(),
                newCustomerAddressText.getText(),
                newCustomerPostText.getText(),
                newCustomerPhoneText.getText(),
                newCustomerDivisionDropdown.getValue().getDivisionID(),
                MasterList.findCountry(newCustomerDivisionDropdown.getValue().getDivisionID())
        );

        DaoCustomer.createCustomers(customer);

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
        setNewCustomerDivisionDropdown();
        setNewCustomerCountryDropdown();
        setNewCustomerIDText();


    }

    private void setNewCustomerDivisionDropdown() {
        ObservableList<Division> divisionObservableList = FXCollections.observableArrayList(MasterList.getDivisions());
        newCustomerDivisionDropdown.setItems((divisionObservableList));
    }

    private void setNewCustomerCountryDropdown() {
        ObservableList<Country> countryObservableList = FXCollections.observableArrayList(MasterList.getCountries());
        newCustomerCountryDropdown.setItems(countryObservableList);

    }

    /**
     * Sets new customer id by adding 1 to the size of the master list
     */
    void setNewCustomerIDText() {
        newCustomerIDText.setText(String.valueOf(MasterList.customers.size() + 1));

    }


    /**
     * Adjusts the division combobox based on what the user has selected in the Country combobox
     *
     * @param event the value in the Country combobox has changed
     */
    @FXML
    void selectDivision(ActionEvent event) {

        ObservableList<Division> change = FXCollections.observableArrayList();
        for (Division d: MasterList.getDivisions())
        {
            if(newCustomerCountryDropdown.getValue().getCountryID() == d.getCountryID()){
                change.add(d);
            }

        }
        newCustomerDivisionDropdown.setItems(change);
    }

}