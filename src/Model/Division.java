package Model;


/**
 * The type Division.
 */
public class Division {


    private int divisionID;
    private String divisionName;
    private int countryID;

    /**
     * Instantiates a new Division.
     *
     * @param divisionID   the division ID
     * @param divisionName the division name
     * @param countryID    the country ID
     */
    public Division(int divisionID, String divisionName, int countryID){
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * Gets country id for the division.
     *
     * @return the country id
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public int getDivisionID() {
        return divisionID;
    }

    @Override
    public String toString() {
        return divisionName;
    }

}
