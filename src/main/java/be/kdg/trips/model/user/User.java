package be.kdg.trips.model.user;

import be.kdg.trips.model.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@Component
public class User implements Nullable, Serializable {

    private int id;
    private String email;
    private String password;
    private Date registerDate;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.registerDate = new Date();
    }

    private User() {
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
