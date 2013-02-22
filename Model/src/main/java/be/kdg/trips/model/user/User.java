package be.kdg.trips.model.user;

import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

import javax.persistence.*;
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
    private int id;
    @Column(unique = true, nullable = false, updatable = false)
    @Email
    private String email;
    @Column(nullable = false)
    @Size(min = 4, max = 30)
    private String password;
    @Column(updatable = false)
    private Date registerDate;
    @Pattern(regexp =  "^\\D*$")
    @Size(max = 50)
    private String firstName;
    @Pattern(regexp =  "^\\D*$")
    @Size(max = 50)
    private String lastName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addressId")
    private Address address;
    @OneToMany(mappedBy = "user")
    private Set<Enrollment> enrollments;
    @OneToMany(mappedBy = "user")
    private Set<Invitation> invitations;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.registerDate = new Date();
        this.address = new Address(null,null,null,null,null,null);
        this.enrollments = new HashSet<>();
        this.invitations = new HashSet<>();
    }

    public User() {
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

    public boolean checkPassword(String password)
    {
        if(password.equals(this.password)){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}