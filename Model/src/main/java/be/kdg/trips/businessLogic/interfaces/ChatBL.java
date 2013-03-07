package be.kdg.trips.businessLogic.interfaces;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.chat.ChatServer;
import be.kdg.trips.model.enrollment.Enrollment;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public interface ChatBL {
    public ChatServer initializeConversation(Enrollment enrollment1, Enrollment enrollment2) throws TripsException;
}
