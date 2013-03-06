package be.kdg.trips.model.address;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface AddressInterface {
    public String getStreet();
    public void setStreet(String street);
    public String getHouseNr();
    public void setHouseNr(String houseNr);
    public String getCity();
    public void setCity(String city);
    public String getPostalCode();
    public void setPostalCode(String postalCode);
    public String getCountry();
    public void setCountry(String country);
}
