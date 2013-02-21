package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.NullUser;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.UserDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public User getUser(String email) {
        Session session = sessionFactory.openSession();
        //PROBLEEM: werken met transactions!
        //User user = (User) session.createCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult();
        Query query = session.createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.enrollments LEFT JOIN FETCH u.invitations WHERE u.email = :email");        query.setParameter("email",email);
        User user = (User) query.uniqueResult();
        session.close();
        if (user == null) {
            user = NullUser.getInstance();
        }

        return user;
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
    public void deleteUser(User user) throws TripsException {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.delete(user);
        tx.commit();
        session.close();
    }
}
