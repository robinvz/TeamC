package be.kdg.trips.persistence.dao.impl;

import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.enrollment.NullEnrollment;
import be.kdg.trips.model.invitation.Invitation;
import be.kdg.trips.model.invitation.NullInvitation;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.persistence.dao.interfaces.EnrollmentDao;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

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
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveOrUpdateEnrollment(Enrollment enrollment)
    {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.saveOrUpdate(enrollment);
        tx.commit();
        session.close();
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(User user) {
        Session session = sessionFactory.openSession();
        List<Enrollment> enrollments = session.createCriteria(Enrollment.class).add(Restrictions.eq("user", user)).list();
        session.close();
        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByTrip(Trip trip) {
        Session session = sessionFactory.openSession();
        List<Enrollment> enrollments = session.createCriteria(Enrollment.class).add(Restrictions.eq("trip", trip)).list();
        session.close();
        return enrollments;
    }

    @Override
    public Enrollment getEnrollmentByUserAndTrip(User user, Trip trip) {
        Session session = sessionFactory.openSession();
        Enrollment enrollment = (Enrollment) session.createCriteria(Enrollment.class).add(Restrictions.conjunction()
                .add(Restrictions.eq("trip", trip))
                .add(Restrictions.eq("user", user))
        ).uniqueResult();
        session.close();
        if (enrollment == null) {
            enrollment = NullEnrollment.getInstance();
        }
        return enrollment;
    }

    @Override
    public Invitation getInvitationByUserAndTrip(User user, Trip trip) {
        Session session = sessionFactory.openSession();
        Invitation invitation = (Invitation) session.createCriteria(Invitation.class).add(Restrictions.conjunction()
                .add(Restrictions.eq("trip", trip))
                .add(Restrictions.eq("user", user))
        ).uniqueResult();
        session.close();
        if (invitation == null) {
            invitation = NullInvitation.getInstance();
        }
        return invitation;
    }

    @Override
    public void saveOrUpdateInvitation(Invitation invitation) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.saveOrUpdate(invitation);
        tx.commit();
        session.close();
    }
}
