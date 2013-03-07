package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.EnrollmentDao;
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
public class EnrollmentDaoImpl implements EnrollmentDao
{
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(User user) {
        Query query = entityManager.createQuery("SELECT DISTINCT e FROM Enrollment e WHERE e.user = :user");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<Enrollment> getEnrollmentsByTrip(Trip trip) {
        Query query = entityManager.createQuery("SELECT DISTINCT e FROM Enrollment e WHERE e.trip = :trip");
        query.setParameter("trip", trip);
        return query.getResultList();
    }

    @Override
    public Enrollment getEnrollmentByUserAndTrip(User user, Trip trip) throws TripsException {
        Query query = entityManager.createQuery("SELECT e FROM Enrollment e WHERE e.user = :user AND e.trip = :trip");
        query.setParameter("user", user);
        query.setParameter("trip", trip);
        try
        {
            return (Enrollment)query.getSingleResult();
        }
        catch(NoResultException ex)
        {
            throw new TripsException("Enrollment for user '"+user.getEmail()+"' in trip '"+trip.getTitle()+"' doesn't exist");
        }
    }

    @Override
    public List<Invitation> getInvitationsByUser(User user) {
        Query query = entityManager.createQuery("SELECT DISTINCT i FROM Invitation i WHERE i.user = :user");
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public Invitation getInvitationByUserAndTrip(User user, Trip trip) throws TripsException {
        Query query = entityManager.createQuery("SELECT i FROM Invitation i WHERE i.user = :user AND i.trip = :trip");
        query.setParameter("user", user);
        query.setParameter("trip", trip);
        try
        {
            return (Invitation)query.getSingleResult();
        }
        catch(NoResultException ex)
        {
            throw new TripsException("Invitation for user '"+user.getEmail()+"' in trip '"+trip.getTitle()+"' doesn't exist");
        }
    }

    @Override
    public void saveOrUpdateEnrollment(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            entityManager.persist(enrollment);
        } else {
            entityManager.merge(enrollment);
        }
    }

    @Override
    public void saveOrUpdateInvitation(Invitation invitation) {
        if (invitation.getId() == null) {
            entityManager.persist(invitation);
        } else {
            entityManager.merge(invitation);
        }
    }

    @Override
    public void deleteEnrollment(int id) {
        entityManager.remove(entityManager.find(Enrollment.class, id));
    }

    @Override
    public void deleteInvitation(int id) {
        entityManager.remove(entityManager.find(Invitation.class, id));
    }

    @Override
    public boolean isExistingEnrollment(User user, Trip trip) throws TripsException {
        getEnrollmentByUserAndTrip(user, trip);
        return true;
    }

    @Override
    public boolean isExistingInvitation(User user, Trip trip) throws TripsException {
        getInvitationByUserAndTrip(user, trip);
        return true;
    }

    @Override
    public boolean isUnexistingEnrollment(User user, Trip trip) throws TripsException {
        try
        {
            getEnrollmentByUserAndTrip(user, trip);
        }
        catch (TripsException ex)
        {
            return true;
        }
        throw new TripsException("Enrollment already exists");
    }

    @Override
    public boolean isUnexistingInvitation(User user, Trip trip) throws TripsException {
        try
        {
            getInvitationByUserAndTrip(user, trip);
        }
        catch (TripsException ex)
        {
            return true;
        }
        throw new TripsException("Invitation already exists");
    }
}
