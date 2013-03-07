package be.kdg.trips.businessLogic.impl;

import be.kdg.trips.businessLogic.interfaces.ChatBL;
import be.kdg.trips.businessLogic.interfaces.EnrollmentBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.chat.ChatServer;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.enrollment.Status;
import be.kdg.trips.model.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Component
public class ChatBLImpl implements ChatBL {

    @Autowired
    private EnrollmentBL enrollmentBL;

    @Override
    public ChatServer initializeConversation(Enrollment enrollment1, Enrollment enrollment2) throws TripsException {
        Trip trip;
        ChatServer chatServer=null;
        if(enrollment1.getTrip().equals(enrollment2.getTrip()))
        {
            trip = enrollment1.getTrip();
            if(enrollmentBL.isExistingEnrollment(enrollment1.getUser(), trip) && enrollmentBL.isExistingEnrollment(enrollment2.getUser(), trip))
            {
                if(enrollment1.getStatus() == Status.BUSY && enrollment2.getStatus() == Status.BUSY)
                {
                    chatServer = new ChatServer(enrollment1.getUser(), enrollment2.getUser());
                }
            }
        }
        return chatServer;
    }

}
