package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.UserDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUser(String email) throws TripsException
    {
        return queryUser(email.toLowerCase(), "FROM User u WHERE u.email = :email");
    }

    @Override
    public User getUserWithDetails(String email) throws TripsException
    {
        return queryUser(email.toLowerCase(), "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.enrollments LEFT JOIN FETCH u.invitations WHERE u.email = :email");
    }

    @Override
    public void saveOrUpdateUser(User user) throws TripsException
    {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.saveOrUpdate(user);
        tx.commit();
        session.close();
    }

    @Override
    public void deleteUser(User user) throws TripsException
    {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.delete(user);
        tx.commit();
        session.close();
    }

    @Override
    public boolean isExistingUser(String email) throws TripsException {
        getUser(email);
        return true;
    }

    @Override
    public boolean isUnexistingUser(String email) throws TripsException
    {
        try
        {
            getUser(email);
        }
        catch (TripsException ex)
        {
            return true;
        }
        throw new TripsException("User with email '"+email+"' already exists");
    }

    private User queryUser(String email, String queryString) throws TripsException
    {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(queryString);
        query.setParameter("email",email);
        User user = (User) query.uniqueResult();
        session.close();
        if (user == null)
        {
            throw new TripsException("User with email '"+email+"' doesn't exist");
        }
        return user;
    }
}
