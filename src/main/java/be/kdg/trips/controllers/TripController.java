package be.kdg.trips.controllers;

import be.kdg.trips.exception.TripsException;
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
            List tripsList = tripsService.findAllTimelessNonPrivateTrips();
            session.setAttribute("tripsList", tripsList);
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
        return "createTrip";
    }

    @RequestMapping(value = "/createTrip", method = RequestMethod.POST)
    public String createTrip(HttpServletRequest request) {
        try {
            String startDateString = request.getParameter("tripStartDate");
            String endDateString = request.getParameter("tripEndDate");
            Date startDate = new SimpleDateFormat("MMMM d, yyyy").parse(startDateString);
            Date endDate = new SimpleDateFormat("MMMM d, yyyy").parse(endDateString);
            tripsService.createTimeBoundTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                    TripPrivacy.valueOf(request.getParameter("radios")), (User) session.getAttribute("user"), startDate, endDate);
        } catch (TripsException e) {
            //failed to create timeBoundTrip
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "trips";
    }

}
