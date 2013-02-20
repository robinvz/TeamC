package be.kdg.trips.controllers;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
    public ModelAndView showTrips(){
        List<Trip> timelessTrips = null;
        List<Trip> timeboundTrips = null;
        Map<String, List> parameters = new HashMap<String, List>();
        try{
            timelessTrips = tripsService.findAllTimelessNonPrivateTrips();
        }catch (TripsException e) {
            //No timeless trips
        }
        try{
            timeboundTrips =  tripsService.findAllTimeBoundPublishedNonPrivateTrips();
        }catch (TripsException e) {
            //No timebound trips
        }
        parameters.put("timelessTrips", timelessTrips);
        parameters.put("timeboundTrips", timeboundTrips);
        return new ModelAndView("trips", parameters);
    }

    @RequestMapping(value = "/trip", method = RequestMethod.GET)
    public String trip(@RequestParam String tripId) {
        return "lol";
    }

    @RequestMapping(value = "/selectTrip", method = RequestMethod.GET)
    public String selectTrip() {
        return "trip";
    }

    @RequestMapping(value = "/users/createTrip", method = RequestMethod.GET)
    public String createTrip() {
        return "/users/createTrip";
    }

    @RequestMapping(value = "/createTimeBoundTrip", method = RequestMethod.POST)
    public String createTimeBoundTrip(HttpServletRequest request) {
        try {
            //Date startDate = new SimpleDateFormat("dd, MM, yyyy").parse(startDateString);
            //Date endDate = new SimpleDateFormat("dd, MM, yyyy").parse(endDateString);
            String stringDatum = request.getParameter("tripStartDate");

            Trip test = tripsService.createTimeBoundTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                    TripPrivacy.valueOf(request.getParameter("radios")), (User) session.getAttribute("user"),
                    (Date)request.getAttribute("tripStartDate"), (Date)request.getAttribute("tripEndDate"));
            System.out.println("string: "+stringDatum);
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
            tripsService.createTimelessTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                    TripPrivacy.valueOf(request.getParameter("radios")), (User) session.getAttribute("user"));
        } catch (TripsException e) {
            //failed to create timeLessTrip
        }
        return "trips";
    }

   /*
    @RequestMapping(value = "/addLocationToTrip", method = RequestMethod.POST)
    public String addLocationToTrip(HttpServletRequest request){
        try{
          //  Trip trip = tripsService.addLocationToTrip((User) session.getAttribute("user"), (Trip) session.getAttribute("trip"),
                    request.getParameter("latitude"), request.getParameter("longitude"), request.getParameter("street"), request.getParameter("houseNr"),
                    request.getParameter("city"), request.getParameter("postalCode"), request.getParameter("province"), request.getParameter("country")
                    , request.getParameter("tite"), request.getParameter("description"));
            //Trip trip = tripsService.addLocationToTrip(user, trip, latitude, longitude, street, houseNr, city, postalCode, province, country, title, description, question, answer );
        }catch (TripsException e){
             //failed to add location to trip
        }
        return null;
    }       */
}
