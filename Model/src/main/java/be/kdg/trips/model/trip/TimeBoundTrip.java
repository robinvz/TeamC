package be.kdg.trips.model.trip;

import be.kdg.trips.model.user.User;

import javax.persistence.*;
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "T_TIMEBOUNDTRIP_DATE", joinColumns = @JoinColumn(name = "timeBoundTripId"))
    @Column(name="dates_VALUE")
    private Map<Date,Date> dates;

    public TimeBoundTrip(String title, String description, TripPrivacy privacy, User organizer, Date startDate, Date endDate) {
        super(title, description, privacy, organizer);
        dates = new HashMap<>();
        dates.put(startDate,endDate);
    }

    public TimeBoundTrip() {
    }

    public Map<Date, Date> getDates() {
        return dates;
    }

    public void setDates(Map<Date, Date> dates) {
        this.dates = dates;
    }

    public void addDates(Date startDate, Date endDate)
    {
        dates.put(startDate, endDate);
    }

    public void removeDates(Date startDate)
    {
        dates.remove(startDate);
    }

    @Override
    public boolean isActive(){
        Date now = new Date();
        for (Map.Entry datePair : dates.entrySet()) {
            if(((Date)datePair.getKey()).before(now) && ((Date)datePair.getValue()).after(now)){
                return true;
            }
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

        if (dates != null ? !dates.equals(that.dates) : that.dates != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (dates != null ? dates.hashCode() : 0);
        return result;
    }
}
