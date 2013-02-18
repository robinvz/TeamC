package be.kdg.trips.controllers;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 7/02/13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TripController {
    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    @RequestMapping(value="/trips", method= RequestMethod.GET)
    public void showTrips(){
        try {
            List timelessTrips = tripsService.findAllTimelessNonPrivateTrips();
            session.setAttribute("timelessTrips", timelessTrips);
            List timeboundTrips = tripsService.findAllTimeBoundPublishedNonPrivateTrips();
            session.setAttribute("timeboundTrips", timeboundTrips);
        } catch (TripsException e) {
            //failed to retrieve tripsList
        }
    }

    @RequestMapping(value = "/trip", method = RequestMethod.GET)
    public String trip() {
        return "trip";
    }

    @RequestMapping(value = "/selectTrip", method = RequestMethod.GET)
    public String selectTrip() {
        return "trip";
    }

    @RequestMapping(value = "/createTrip", method = RequestMethod.GET)
    public String createTrip() {
        return "users/createTrip";
    }

    @RequestMapping(value = "/createTimeBoundTrip", method = RequestMethod.POST)
    public String createTimeBoundTrip(HttpServletRequest request) {
        try {

            //Date startDate = new SimpleDateFormat("dd, MM, yyyy").parse(startDateString);
            //Date endDate = new SimpleDateFormat("dd, MM, yyyy").parse(endDateString);
            Trip test = tripsService.createTimeBoundTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                    TripPrivacy.valueOf(request.getParameter("radios")), (User) session.getAttribute("user"),
                    (Date)request.getAttribute("tripStartDate"), (Date)request.getAttribute("tripEndDate"));
            System.out.println(test);
        } catch (TripsException e) {
            //failed to create timeBoundTrip
        /*} catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        */
        }
        return "trips";
    }

    @RequestMapping(value = "/createTimeLessTrip", method = RequestMethod.POST)
    public String createTimeLessTrip(HttpServletRequest request) {
        try {
            Trip trip = tripsService.createTimelessTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                    TripPrivacy.valueOf(request.getParameter("radios")), (User) session.getAttribute("user"));
            System.out.println("privacy: "+trip.getPrivacy() +" // active: " +trip.isActive() + " // organizer: "+ trip.getOrganizer().getFirstName());
        } catch (TripsException e) {
            //failed to create timeLessTrip
        }
        return "trips";
    }

}
