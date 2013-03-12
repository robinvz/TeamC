package be.kdg.trips.model.address;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
@Table(name = "T_ADDRESS")
public class Address implements Serializable
{
    @Transient
    private final String noDigitsRegEx = "^\\D*$";
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Pattern(regexp = noDigitsRegEx, message = "Street should only contain letters")
    @Size(max=50, message = "Street has a maximum amount of 50 characters")
    private String street;
    @Pattern(regexp = "^\\d{1,}[a-zA-Z]{0,1}$", message = "House number should be a number with maximum one letter")
    @Size(max=9, message = "House number has a maximum amount of 9 characters")
    private String houseNr;
    @Pattern(regexp = noDigitsRegEx, message = "City should only contain letters")
    @Size(max=50, message = "City has a maximum amount of 50 characters")
    private String city;
    @Size(max=15, message = "Postal code has a maximum amount of 15 characters")
    private String postalCode;
    @Pattern(regexp = noDigitsRegEx, message = "Country should only contain letters")
    @Size(max=50, message = "Country has a maximum amount of 50 characters")
    private String country;

    public Address(String street, String houseNr, String city, String postalCode, String country) {
        this.street = street;
        this.houseNr = houseNr;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (houseNr != null ? !houseNr.equals(address.houseNr) : address.houseNr != null) return false;
        if (postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (houseNr != null ? houseNr.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}
