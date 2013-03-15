package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.UserDao;
import org.apache.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Repository
public class UserDaoImpl implements UserDao {
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class);


    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getUser(String email) throws TripsException
    {

            Query query = entityManager.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.enrollments LEFT JOIN FETCH u.invitations WHERE u.email = :email");
            query.setParameter("email", email.toLowerCase());
            try
            {
                return (User)query.getSingleResult();
            }
            catch (NoResultException ex)
            {
                logger.warn("User with email " + email + " tried to log in, but was not registered in database");
                throw new TripsException("User with email '"+email+"' doesn't exist");
            }
            /*
            catch (JDBCConnectionException connectionEx)
            {
                logger.fatal("Could not connect to database");
                throw new RuntimeException("Could not connect to the database");
            }
            */
    }

    @Override
    public List<User> getUsersByKeyword(String keyword, User user)
    {
        Query query = entityManager.createQuery("SELECT DISTINCT u FROM User u WHERE (lower(u.email) LIKE :keyword OR lower(u.firstName) LIKE :keyword OR lower(u.lastName) LIKE :keyword) AND u <> :user");
        query.setParameter("keyword", "%"+keyword.toLowerCase()+"%");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public void saveOrUpdateUser(User user) throws TripsException {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }

    @Override
    public void deleteUser(int id) throws TripsException {
        entityManager.remove(entityManager.find(User.class, id));
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
}
