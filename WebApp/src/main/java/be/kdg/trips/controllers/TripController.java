package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/trips", method = RequestMethod.GET)
    public ModelAndView showTrips() {
        List<Trip> allNonPrivateTrips = null;
        List<Trip> allPrivateTrips = null;
        List<Trip> allOrganizedTrips = null;
        List<Enrollment> allEnrollments = null;
        Map<String, List> parameters = new HashMap();
        User user = (User) session.getAttribute("user");
        try {
            if (session.getAttribute("user") != null) {
                allNonPrivateTrips = tripsService.findAllNonPrivateTrips(user);
                allPrivateTrips = tripsService.findPrivateTrips(user);
                allOrganizedTrips = tripsService.findTripsByOrganizer(user);
                allEnrollments = tripsService.findEnrollmentsByUser(user);
            } else {
                allNonPrivateTrips = tripsService.findAllNonPrivateTrips(null);
            }
        } catch (TripsException e) {
            //No (non)private trips
        }
        parameters.put("allNonPrivateTrips", allNonPrivateTrips);
        parameters.put("allPrivateTrips", allPrivateTrips);
        parameters.put("allOrganizedTrips", allOrganizedTrips);
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
    public String createTimeBoundTrip(@RequestParam String title, @RequestParam String description, @RequestParam TripPrivacy privacy,
                                      @RequestParam String startDate, @RequestParam String endDate) {
        try {
            User user = (User) session.getAttribute("user");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Trip test = tripsService.createTimeBoundTrip(title, description, privacy, user, sdf.parse(startDate), sdf.parse(endDate));
            return "trip/" + test.getId();
        } catch (TripsException e) {
            return "users/createTripView";
        } catch (ParseException e) {
            return "/users/createTripView";
        }
    }

    @RequestMapping(value = "/createTimeLessTrip", method = RequestMethod.POST)
    public String createTimeLessTrip(@RequestParam String title, @RequestParam String description, @RequestParam TripPrivacy privacy) {
        try {
            User user = (User) session.getAttribute("user");
            Trip test = tripsService.createTimelessTrip(title, description, privacy, user);
            return "redirect:trip/" + test.getId();
        } catch (TripsException e) {
            return "/users/createTripView";
        }
    }

    @RequestMapping(value = "/deleteTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView deleteTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.deleteTrip(tripsService.findTripById(tripId, user), user);
            } catch (TripsException e) {
                return new ModelAndView("tripView", "error", messageSource.getMessage("deleteFailed", null, locale));
            } catch (MessagingException e) {
                //return new ModelAndView("tripView" + tripId);   //deze error ook catchen geeft fout
            }
            return new ModelAndView("tripsView");
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public ModelAndView subscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.subscribe(tripsService.findTripById(tripId, user), user);
            } catch (TripsException e) {
                Map map = new HashMap();
                try {
                    map.put("trip", tripsService.findTripById(tripId, user));
                    if (e.getMessage().contains("published")) {
                        map.put("error", messageSource.getMessage("notSubscribed", null, locale));
                    } else {
                        map.put("error", messageSource.getMessage("enrollmentAlreadyExists", null, locale));
                    }
                } catch (TripsException e1) {

                }
                return new ModelAndView("tripView", map);
            }
            return getTrip(tripId);
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/publishTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView publish(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.publishTrip(tripsService.findTripById(tripId, user), user);
                return new ModelAndView("tripsView");
            } catch (TripsException e) {
                return new ModelAndView("tripView", "error", messageSource.getMessage("notPublished", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }
      /*
    @RequestMapping(value = "/createLocation", method = RequestMethod.GET)
    public String createLocation() {
        return "/createLocationView";
    }

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