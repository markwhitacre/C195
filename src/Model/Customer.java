package Model;

import Utils.MasterList;


/**
 * The type Customer.
 */
public class Customer {

    private  int customerID;
    private  String customerName;
    private String customerAddress;
    private  String customerPostCode;
    private  String customerPhoneNumber;
    private int customerDivision;
    private int customerCountry;


    /**
     * Gets customer country.
     *
     * @return the customer country
     */
    public int getCustomerCountry() {
        return customerCountry;
    }

    /**
     * Gets customer division.
     *
     * @return the customer division
     */
    public int getCustomerDivision() {
        return customerDivision;
    }

    /**
     * Sets customer division.
     *
     * @param customerDivision the customer division
     */
    public void setCustomerDivision(int customerDivision) {
        this.customerDivision = customerDivision;
    }

    /**
     * Instantiates a new Customer.
     *
     * @param customerID          the customer ID
     * @param customerName        the customer name
     * @param customerAddress     the customer address
     * @param customerPostCode    the customer postal code
     * @param customerPhoneNumber the customer phone number
     * @param customerDivision    the customer first level division
     * @param customerCountry     the customer country
     */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostCode, String customerPhoneNumber,
                    int customerDivision, int customerCountry){
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostCode = customerPostCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerDivision = customerDivision;
        this.customerCountry = customerCountry;


    }

    /**
     * Gets customer id.
     *
     * @return the customer id
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Gets customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Gets customer address.
     *
     * @return the customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Gets customer postal code.
     *
     * @return the customer postal code
     */
    public String getCustomerPostCode() {
        return customerPostCode;
    }

    /**
     * Gets customer phone number.
     *
     * @return the customer phone number
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Get customer first level division ID.
     *
     * @return the customer first level division ID
     */
    public int getCustomerDivisionID(){
        return customerDivision;
    }

    /**
     * Updates the customer instance that matches the customer ID in the master list.
     *
     * @param index the customer ID
     * @param cust  the customer to be updated
     */
    public static void updateCustomer(int index, Customer cust) {
        for (int i = 0; i < MasterList.getCustomers().size(); i++) {
            if (index == MasterList.getCustomers().get(i).getCustomerID()) {
                MasterList.customers.set(i, cust);
            }
        }
    }

    @Override
    public String toString() {
        return customerName;
    }

    }
