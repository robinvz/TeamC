package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.UserDao;
import be.kdg.trips.utility.ImageChecker;
import be.kdg.trips.utility.MailSender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public class UserBLImpl implements UserBL
{
    private static final Logger logger = Logger.getLogger(UserBLImpl.class);

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public User createUser(User user) throws TripsException {
        if(isUnexistingUser(user.getEmail()))
        {
            user.setEmail(user.getEmail().toLowerCase());
            userDao.saveOrUpdateUser(user);
        }
        return user;
    }

    @Override
    public User findUser(String email) throws TripsException {
        return userDao.getUser(email);
    }

    @Override
    public List<User> findUsersByKeyword(String keyword, User user) throws TripsException {
        List<User> users = new ArrayList<>();
        if(isExistingUser(user.getEmail()))
        {
            users = userDao.getUsersByKeyword(keyword, user);
        }
        return users;
    }

    @Transactional
    @Override
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String country, byte[] profilePicture) throws TripsException {
        if(isExistingUser(user.getEmail()))
        {
            if(!firstName.equals(""))
            {
                user.setFirstName(firstName);
            }
            if(!lastName.equals(""))
            {
                user.setLastName(lastName);
            }
            if(!street.equals(""))
            {
                user.getAddress().setStreet(street);
            }
            if(!houseNr.equals(""))
            {
                user.getAddress().setHouseNr(houseNr);
            }
            if(!city.equals(""))
            {
                user.getAddress().setCity(city);
            }
            if(!postalCode.equals(""))
            {
                user.getAddress().setPostalCode(postalCode);
            }
            if(!country.equals(""))
            {
                user.getAddress().setCountry(country);
            }
            if(profilePicture!=null)
            {
                if(ImageChecker.isValidImage(profilePicture))
                {
                    user.setProfilePicture(profilePicture);
                }
            }
            userDao.saveOrUpdateUser(user);
        }
    }

    @Transactional
    @Override
    public void deleteUser(User user) throws TripsException {
        if(isExistingUser(user.getEmail()))
        {
            userDao.deleteUser(user.getId());
        }
    }

    @Override
    public boolean checkLogin(String email, String password) throws TripsException {
        try
        {
            User user = userDao.getUser(email);
            if(user.checkPassword(password))
            {
                return true;
            }
        }
        catch (TripsException ex)
        {

        }
        return false;
    }

    @Transactional
    @Override
    public void changePassword(User user, String oldPassword, String newPassword) throws TripsException {
        if (isExistingUser(user.getEmail()))
        {
            if(newPassword!=null){
                if(user.checkPassword(oldPassword))
                {
                    user.setPassword(newPassword);
                    userDao.saveOrUpdateUser(user);
                }
                else
                {
                    logger.warn("User " + user.getEmail() + " tried to change passwords, but entered the wrong old password.");
                    throw new TripsException("Passwords didn't match");
                }
            }
        }
    }

    @Override
    public void forgotPassword(String email) throws TripsException, MessagingException {
        if(isExistingUser(email))
        {
            User user = userDao.getUser(email);
            logger.warn("User " + user.getEmail() + " asked to retrieve his/her password by mail.");
            MailSender.sendMail("password retrieved", "Your password is '" + user.getPassword() + "'", user.getEmail());
        }
    }

    /**
     * Puts the user's current position on Google Maps in the database
     *
     * @param user the user who's position is to be set in the database
     * @param latitude google maps latitude
     * @param longitude google maps longitude
     */
    @Transactional
    @Override
    public void setUsersCurrentPosition(User user, double latitude, double longitude) throws TripsException {
        if(isExistingUser(user.getEmail())){
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            userDao.saveOrUpdateUser(user);
        }
    }

    @Override
    public boolean isExistingUser(String email) throws TripsException {
        return userDao.isExistingUser(email);
    }

    @Override
    public boolean isUnexistingUser(String email) throws TripsException {
        return userDao.isUnexistingUser(email);
    }
}