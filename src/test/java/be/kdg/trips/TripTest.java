package be.kdg.trips;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.services.interfaces.TripsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.junit.Assert.assertNotNull;


/**
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
public class TripTest {
    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    @Test
    public void showTimeLessTrips(){
        try {
            List timeLessTripsList = tripsService.findAllTimelessNonPrivateTrips();
            session.setAttribute("timeLessTripsList", timeLessTripsList);
        } catch (TripsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        assertNotNull(session.getAttribute("timeLessTripsList"));
    }

}
