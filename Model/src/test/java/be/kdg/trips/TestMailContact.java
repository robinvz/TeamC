package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.impl.TripsServiceImpl;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Subversion id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TestMailContact
{
    private static TripsService tripsService;

    @BeforeClass
    public static void createEnrollmentManager() throws TripsException, ParseException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        tripsService = (TripsServiceImpl)ctx.getBean("tripsService");
    }

    @Test
    public void successfulSendContactMail() throws MessagingException {
        tripsService.sendContactMail("Trip Location","I can't find the trip location", "tony_corsari@robin.com");
    }

}
