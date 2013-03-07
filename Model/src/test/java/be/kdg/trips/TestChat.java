package be.kdg.trips;

import be.kdg.trips.businessLogic.interfaces.ChatBL;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.chat.ChatServer;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
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

    private static User organizer;
    private static User user;
    private static Trip trip;

    @BeforeClass
    public static void createChatManager() throws TripsException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");

        organizer = tripsService.createUser(new User("organizer.121@hotmail.com","xoxoxoxo"));
        user = tripsService.createUser(new User("chatuser.1.2@hotmail.com","oxoxoxoxo"));
        trip = tripsService.createTimelessTrip("ChatTrip","Chat", TripPrivacy.PROTECTED, organizer);

        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, user);
        tripsService.startTrip(trip, organizer);
        tripsService.startTrip(trip, user);
    }

    @Test
    public void successfulInitializeChat() throws TripsException
    {
        List<Enrollment> enrollments = tripsService.findEnrollmentsByTrip(trip);
        assertEquals(ChatServer.class, (tripsService.initializeConversation(enrollments.get(FIRST_ELEMENT), enrollments.get(SECOND_ELEMENT))).getClass());
    }

    @Test(expected = TripsException.class)
    public void failedInitializeChatInvalidEnrollment() throws TripsException
    {
        List<Enrollment> enrollments = tripsService.findEnrollmentsByTrip(trip);
        tripsService.initializeConversation(enrollments.get(FIRST_ELEMENT), new Enrollment(trip, new User("Jos@gmail.com", "josjos")));
    }

    @Test(expected = TripsException.class)
    public void failedInitializeChatTripNotStarted() throws TripsException
    {
        User user = tripsService.createUser(new User("chatuser.1.2.3@hotmail.com","oxoxoxoxo"));
        Trip trip = tripsService.createTimelessTrip("ChatTrip","Chat", TripPrivacy.PROTECTED, organizer);

        tripsService.publishTrip(trip, organizer);
        tripsService.subscribe(trip, user);
        tripsService.startTrip(trip, organizer);

        List<Enrollment> enrollments = tripsService.findEnrollmentsByTrip(trip);
        tripsService.initializeConversation(enrollments.get(FIRST_ELEMENT), enrollments.get(SECOND_ELEMENT));
    }

    @Test(expected = TripsException.class)
    public void failedInitializeChatInvalidTrip() throws TripsException
    {
        List<Enrollment> enrollments = tripsService.findEnrollmentsByTrip(trip);
        tripsService.initializeConversation(enrollments.get(FIRST_ELEMENT), new Enrollment(new TimelessTrip("triptitle", "tripdescription", TripPrivacy.PROTECTED, organizer), user));
    }
    /*
    @Test
    public void successfulSendMessage() throws TripsException
    {
        List<Enrollment> enrollments = tripsService.findEnrollmentsByTrip(trip);
        ChatServer chatServer = tripsService.initializeConversation(enrollments.get(FIRST_ELEMENT), enrollments.get(SECOND_ELEMENT));
        chatServer.sendMessage("Testje");
    }
    */
}
