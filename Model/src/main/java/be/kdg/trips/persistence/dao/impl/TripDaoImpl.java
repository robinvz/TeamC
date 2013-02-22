package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.trip.*;
import be.kdg.trips.persistence.dao.interfaces.TripDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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


    public List getPublicTrips()
    {
        return queryTrip("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations WHERE t.privacy = 0 AND t.published = 1");
    }

    public List getProtectedTrips()
    {
        return queryTrip("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations WHERE t.privacy = 1 AND t.published = 1 ");
    }

    public List getProtectedTripsWithoutDetails()
    {
        return queryTrip("FROM Trip t WHERE t.privacy = 1 AND t.published = 1");
    }

    @Override
    public List getNonPrivateTripsByKeyword(String keyword) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("FROM Trip t LEFT OUTER JOIN t.labels label WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy <> 2");
        query.setParameter("keyword","%"+keyword+"%");
        List<Trip> trips = query.list();
        tx.commit();
        session.close();
        return trips;
    }

    @Override
    public Trip getTrip(int id) throws TripsException {
        Session session = sessionFactory.openSession();
        Trip trip = (Trip) session.createCriteria(Trip.class).add(Restrictions.eq("id", id)).uniqueResult();
        session.close();
        if (trip == null) {
            throw new TripsException("Trip with id '"+id+"' doesn't exist");
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

    @Override
    public boolean isExistingTrip(int id) throws TripsException {
        getTrip(id);
        return true;
    }

    private List<Trip> queryTrip(String queryString)
    {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(queryString);
        List<Trip> trips = query.list();
        session.close();
        return trips;
    }


}
