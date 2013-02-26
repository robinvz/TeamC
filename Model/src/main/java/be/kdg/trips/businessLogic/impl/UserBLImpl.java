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
    public User findUserWithDetails(String email) throws TripsException
    {
        return userDao.getUserWithDetails(email);
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
    public void updateUser(User user, String firstName, String lastName, String street, String houseNr, String city, String postalCode, String province, String country) throws TripsException {
        if(isExistingUser(user.getEmail()))
        {
            if(firstName!=null)
            {
                user.setFirstName(firstName);
            }
            if(lastName!=null)
            {
                user.setLastName(lastName);
            }
            if(street!=null)
            {
                user.getAddress().setStreet(street);
            }
            if(houseNr!=null)
            {
                user.getAddress().setHouseNr(houseNr);
            }
            if(city!=null)
            {
                user.getAddress().setCity(city);
            }
            if(postalCode!=null)
            {
                user.getAddress().setPostalCode(postalCode);
            }
            if(province!=null)
            {
                user.getAddress().setProvince(province);
            }
            if(country!=null)
            {
                user.getAddress().setCountry(country);
            }
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
