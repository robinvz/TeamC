package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.UserDao;
import be.kdg.trips.utility.ImageChecker;
import be.kdg.trips.utility.MailSender;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public class UserBLImpl implements UserBL
{
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public User createUser(User user) throws TripsException {
        if(isUnexistingUser(user.getEmail()))
        {
            user.setEmail(user.getEmail().toLowerCase());
            userDao.saveOrUpdateUser(user);
        }
        return user;
    }

    @Override
    public User findUser(String email) throws TripsException
    {
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

    @Override
    public boolean checkLogin(String email, String password)
    {
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

    @Override
    @Transactional
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

    @Override
    @Transactional
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
                    throw new TripsException("Passwords didn't match");
                }
            }
        }
    }

    @Override
    public void forgotPassword(String email) throws TripsException, MessagingException
    {
        if(isExistingUser(email))
        {
            List<InternetAddress[]> recipients = new ArrayList<>();
            recipients.add(InternetAddress.parse(email));
            String password = userDao.getUser(email).getPassword();
            MailSender.sendMail("password retrieved", "Your password is '" + password + "'", recipients);
        }
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws TripsException {
        if(isExistingUser(user.getEmail()))
        {
            userDao.deleteUser(user.getId());
        }
    }

    @Override
    public boolean isExistingUser(String email) throws TripsException
    {
        return userDao.isExistingUser(email);
    }

    @Override
    public boolean isUnexistingUser(String email) throws TripsException
    {
        return userDao.isUnexistingUser(email);
    }
}
