package be.kdg.trips.model.trip;

import be.kdg.trips.model.Nullable;
import be.kdg.trips.model.user.NullUser;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class NullTrip extends Trip implements Nullable {
    private static NullTrip theNullTrip = new NullTrip();

    public static Trip getInstance() {
        return theNullTrip;
    }

    private NullTrip() {
        super("Invalid title", "Invalid description", TripPrivacy.PRIVATE, NullUser.getInstance());
    }

    public String toString() {
        return "Invalid trip";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
