package Model;


/**
 * The type User.
 */
public class User {

    private final int userID;
    private final String userPassword;
    private final String userName;


    /**
     * Instantiates a new User.
     *
     * @param userID       the user ID
     * @param userName     the user name
     * @param userPassword the user password
     */
    public User(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Gets user password.
     *
     * @return the user password
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

}
