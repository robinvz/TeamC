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
public class Address implements AddressInterface, Serializable
{
    @Transient
    private final String noDigitsRegEx = "^\\D*$";
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Pattern(regexp = noDigitsRegEx)
    @Size(max=50)
    private String street;
    //check number (+ 1 karakter)
    @Size(max=9)
    private String houseNr;
    @Pattern(regexp = noDigitsRegEx)
    @Size(max=50)
    private String city;
    @Size(max=15)
    private String postalCode;
    @Pattern(regexp = noDigitsRegEx)
    @Size(max=50)
    private String province;
    @Pattern(regexp = noDigitsRegEx)
    @Size(max=50)
    private String country;

    public Address(String street, String houseNr, String city, String postalCode, String province, String country) {
        this.street = street;
        this.houseNr = houseNr;
        this.city = city;
        this.postalCode = postalCode;
        this.province = province;
        this.country = country;
    }

    public Address() {
    }


    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getHouseNr() {
        return houseNr;
    }

    @Override
    public void setHouseNr(String houseNr) {
        this.houseNr = houseNr;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String getProvince() {
        return province;
    }

    @Override
    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
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
        if (province != null ? !province.equals(address.province) : address.province != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = street != null ? street.hashCode() : 0;
        result = 31 * result + (houseNr != null ? houseNr.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}
