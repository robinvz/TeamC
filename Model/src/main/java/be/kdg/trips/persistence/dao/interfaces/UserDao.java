package be.kdg.trips.persistence.dao.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface UserDao {
    public User getUser(String email) throws TripsException;
    public User getUserWithDetails(String email) throws TripsException;

    public void saveOrUpdateUser(User user) throws TripsException;

    public void deleteUser(User user) throws TripsException;

    public boolean isExistingUser(String email) throws TripsException;
    public boolean isUnexistingUser(String email) throws TripsException;
}