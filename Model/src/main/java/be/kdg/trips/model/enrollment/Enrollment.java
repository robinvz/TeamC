package be.kdg.trips.model.enrollment;

import be.kdg.trips.model.Nullable;
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
@Table(name = "T_ENROLLMENT")
public class Enrollment implements EnrollmentInterface, Serializable, Nullable
{
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

    public Enrollment(Trip trip, User user)
    {
        this.trip = trip;
        this.user = user;
        trip.addEnrollment(this);
        user.addEnrollment(this);
        this.date = new Date();
    }

    private Enrollment()
    {
    }

    @Override
    public Trip getTrip()
    {
        return trip;
    }

    @Override
    public User getUser()
    {
        return user;
    }

    @Override
    public Date getDate()
    {
        return new Date(this.date.getTime());
    }

    @Override
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Enrollment that = (Enrollment) o;

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

    @Override
    public boolean isNull() {
        return false;
    }
}
