package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.UserBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.address.Address;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public User createUser(String email, String password) throws TripsException {
        User user = null;
        if(isUnexistingUser(email))
        {
            user = new User(email.toLowerCase(),password);
            userDao.saveOrUpdateUser(user);
        }
        return user;
    }

    @Override
    public User findUser(String email) throws TripsException {
        User user = getUser(email);
        if(!user.isNull())
        {
            return user;
        }
        else
        {
            throw new TripsException("User with email '"+email+"' doesn't exist");
        }
    }

    @Override
    public boolean checkLogin(String email, String password){
        User user = getUser(email);
        if(!user.isNull() && user.checkPassword(password))
        {
            return true;
        }
        return false;
    }

    @Override
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String province, String country) throws TripsException {
        if (isExistingUser(user.getEmail()))
        {
            if(firstName!=null)
            {
                user.setFirstName(firstName);
            }
            if(lastName!=null)
            {
                user.setLastName(lastName);
            }
            user.setAddress(new Address(street, houseNr, city, postalCode, province, country));
            userDao.saveOrUpdateUser(user);
        }
    }

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
                    throw new TripsException("Passwords didn't match");
                }
            }
        }
    }

    @Override
    public void deleteUser(User user) throws TripsException {
        if(isExistingUser(user.getEmail()))
        {
            userDao.deleteUser(user);
        }
    }

    @Override
    public boolean isExistingUser(String email) throws TripsException {
        if(getUser(email).isNull())
        {
            throw new TripsException("User with email '"+email+"' doesn't exist");
        }
        return true;
    }

    private User getUser(String email)
    {
        return userDao.getUser(email.toLowerCase());
    }

    private boolean isUnexistingUser(String email) throws TripsException
    {
        if(!getUser(email).isNull())
        {
            throw new TripsException("User with email '"+email+"' already exists");
        }
        return true;
    }
}
