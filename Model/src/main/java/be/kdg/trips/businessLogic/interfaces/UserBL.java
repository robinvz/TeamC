package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.util.List;
import java.util.Set;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface UserBL
{
    public User createUser(User user) throws TripsException;

    public User findUser(String email) throws TripsException;
    public List<User> findUsersByKeyword(String keyword, User user) throws TripsException;
    public boolean checkLogin(String email, String password);

    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String country, byte[] profilePicture) throws TripsException;
    public void setUserPosition(User user, double latitude, double longitude) throws TripsException;
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException;
    public void forgotPassword(String email) throws TripsException, MessagingException;

    public void deleteUser(User user) throws TripsException;

    public boolean isExistingUser(String email) throws TripsException;
    public boolean isUnexistingUser(String email) throws TripsException;
}
