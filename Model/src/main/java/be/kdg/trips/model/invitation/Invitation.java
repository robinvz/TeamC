package be.kdg.trips.model.invitation;

import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
@Table(name = "T_INVITATION")
public class Invitation implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "tripId")
    @NotNull
    private Trip trip;
    @OneToOne
    @JoinColumn(name = "userId")
    @NotNull
    private User user;
    @NotNull
    private Date date;
    @NotNull
    private Answer answer;

    public Invitation(Trip trip, User user) {
        this.trip = trip;
        this.user = user;
        this.date = new Date();
        this.answer = Answer.UNANSWERED;
        user.addInvitation(this);
    }

    private Invitation() {
    }

    public Trip getTrip() {
        return trip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invitation that = (Invitation) o;

        if (!trip.equals(that.trip)) return false;
        if (!user.equals(that.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = trip.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}
