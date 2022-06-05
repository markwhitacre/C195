package Model;


/**
 * The type Country.
 */
public class Country {

    private final int countryID;
    private final String country;


    /**
     * Instantiates a new Country.
     *
     * @param countryID the country ID
     * @param country   the country Name
     */
    public Country(int countryID, String country){
        this.countryID = countryID;
        this.country = country;

    }

    /**
     * Gets country ID.
     *
     * @return the country ID
     */
    public int getCountryID() {
        return countryID;
    }

    @Override
    public String toString() {
        return country;
    }
}
