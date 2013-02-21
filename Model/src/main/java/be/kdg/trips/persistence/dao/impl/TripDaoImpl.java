package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.model.trip.*;
import be.kdg.trips.model.user.NullUser;
import be.kdg.trips.persistence.dao.interfaces.TripDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Repository
public class TripDaoImpl implements TripDao{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveOrUpdateTrip(Trip trip) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.saveOrUpdate(trip);
        tx.commit();
        session.close();
    }


     public List getPublicTrips(){
         Session session = sessionFactory.openSession();
         Query query = session.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations WHERE t.privacy = 0 AND t.published = 1");
         List<Trip> trips = query.list();
         session.close();
         return trips;
     }

    public List getProtectedTrips(){
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations WHERE t.privacy = 1 AND t.published = 1 ");
        List<Trip> trips = query.list();
        session.close();
        return trips;
    }

    public List getProtectedTripsWithoutDetails(){
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM Trip t WHERE t.privacy = 1 AND t.published = 1");
        List<Trip> trips = query.list();
        session.close();
        return trips;
    }

/*    @Override
    public List getAllTimelessNonPrivateTrips() {
        Session session = sessionFactory.openSession();
        List<Trip> trips = session.createCriteria(TimelessTrip.class).add(Restrictions.ne("privacy", TripPrivacy.PRIVATE)).addOrder(Order.asc("title")).list();
        session.close();
        return trips;
    }*/


    @Override
    public List getNonPrivateTripsByKeyword(String keyword) {
        Session session = sessionFactory.openSession();
  /*      List<Trip> trips = session.createCriteria(Trip.class)
                .add(Restrictions.disjunction()
                        .add(Restrictions.ilike("title", "%" + keyword + "%"))
                        .add(Restrictions.ilike("description", "%" + keyword + "%"))
                       //to do: checken of het keyword in labels komt
                ).add(Restrictions.ne("privacy", TripPrivacy.PRIVATE))
                .list();*/

        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("FROM Trip t LEFT OUTER JOIN t.labels label WHERE lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword");
        query.setParameter("keyword","%"+keyword+"%");
        List<Trip> trips = query.list();
        tx.commit();
        session.close();
        return trips;
    }

    @Override
    public Trip getTrip(int id) {
        Session session = sessionFactory.openSession();
        Trip trip = (Trip) session.createCriteria(Trip.class).add(Restrictions.eq("id", id)).uniqueResult();
        session.close();
        if (trip == null) {
            trip = NullTrip.getInstance();
        }
        return trip;
    }

    @Override
    public void deleteTrip(Trip trip) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.delete(trip);
        tx.commit();
        session.close();
    }
}
