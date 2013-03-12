package be.kdg.trips.model.trip;

import be.kdg.trips.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Entity
public class TimeBoundTrip extends Trip implements Serializable {
    @Future(message = "Start date must be in the future")
    @NotNull
    private Date startDate;
    @Future(message = "End date must be in the future")
    @NotNull
    private Date endDate;

    public TimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) {
        super(title, description, privacy, organizer);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate, byte[] image) {
        super(title, description, privacy, organizer, image);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimeBoundTrip() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean isActive(){
        Date now = new Date();
        if(startDate.before(now) && endDate.after(now)){
            return true;
        }
        return false;
    }

    @Override
    public boolean isTimeBoundTrip()
    {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TimeBoundTrip that = (TimeBoundTrip) o;

        if (!endDate.equals(that.endDate)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }
}
