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
        if (tripsService.checkLogin(username, password)) {
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Trip trip : tripsService.findAllNonPrivateTrips(user)) {
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
        if (tripsService.checkLogin(username, password)) {
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Enrollment enrollment : tripsService.findEnrollmentsByUser(user)) {
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
        if (tripsService.checkLogin(username, password)) {
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Trip trip : tripsService.findTripsByOrganizer(user)) {
                JSONObject obj = new JSONObject();
                obj.accumulate("title", trip.getTitle());
                obj.accumulate("id", trip.getId());
                jsonArray.add(obj);
            }
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
        if (tripsService.checkLogin(username, password)) {
            User user = tripsService.findUser(username);
            Trip trip = tripsService.findTripById(id, user);
            js.accumulate("title", trip.getTitle());
            js.accumulate("description", trip.getDescription());
            js.accumulate("enrollments", trip.getEnrollments().size());
            //      js.accumulate("organizer", trip.getOrganizer()) ;
            js.accumulate("privacy", trip.getPrivacy());
        }
        return js.toString();
    }

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
    public ModelAndView createTimeBoundTrip(@RequestParam String title, @RequestParam String description, @RequestParam TripPrivacy privacy,
                                      @RequestParam String startDate, @RequestParam String endDate, Locale locale) {
        try {
            User user = (User) session.getAttribute("user");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Trip test = tripsService.createTimeBoundTrip(title, description, privacy, user, sdf.parse(startDate), sdf.parse(endDate));
            return new ModelAndView("redirect:trip/"+test.getId());
        } catch (TripsException e) {
            if (e.getMessage().contains("future")) {
                return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("StartDateFuture", null, locale));
            } else {
                return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("StartDateBefore", null, locale));
            }
        } catch (ParseException e) {
            return new ModelAndView("/users/createTripView", "error", e.getMessage());
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
                return new ModelAndView("tripView", "error", messageSource.getMessage("DeleteError", null, locale));
            } catch (MessagingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                        map.put("error", messageSource.getMessage("NotSubscribedError", null, locale));
                    } else {
                        map.put("error", messageSource.getMessage("EnrollmentExistsError", null, locale));
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

    @RequestMapping(value = "/unSubscribe", method = RequestMethod.GET)
    public ModelAndView unSubscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
               tripsService.disenroll(tripsService.findTripById(tripId, user), user);
            } catch (TripsException e) {
                return new ModelAndView("tripView", "error", messageSource.getMessage("UnSubscribeError", null, locale));
            }
            try {
                return new ModelAndView("tripView", "trip", tripsService.findTripById(tripId,user));
            } catch (TripsException e) {
                return new ModelAndView("tripsView");
            }
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
                return new ModelAndView("tripView", "error", messageSource.getMessage("NotPublishedError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations", method = RequestMethod.GET)
    public ModelAndView getLocations(@PathVariable int tripId) {
        Map parameters = new HashMap();
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            parameters.put("trip", trip);
            parameters.put("locations", trip.getLocations());
            return new ModelAndView("locationsView", parameters);
        } catch (TripsException e) {
            return new ModelAndView("tripsView");
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/createLocation", method = RequestMethod.GET)
    public ModelAndView createLocationView(@PathVariable int tripId) {
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            return new ModelAndView("createLocationView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView");
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/createLocation", method = RequestMethod.POST)
    public String createLocation(@PathVariable int tripId, @RequestParam double latitude, @RequestParam double longitude, @RequestParam String street,
                                 @RequestParam String houseNr, @RequestParam String city, @RequestParam String postalCode,
                                 @RequestParam String province, @RequestParam String country, @RequestParam String title,
                                 @RequestParam String description, @RequestParam String question, @RequestParam String correctAnswer, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            if (question.isEmpty()) {
                tripsService.addLocationToTrip(user, trip, latitude, longitude, street, houseNr.split("-")[0], city, postalCode, province,
                        country, title, description);
            } else {
                List answers = new ArrayList(Arrays.asList(request.getParameter("possibleAnswers")));
                tripsService.addLocationToTrip(user, trip, latitude, longitude, street, houseNr.split("-")[0], city, postalCode, province,
                        country, title, description, question, answers, answers.indexOf(correctAnswer));
            }
        } catch (TripsException e) {
            //failed to add location to trip
        }
        return "redirect:/trip/" + tripId + "/locations";
    }

    @RequestMapping(value = "/trip/{tripId}/locations/{locationId}/deleteLocation", method = RequestMethod.GET)
    public String deleteLocation(@PathVariable int tripId, @PathVariable int locationId) {
        User user = (User) session.getAttribute("user");
        Trip trip;
        try {
            trip = tripsService.findTripById(tripId, user);
            try {

                tripsService.deleteLocation(trip, user, tripsService.findLocationById(locationId));

            } catch (TripsException e) {
                //failed to delete location
            }
            return "redirect:/trip/" + trip.getId() + "/locations";
        } catch (TripsException e) {
          //Failed to find trip
        }
        return   "/trips";
    }

    @RequestMapping(value = "/trip/switchLocation", method = RequestMethod.POST)
    public ModelAndView switchLocation(@RequestParam String id,@RequestParam int fromPosition,@RequestParam int toPosition,@RequestParam String direction){
        System.out.println();
        User user = (User) session.getAttribute("user");
        String[] ids = id.split("-");
        int tripId = Integer.parseInt(ids[0]);
        int locationId = Integer.parseInt(ids[1]);
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
        }
        catch (TripsException e) {
            //Trip not found
            System.out.println("error1");
        }
        if (user != null){
            try {
                tripsService.switchLocationSequence(trip, user, fromPosition, toPosition);
            } catch (TripsException e) {
                //Switch location failed
                System.out.println("error2");
            }
        }

        return new ModelAndView("locationsView", "trip", trip);
    }


    @RequestMapping(value = "/trip/{tripId}/participants", method = RequestMethod.GET)
    public ModelAndView participants(@PathVariable int tripId) {
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            List<Enrollment> enr = tripsService.findEnrollmentsByTrip(trip);
            return new ModelAndView("participantsView", "enrollments", enr);
        } catch (TripsException e) {
            return new ModelAndView("tripsView");
        }
    }


    }