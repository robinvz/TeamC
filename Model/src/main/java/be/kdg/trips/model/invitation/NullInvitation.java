package be.kdg.trips.model.invitation;

import be.kdg.trips.model.Nullable;
import be.kdg.trips.model.trip.NullTrip;
import be.kdg.trips.model.user.NullUser;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class NullInvitation extends Invitation implements Nullable {
    private static NullInvitation theNullInvitation = new NullInvitation();

    public static Invitation getInstance() {
        return theNullInvitation;
    }

    private NullInvitation() {
        super(NullTrip.getInstance(), NullUser.getInstance());
    }

    public String toString() {
        return "Invalid invitation";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
