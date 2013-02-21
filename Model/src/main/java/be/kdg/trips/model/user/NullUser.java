package be.kdg.trips.model.user;

import be.kdg.trips.model.Nullable;
import org.springframework.stereotype.Component;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public final class NullUser extends User implements Nullable {
    private static NullUser theNullUser = new NullUser();

    public static User getInstance() {
        return theNullUser;
    }

    private NullUser() {
        super("Invalid email", "Invalid password");
    }

    public String toString() {
        return "Invalid user";
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
