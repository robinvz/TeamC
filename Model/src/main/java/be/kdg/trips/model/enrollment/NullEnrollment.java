package be.kdg.trips.model.enrollment;

import be.kdg.trips.model.Nullable;
import be.kdg.trips.model.trip.NullTrip;
import be.kdg.trips.model.user.NullUser;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class NullEnrollment extends Enrollment implements Nullable {
    private static NullEnrollment theNullEnrollment = new NullEnrollment();

    public static Enrollment getInstance() {
        return theNullEnrollment;
    }

    private NullEnrollment() {
        super(NullTrip.getInstance(), NullUser.getInstance());
    }

    public String toString() {
        return "Invalid enrollment";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
