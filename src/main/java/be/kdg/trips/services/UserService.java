package be.kdg.trips.services;

import be.kdg.trips.exceptions.UserException;
import be.kdg.trips.model.user.User;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface UserService {
    public void createUser(String email, String password) throws UserException;

    public User findUser(String email) throws UserException;

    public void updateUser(User user) throws UserException;

    public boolean checkLogin(String email, String password);

}
