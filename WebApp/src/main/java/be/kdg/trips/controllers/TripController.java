package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    @RequestMapping(value = "/trips", method = RequestMethod.GET)
    public ModelAndView showTrips() {
        List<Trip> allNonPrivateTrips = null;
        List<Trip> allPrivateTrips = null;
        List<Trip> allOrganisedTrips = null;
        List<Enrollment> allEnrollments = null;
        Map<String, List> parameters = new HashMap();
        User user = (User) session.getAttribute("user");
        try {
            if (session.getAttribute("user") != null) {
                allNonPrivateTrips = tripsService.findAllNonPrivateTrips(user);
                allPrivateTrips = tripsService.findPrivateTrips(user);
                allOrganisedTrips = tripsService.findTripsByOrganizer(user);
                allEnrollments = tripsService.findEnrollmentsByUser(user);
            } else {
                allNonPrivateTrips = tripsService.findAllNonPrivateTrips(null);
            }
        } catch (TripsException e) {
            //No (non)private trips
        }
        parameters.put("allNonPrivateTrips", allNonPrivateTrips);
        parameters.put("allPrivateTrips", allPrivateTrips);
        parameters.put("allOrganisedTrips", allOrganisedTrips);
        parameters.put("allEnrollments", allEnrollments);
        return new ModelAndView("tripsView", parameters);
    }

  /*  @RequestMapping(value = "/createdTrips", method = RequestMethod.GET)
    public ModelAndView showCreatedTrips(){
        List<Trip> allOrganisedTrips = null;
        Map<String, List> parameters = new HashMap<>();
        try{
            if(session.getAttribute("user") != null){
                allOrganisedTrips = tripsService.findTripsByOrganizer((User) session.getAttribute("user"));
            }
        }catch(TripsException e){
            //No created trips by this user.
        }
        parameters.put("allOrganisedTrips", allOrganisedTrips);
        return new ModelAndView("tripsView", parameters);
    }                        */

    @RequestMapping(value = "/trip/{tripId}", method = RequestMethod.GET)
    public ModelAndView getTrip(@PathVariable int tripId) {
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            return new ModelAndView("tripView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView");
        }
    }

    @RequestMapping(value = "/users/createTrip", method = RequestMethod.GET)
    public String createTrip() {
        return "/users/createTripView";
    }

    @RequestMapping(value = "/createTimeBoundTrip", method = RequestMethod.POST)
    public String createTimeBoundTrip(HttpServletRequest request) {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            TripPrivacy privacy = TripPrivacy.valueOf(request.getParameter("privacy"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            User user = (User) session.getAttribute("user");
            String uur = request.getParameter("startDate");
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));
            Trip test = tripsService.createTimeBoundTrip(title, description, privacy, user, startDate, endDate);
            String view = "trip/" + test.getId();
            return view;

        } catch (TripsException e) {
        } catch (ParseException e) {
        }
        return "/users/createTripView";
    }

    @RequestMapping(value = "/createTimeLessTrip", method = RequestMethod.POST)
    public String createTimeLessTrip(HttpServletRequest request) {
        try {
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            TripPrivacy privacy = TripPrivacy.valueOf(request.getParameter("privacy"));
            User user = (User) session.getAttribute("user");
            Trip test = tripsService.createTimelessTrip(title, description, privacy, user);
            String view = "redirect:trip/" + test.getId();
            return view;
        } catch (TripsException e) {
            return "/errors/loginErrorView";
        }
    }

    @RequestMapping(value = "/deleteTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView deleteTrip(@PathVariable int tripId) throws TripsException {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
            tripsService.deleteTrip(trip, user);
        } catch (TripsException e) {
            //failed to delete trip
            return new ModelAndView("tripView", "trip", trip);
        } catch (MessagingException e) {
        }
        return new ModelAndView("tripsView");
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public ModelAndView subscribe(@RequestParam int id) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.subscribe(tripsService.findTripById(id, user), user);
            } catch (TripsException e) {
                Map map = new HashMap();
                try {
                    map.put("trip", tripsService.findTripById(id, user));
                    map.put("error", "Could not subscribe to the trip.");
                } catch (TripsException e1) {
                }
                return new ModelAndView("tripView", map);
            }
            return getTrip(id);
        }
        return new ModelAndView("loginView", "loginBean", new LoginBean());
    }

    @RequestMapping(value = "/trip/{tripId}/createLocation", method = RequestMethod.GET)
    public ModelAndView createLocationView(@PathVariable int tripId) {
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            return new ModelAndView("createLocationView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView");
        }
    }

    @RequestMapping(value = "/trip/{tripId}/createLocation", method = RequestMethod.POST)
    public String createLocation(HttpServletRequest request, @PathVariable int tripId) {
        User user = (User) session.getAttribute("user");
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            tripsService.addLocationToTrip(user, trip, Double.parseDouble(request.getParameter("latitude")), Double.parseDouble(request.getParameter("longitude")), request.getParameter("street"),
                    request.getParameter("houseNr"), request.getParameter("city"), request.getParameter("postalCode"), request.getParameter("province"),
                    request.getParameter("country"), request.getParameter("title"), request.getParameter("description"));
        } catch (TripsException e) {
            //failed to add location to trip
        }
        return "redirect:/trip/" + tripId;
    }
}
