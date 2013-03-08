package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.TripDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
@SuppressWarnings("ALL")
@Repository
public class TripDaoImpl implements TripDao{
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Trip> getPublicTrips() {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments LEFT JOIN FETCH t.requisites LEFT JOIN FETCH t.invitations WHERE t.privacy = 0 AND t.published = true");
        return query.getResultList();
    }

    @Override
    public List<Trip> getProtectedTrips() {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments LEFT JOIN FETCH t.requisites LEFT JOIN FETCH t.invitations  WHERE t.privacy = 1 AND t.published = true");
        return query.getResultList();
    }

    @Override
    public List<Trip> getProtectedTripsWithoutDetails() {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t WHERE t.privacy = 1 AND t.published = true");
        return query.getResultList();
    }

    @Override
    public List<Trip> getPublicTripsByKeyword(String keyword) {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT OUTER JOIN t.labels label LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments LEFT JOIN FETCH t.requisites LEFT JOIN FETCH t.invitations WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy = 0 AND t.published = true");
        query.setParameter("keyword", "%"+keyword.toLowerCase()+"%");
        return query.getResultList();
    }

    @Override
    public List<Trip> getProtectedTripsByKeyword(String keyword) {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT OUTER JOIN t.labels label LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments LEFT JOIN FETCH t.requisites LEFT JOIN FETCH t.invitations WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy = 1 AND t.published = true");
        query.setParameter("keyword", "%"+keyword.toLowerCase()+"%");
        return query.getResultList();
    }

    @Override
    public List<Trip> getProtectedTripsWithoutDetailsByKeyword(String keyword) {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT OUTER JOIN t.labels label WHERE (lower(t.title) LIKE :keyword OR lower(t.description) LIKE :keyword OR lower(label) LIKE :keyword) AND t.privacy = 1 AND t.published = true");
        query.setParameter("keyword", "%"+keyword.toLowerCase()+"%");
        return query.getResultList();
    }

    @Override
    public List<Trip> getTripsByOrganizer(User organizer) {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.enrollments LEFT JOIN FETCH t.requisites LEFT JOIN FETCH t.invitations WHERE t.organizer.email LIKE :email");
        query.setParameter("email", "%"+organizer.getEmail()+"%");
        return query.getResultList();
    }

    @Override
    public Trip getTrip(int id) throws TripsException {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.enrollments LEFT JOIN FETCH t.locations LEFT JOIN FETCH t.requisites LEFT JOIN FETCH t.invitations WHERE t.id = :id");
        query.setParameter("id", id);
        try
        {
            return (Trip)query.getSingleResult();
        }
        catch(NoResultException ex)
        {
            throw new TripsException("Trip with id '"+id+"' doesn't exist");
        }
    }

    @Override
    public Trip getTripByQuestion(Question question) throws TripsException {
        Query query = entityManager.createQuery("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.locations location WHERE location = (SELECT l FROM Location l WHERE l.question = :question)");
        query.setParameter("question", question);
        try
        {
            return (Trip)query.getSingleResult();
        }
        catch(NoResultException ex)
        {
            throw new TripsException("Trip with question '"+question.getQuestion()+"' doesn't exist");
        }
    }

    @Override
    public Location getLocationById(int id) throws TripsException {
        return entityManager.find(Location.class, id);
    }

    @Override
    public void saveTrip(Trip trip)
    {
        entityManager.persist(trip);
    }

    @Override
    public void updateTrip(Trip trip)
    {
        entityManager.merge(trip);
    }

    @Override
    public void saveOrUpdateLocation(Location location) {
        if (location.getId() == null) {
            entityManager.persist(location);
        } else {
            entityManager.merge(location);
        }
    }

    @Override
    public void deleteTrip(int id) {
        entityManager.remove(entityManager.find(Trip.class, id));
    }

    @Override
    public void deleteLocation(int id) {
        entityManager.remove(entityManager.find(Location.class, id));
    }

    @Override
    public void deleteQuestion(int id) {
        entityManager.remove(entityManager.find(Question.class, id));
    }

    @Override
    public boolean isExistingTrip(int id) throws TripsException {
        getTrip(id);
        return true;
    }

    @Override
    public boolean isExistingLocation(int id) throws TripsException
    {
        getLocationById(id);
        return true;
    }
}
