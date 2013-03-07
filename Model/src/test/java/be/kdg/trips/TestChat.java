package be.kdg.trips;

import be.kdg.trips.businessLogic.interfaces.ChatBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TestChat {
    private static TripsService tripsService;
    private final int FIRST_ELEMENT = 0;
    private final int SECOND_ELEMENT = 1;

    @BeforeClass
    public static void createChatManager() throws TripsException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
    }

    @Test
    public void successfulInitializeChat() throws TripsException {
        tripsService.createUser(new User("organizer.121@hotmail.com","xoxoxoxo"));
        User organizer = tripsService.findUser("organizer.121@hotmail.com");
        Trip trip = tripsService.createTimelessTrip("ChatTrip","Chat", TripPrivacy.PROTECTED, organizer);
        tripsService.publishTrip(trip, organizer);

        tripsService.createUser(new User("chatuser.1.2@hotmail.com","oxoxoxoxo"));
        User subscriber = tripsService.findUser("chatuser.1.2@hotmail.com");

        tripsService.subscribe(trip, subscriber);
        tripsService.startTrip(trip, organizer);
        tripsService.startTrip(trip, subscriber);
        List<Enrollment> enrollments = tripsService.findEnrollmentsByTrip(trip);
        assertNotNull(tripsService.initializeConversation(enrollments.get(FIRST_ELEMENT), enrollments.get(SECOND_ELEMENT)));
    }
}
