package be.kdg.trips.persistence.dao.interfaces;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.user.User;

import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface UserDao {
    public User getUser(String email) throws TripsException;
    public List<User> getUsersByKeyword(String keyword, User user);

    public void saveOrUpdateUser(User user) throws TripsException;

    public void deleteUser(int id) throws TripsException;

    public boolean isExistingUser(String email) throws TripsException;
    public boolean isUnexistingUser(String email) throws TripsException;
}


