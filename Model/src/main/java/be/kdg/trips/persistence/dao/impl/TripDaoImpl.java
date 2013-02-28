package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.trip.*;
import be.kdg.trips.model.user.User;
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


    public List<Trip> getPublicTrips()
    {
        return queryTrip("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations WHERE t.privacy = 0 AND t.published = 1");
    }

    public List<Trip> getProtectedTrips()
    {
        return queryTrip("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments WHERE t.privacy = 1 AND t.published = 1 ");
    }

    public List<Trip> getProtectedTripsWithoutDetails()
    {
        return queryTrip("FROM Trip t WHERE t.privacy = 1 AND t.published = 1");
    }

    @Override
    public List<Trip> getPublicTripsByKeyword(String keyword)
    {
        return queryTripByKeyword("SELECT DISTINCT t FROM Trip t LEFT OUTER JOIN t.labels label LEFT JOIN FETCH t.locations WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy = 0 AND t.published = 1", keyword);
    }

    @Override
    public List<Trip> getProtectedTripsByKeyword(String keyword)
    {
        return queryTripByKeyword("SELECT DISTINCT t FROM Trip t LEFT OUTER JOIN t.labels label LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy = 1 AND t.published = 1", keyword);
    }

    @Override
    public List<Trip> getProtectedTripsWithoutDetailsByKeyword(String keyword)
    {
        return queryTripByKeyword("FROM Trip t LEFT OUTER JOIN t.labels label WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy = 1 AND t.published = 1", keyword);
    }

    @Override
    public List<Trip> getTripsByOrganizer(User organizer) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.labels LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments WHERE t.organizer.email =  :email");
        query.setParameter("email", organizer.getEmail());
        List<Trip> trips = query.list();
        session.close();
        return trips;
    }

    @Override
    public Trip getTrip(int id) throws TripsException {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments WHERE t.id = :id");
        query.setParameter("id", id);
        Trip trip =(Trip)query.uniqueResult();
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
    public void deleteLocation(Location location) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.delete(location);
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

    private List<Trip> queryTripByKeyword(String queryString, String keyword)
    {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(queryString);
        query.setParameter("keyword", "%"+keyword+"%");
        List<Trip> trips = query.list();
        session.close();
        return trips;
    }
}
