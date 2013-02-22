package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface UserBL
{
    public User createUser(String email, String password) throws TripsException;

    public User findUser(String email) throws TripsException;
    public User findUserWithDetails(String email) throws TripsException;
    public boolean checkLogin(String email, String password);

    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String province, String country) throws TripsException;
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException;

    public void deleteUser(User user) throws TripsException;

    public boolean isExistingUser(String email) throws TripsException;
    public boolean isUnexistingUser(String email) throws TripsException;
}
