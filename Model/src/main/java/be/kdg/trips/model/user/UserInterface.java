package be.kdg.trips.model.user;

import be.kdg.trips.model.Nullable;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface UserInterface {
    public void setPassword(String password);
    public String getEmail();
    public void setEmail(String email);
    public Date getRegisterDate();
    public String getFirstName();
    public void setFirstName(String firstName);
    public String getLastName();
    public void setLastName(String lastName);
    public Address getAddress();
    public void setAddress(Address address);
    public void addEnrollment(Enrollment enrollment);
    public void removeEnrollment(Enrollment enrollment);
}
