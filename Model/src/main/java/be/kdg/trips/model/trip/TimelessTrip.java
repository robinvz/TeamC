package be.kdg.trips.model.trip;

import be.kdg.trips.model.user.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
public class TimelessTrip extends Trip implements Serializable {
    public TimelessTrip(String title, String description, TripPrivacy privacy, User organizer) {
        super(title, description, privacy, organizer);
    }

    public TimelessTrip(){
    }
}
