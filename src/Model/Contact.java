package Model;


/**
 * The type Contact.
 */
public class Contact {




    private final String contactName;
    private final int contactID;


    /**
     * Instantiates a new Contact.
     *
     * @param contactID   the contact ID
     * @param contactName the contact name
     */
    public Contact(int contactID, String contactName){
        this.contactID = contactID;
        this.contactName = contactName;
    }


    /**
     * Gets contact ID.
     *
     * @return the contact ID
     */
    public int getContactID() {
        return contactID;
    }
    @Override
    public String toString() {
        return contactName;
    }
}