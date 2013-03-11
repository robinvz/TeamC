package be.kdg.trips.model.user;

import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@Entity
@Table(name="T_USER")
@Component
public class User implements UserInterface, Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false, updatable = false)
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email must be valid")
    @Size(min = 4, max = 50, message = "Email must be between 4 and 50 characters")
    private String email;
    @Column(nullable = false)
    @Size(min = 4, max = 30, message = "Password must be between 4 and 30 characters")
    private String password;
    @Column(updatable = false)
    private Date registerDate;
    @Pattern(regexp =  "^\\D*$", message = "First name should only contain letters")
    @Size(max = 50, message = "First name has a maximum amount of 50 characters")
    private String firstName;
    @Pattern(regexp =  "^\\D*$", message = "Last name should only contain letters")
    @Size(max = 50, message = "Last name has a maximum amount of 50 characters")
    private String lastName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    @Valid
    private Address address;
    @OneToMany(mappedBy = "user")
    private Set<Enrollment> enrollments;
    @OneToMany(mappedBy = "user")
    private Set<Invitation> invitations;
    @Lob
    private byte[] profilePicture;
    @DecimalMin(value = "-90.00", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.00", message = "Latitude must be between -90 and 90")
    private double latitude;
    @DecimalMin(value = "-180.00", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.00", message = "Longitude must be between -180 and 180")
    private double longitude;


    public User(String email, String password) {
        this();
        this.email = email;
        this.password = password;

    }

    public User() {
        this.registerDate = new Date();
        this.enrollments = new HashSet<>();
        this.invitations = new HashSet<>();
        this.address = new Address(null,null,null,null,null);
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Date getRegisterDate() {
        return new Date(this.registerDate.getTime());
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public void addEnrollment(Enrollment enrollment)
    {
        enrollments.add(enrollment);
    }

    @Override
    public void removeEnrollment(Enrollment enrollment)
    {
        enrollments.remove(enrollment);
    }

    public Set<Invitation> getInvitations() {
        return invitations;
    }

    public void addInvitation(Invitation invitation)
    {
        invitations.add(invitation);
    }

    public void removeInvitation(Invitation invitation)
    {
        invitations.remove(invitation);
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean checkPassword(String password)
    {
        if(password.equals(this.password)){
            return true;
        }
        return false;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
