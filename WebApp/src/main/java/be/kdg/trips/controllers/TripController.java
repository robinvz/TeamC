package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.trip.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
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
    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/service/alltrips", method = RequestMethod.GET)
    public
    @ResponseBody
    String allTripsService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        if (tripsService.checkLogin(username, password)){
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Trip trip : tripsService.findAllNonPrivateTrips(user)){
                JSONObject obj = new JSONObject();
                obj.accumulate("title", trip.getTitle());
                obj.accumulate("id", trip.getId());
                jsonArray.add(obj);
            }
            js.accumulate("trips", jsonArray);
        }
        return js.toString();
    }

    @RequestMapping(value = "/service/enrolledtrips", method = RequestMethod.GET)
    public
    @ResponseBody
    String enrolledTripsService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        if (tripsService.checkLogin(username, password)){
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Enrollment enrollment : tripsService.findEnrollmentsByUser(user)){
                JSONObject obj = new JSONObject();
                obj.accumulate("title", enrollment.getTrip().getTitle());
                obj.accumulate("id", enrollment.getTrip().getId());
                jsonArray.add(obj);
            }
            js.accumulate("trips", jsonArray);
        }
        return js.toString();
    }

    @RequestMapping(value = "/service/createdtrips", method = RequestMethod.GET)
    public
    @ResponseBody
    String createdTripsService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        if (tripsService.checkLogin(username, password)){
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Trip trip : tripsService.findTripsByOrganizer(user)){
                JSONObject obj = new JSONObject();
                obj.accumulate("title", trip.getTitle());
                obj.accumulate("id", trip.getId());
                jsonArray.add(obj);            }
            js.accumulate("trips", jsonArray);
        }
        return js.toString();
    }

    @RequestMapping(value = "/service/trip", method = RequestMethod.GET)
    public
    @ResponseBody
    String tripByIdService(@RequestParam int id, @RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        if (tripsService.checkLogin(username, password)){
            User user = tripsService.findUser(username);
            Trip trip = tripsService.findTripById(id, user);
            js.accumulate("title", trip.getTitle());
            js.accumulate("description", trip.getDescription()) ;
            js.accumulate("enrollments", trip.getEnrollments().size()) ;
      //      js.accumulate("organizer", trip.getOrganizer()) ;
            js.accumulate("privacy", trip.getPrivacy()) ;
        }
        return js.toString();
    }


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
    public ModelAndView subscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.subscribe(tripsService.findTripById(tripId, user), user);
            } catch (TripsException e) {
                Map map = new HashMap();
                try {
                    map.put("trip", tripsService.findTripById(tripId, user));
                    map.put("error", messageSource.getMessage("notSubscribed", null, locale));
                } catch (TripsException e1) {
                }
                return new ModelAndView("tripView", map);
            }
            return getTrip(tripId);
        }
        return new ModelAndView("loginView", "loginBean", new LoginBean());
    }

    @RequestMapping(value = "/publishTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView publish(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                Trip trip = tripsService.findTripById(tripId, user);
                tripsService.publishTrip(trip, user);
            } catch (TripsException e) {
                return new ModelAndView("tripView/"+tripId, "error", messageSource.getMessage("notPublished", null, locale));
            }
            return new ModelAndView("tripsView");
        }
        return new ModelAndView("loginView", "loginBean", new LoginBean());
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