package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.*;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
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

    @RequestMapping(value = "/service/alltrips", method = RequestMethod.POST)
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

    @RequestMapping(value = "/service/enrolledtrips", method = RequestMethod.POST)
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

    @RequestMapping(value = "/service/createdtrips", method = RequestMethod.POST)
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

    @RequestMapping(value = "/service/searchtrips", method = RequestMethod.POST)
    public
    @ResponseBody
    String searchTripsService(@RequestParam String username, @RequestParam String password, @RequestParam String keyword) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        if (tripsService.checkLogin(username, password)) {
            User user = tripsService.findUser(username);
            JSONArray jsonArray = new JSONArray();
            for (Trip trip : tripsService.findNonPrivateTripsByKeyword(keyword, user)) {
                JSONObject obj = new JSONObject();
                obj.accumulate("title", trip.getTitle());
                obj.accumulate("id", trip.getId());
                jsonArray.add(obj);
            }
            js.accumulate("trips", jsonArray);
        }
        return js.toString();
    }

    @RequestMapping(value = "/service/trip", method = RequestMethod.POST)
    public
    @ResponseBody
    String tripByIdService(@RequestParam int id, @RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        if (tripsService.checkLogin(username, password)) {
            User user = tripsService.findUser(username);
            Trip trip = tripsService.findTripById(id, user);
            js.accumulate("id", trip.getId());
            js.accumulate("title", trip.getTitle());
            js.accumulate("description", trip.getDescription());
            js.accumulate("enrollments", trip.getEnrollments().size());
            //      js.accumulate("organizer", trip.getOrganizer()) ;
            js.accumulate("privacy", trip.getPrivacy());
        }
        return js.toString();
    }

    @RequestMapping(value = "/service/enroll", method = RequestMethod.POST)
    public
    @ResponseBody
    String subscribeService(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Trip trip = tripsService.findTripById(id, user);
                tripsService.subscribe(trip, user);
            }
        } catch (TripsException t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }

    @RequestMapping(value = "/service/start", method = RequestMethod.POST)
    public
    @ResponseBody
    String startTripService(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Trip trip = tripsService.findTripById(id, user);
                tripsService.startTrip(trip, user);
            }
        } catch (TripsException t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }

    @RequestMapping(value = "/service/unsubscribe", method = RequestMethod.POST)
    public
    @ResponseBody
    String unsubscribeTripService(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Trip trip = tripsService.findTripById(id, user);
                tripsService.disenroll(trip, user);
            }
        } catch (TripsException t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }


    @RequestMapping(value = "/service/stop", method = RequestMethod.POST)
    public
    @ResponseBody
    String stopTripService(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Trip trip = tripsService.findTripById(id, user);
                tripsService.stopTrip(trip, user);
            }
        } catch (TripsException t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }

    @RequestMapping(value = "/service/locations", method = RequestMethod.POST)
    public
    @ResponseBody
    String locationsService(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Trip trip = tripsService.findTripById(id, user);
                JSONArray jsonArray = new JSONArray();
                for (Location loc : trip.getLocations()) {
                    JSONObject loco = new JSONObject();
                    loco.accumulate("id", loc.getId());
                    loco.accumulate("title", loc.getTitle());
                    loco.accumulate("latitude", loc.getLatitude());
                    loco.accumulate("longitude", loc.getLongitude());
                    loco.accumulate("description", loc.getDescription());
                    Question q = loc.getQuestion();
                    if (q != null) {
                        loco.accumulate("question", loc.getQuestion().getQuestion());
                        JSONArray answers = new JSONArray();
                        answers.addAll(loc.getQuestion().getPossibleAnswers());
                        loco.accumulate("possibleAnswers", answers);
                    } else {
                        loco.accumulate("question", null);
                    }
                    jsonArray.add(loco);
                }
                js.accumulate("locations", jsonArray);
            }
        } catch (TripsException t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
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
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
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
    public ModelAndView createTimeBoundTrip(@RequestParam String title, @RequestParam String
            description, @RequestParam TripPrivacy privacy,
                                            @RequestParam String startDate, @RequestParam String endDate, Locale locale, @RequestParam String repeat, @RequestParam String limit) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                startDate =  startDate.replace("T", " ");
                endDate = endDate.replace('T', ' ');
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Repeatable rp = null;
                Integer limitInt = null;
                if (!repeat.contentEquals("ONCE")){
                    rp = Repeatable.valueOf(repeat);
                    limitInt = Integer.parseInt(limit);
                }
                else{
                    return new ModelAndView("/users/createTripView");
                }
                Trip test = tripsService.createTimeBoundTrip(title, description, privacy, user, sdf.parse(startDate), sdf.parse(endDate), rp , limitInt);
                return new ModelAndView("redirect:trip/" + test.getId());
            } catch (TripsException e) {
                if (e.getMessage().contains("future")) {
                    return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("StartDateFuture", null, locale));
                } else {
                    return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("StartDateBefore", null, locale));
                }
            } catch (ParseException e) {
                return new ModelAndView("/users/createTripView", "error", e.getMessage());
            } catch (NumberFormatException n){
                return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("AmountNotANumber", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/createTimeLessTrip", method = RequestMethod.POST)
    public ModelAndView createTimeLessTrip(@RequestParam String title, @RequestParam String
            description, @RequestParam TripPrivacy privacy) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                Trip test = tripsService.createTimelessTrip(title, description, privacy, user);
                return new ModelAndView("redirect:trip/" + test.getId());
            } catch (TripsException e) {
                return new ModelAndView("/users/createTripView");
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/deleteTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView deleteTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.deleteTrip(trip, user);
                map.put("success", messageSource.getMessage("TripDeleted", null, locale));
                return new ModelAndView("tripsView");
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("NotOrganizerError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            } catch (MessagingException e) {
                map.put("trip", trip);
                map.put("error", e.getMessage());
                return new ModelAndView("tripView", map);
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public ModelAndView subscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Map map = new HashMap();
            Trip trip = null;
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.subscribe(trip, user);
                map.put("trip", trip);
                map.put("success", "You have been subscribed");
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("published")) {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("NotSubscribedError", null, locale));
                    return new ModelAndView("tripView", map);
                } else {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("EnrollmentExistsError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/unSubscribe", method = RequestMethod.GET)
    public ModelAndView unSubscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.disenroll(trip, user);
                map.put("trip", trip);
                map.put("success", "You have been unsubscribed");
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));//map.put("error", );
                } else {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("UnSubscribeError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/publishTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView publish(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.publishTrip(trip, user);
                map.put("trip", trip);
                map.put("success", messageSource.getMessage("IsPublished", null, locale));
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("is not the organizer")) {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("NotOrganizerError", null, locale));
                    return new ModelAndView("tripView", map);
                } else {   //e.getMessage().contains("published")
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("AlreadyPublishedError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/labels/{tripId}", method = RequestMethod.GET)
    public ModelAndView addLabel(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            try {
                trip = tripsService.findTripById(tripId, user);
                return new ModelAndView("labelsView", "trip", trip);
            } catch (TripsException e) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/labels/{tripId}", method = RequestMethod.POST)
    public ModelAndView addLabel(@PathVariable int tripId, @RequestParam String label, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.addLabelToTrip(trip, user, label);
                map.put("trip", trip);
                map.put("success", messageSource.getMessage("LabelAdded", null, locale));
                return new ModelAndView("labelsView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else {    //e.getMessage().contains("is not the organizer")
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("NotOrganizerError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/requirements/{tripId}", method = RequestMethod.GET)
    public ModelAndView requirements(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            try {
                trip = tripsService.findTripById(tripId, user);
            } catch (TripsException e) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            }
            return new ModelAndView("requirementsView", "trip", trip);
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/requirements/{tripId}", method = RequestMethod.POST)
    public ModelAndView requirements(@PathVariable int tripId, @RequestParam String requisite, @RequestParam String amount, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.addRequisiteToTrip(requisite, Integer.parseInt(amount), trip, user);
                map.put("trip", trip);
                map.put("success", messageSource.getMessage("RequisiteAdded", null, locale));
                return new ModelAndView("requirementsView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("is not the organizer")) {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("NotOrganizerError", null, locale));
                    return new ModelAndView("requirementsView", map);
                } else {    //e.getMessage().contains("already active")
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("AlreadyActiveError", null, locale));
                    return new ModelAndView("requirementsView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/inviteUser/{tripId}", method = RequestMethod.GET)
    public ModelAndView inviteUser(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            try {
                trip = tripsService.findTripById(tripId, user);
                return new ModelAndView("/users/inviteUserView", "trip", trip);
            } catch (TripsException e) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/startTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView startTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.startTrip(trip, user);
                map.put("trip", trip);
                map.put("success", messageSource.getMessage("TripStarted", null, locale));
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("is already started")) {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("EnrollmentAlreadyStartedError", null, locale));
                    return new ModelAndView("tripView", map);
                } else {  //TODO:catch error for tb trip (trip is not active yet->cannot start)
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("EnrollmentUnexistingError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/stopTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView endTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.stopTrip(trip, user);
                map.put("trip", trip);
                map.put("success", "You have stopped the trip");
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("you haven't begun yet")) {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("StopTripError", null, locale));
                    return new ModelAndView("tripView", map);
                } else {
                    map.put("trip", trip);
                    map.put("error", messageSource.getMessage("EnrollmentUnexistingError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations", method = RequestMethod.GET)
    public ModelAndView getLocations(@PathVariable int tripId) {
        Map map = new HashMap();
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            map.put("trip", trip);
            map.put("locations", trip.getLocations());
            return new ModelAndView("locationsView", map);
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
    public ModelAndView createLocation(@PathVariable int tripId, @RequestParam double latitude,
                                       @RequestParam double longitude, @RequestParam String street,
                                       @RequestParam String houseNr, @RequestParam String city, @RequestParam String postalCode,
                                       @RequestParam String country, @RequestParam String title, @RequestParam("file") MultipartFile file,
                                       @RequestParam String description, @RequestParam String question,
                                       @RequestParam String correctAnswer, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                if (question.isEmpty()) {
                    tripsService.addLocationToTrip(user, trip, latitude, longitude, street, houseNr.split("-")[0], city, postalCode,
                            country, title, description);
                } else {
                    List answers = new ArrayList(Arrays.asList(request.getParameter("possibleAnswers")));
                    byte[] bFile = file.getBytes();
                    tripsService.addLocationToTrip(user, trip, latitude, longitude, street, houseNr.split("-")[0], city, postalCode,
                            country, title, description, question, answers, answers.indexOf(correctAnswer), bFile);
                }
            } catch (TripsException e) {
                //failed to add location to trip
            } catch (IOException e) {
                //TODO: bfile is foute type (niet jpeg, gif of png)
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations");
    }

    @RequestMapping(value = "/trip/{tripId}/locations/{locationId}/deleteLocation", method = RequestMethod.GET)
    public ModelAndView deleteLocation(@PathVariable int tripId, @PathVariable int locationId) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                try {
                    tripsService.deleteLocation(trip, user, tripsService.findLocationById(locationId));
                } catch (TripsException e) {
                    //failed to delete location
                }
            } catch (TripsException e) {
                //Failed to find trip
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations");
    }

    @RequestMapping(value = "/trip/switchLocation", method = RequestMethod.POST)
    public ModelAndView switchLocation(@RequestParam String id, @RequestParam int fromPosition,
                                       @RequestParam int toPosition, @RequestParam String direction) {
        System.out.println();
        User user = (User) session.getAttribute("user");
        String[] ids = id.split("-");
        int tripId = Integer.parseInt(ids[0]);
        int locationId = Integer.parseInt(ids[1]);
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
        } catch (TripsException e) {
            //Trip not found
            System.out.println("error1");
        }
        if (user != null) {
            try {
                tripsService.switchLocationSequence(trip, user, fromPosition - 1, toPosition - 1);
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
            Map map = new HashMap();
            map.put("trip", trip);
            map.put("enrollments", enr);
            return new ModelAndView("users/participantsView", map);
        } catch (TripsException e) {
            return new ModelAndView("tripsView");
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/getLocationsLatLng", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationsLatLng(@PathVariable int tripId) {
        JSONArray jsonArray = new JSONArray();
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            for (Location location : trip.getLocations()) {
                JSONObject coordinates = new JSONObject();
                coordinates.put("latitude", location.getLatitude());
                coordinates.put("longitude", location.getLongitude());
                jsonArray.add(coordinates);
            }
        } catch (TripsException e) {
            //Trip not found
        }
        return jsonArray.toString();
    }

    public boolean isLoggedIn() {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/editLocation", method = RequestMethod.POST)
    public ModelAndView editLocation(@PathVariable int tripId, @RequestParam String value, @RequestParam String id, @RequestParam String rowId,
                                     @RequestParam String columnPosition, @RequestParam String columnId, @RequestParam String columnName) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                int locationId = Integer.parseInt(id.split("-")[1]);
                try {
                    Location location = tripsService.findLocationById(locationId);
                    String newValue;
                    switch (Integer.parseInt(columnId)) {
                        case 1:
                            newValue = value.trim().substring(location.getTitle().length());
                            tripsService.editTripLocationDetails(user, trip, location, "", "", "", "", "", newValue, "");
                            break;
                        case 2:
                            newValue = value.trim().substring(location.getDescription().length());
                            tripsService.editTripLocationDetails(user, trip, location, "", "", "", "", "", "", newValue);
                            break;
                    }
                } catch (TripsException e) {
                    // location not found
                    return new ModelAndView("locationsView");
                }
            } catch (TripsException e) {
                // trip not found
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations");
    }

    @RequestMapping(value = "/inviteUser/{tripId}/findUsersByKeyword", method = RequestMethod.POST)
    public ModelAndView getUsersByKeyword(@PathVariable int tripId, @RequestParam String keyword) {
        User user = (User) session.getAttribute("user");
        Map parameters;
        if (isLoggedIn()) {
            parameters = new HashMap();
            try {
                parameters.put("usersByKeyword", tripsService.findUsersByKeyword(keyword, user));
            } catch (TripsException e) {
                // keyword not found in users
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("inviteUserView", parameters);
    }

    @RequestMapping(value = "/inviteUser/{tripId}/{userByKeywordEmail}/sendInvite", method = RequestMethod.GET)
    public ModelAndView inviteUser(@PathVariable int tripId, @PathVariable String userByKeywordEmail) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                try {
                    tripsService.invite(trip, user, tripsService.findUser(userByKeywordEmail));
                } catch (MessagingException e) {
                    // failed to invite user
                }
            } catch (TripsException e) {
                //Failed to find trip
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/inviteUser/{tripId}");
    }

    @RequestMapping(value = "/editTripPic/{tripId}", method = RequestMethod.GET)
    public ModelAndView showEditTripPic(@PathVariable int tripId) {
        User user = (User)  session.getAttribute("user");
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
            return new ModelAndView("editTripPicView", "trip", trip);
        } catch (TripsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new ModelAndView("tripsView");
    }

    @RequestMapping(value = "/tripPic/{tripId}", method = RequestMethod.GET, produces = "image/jpg")
    public @ResponseBody byte[] showProfilePic(@PathVariable int tripId){
        User user = (User) session.getAttribute("user");
        byte[] imageData = null;
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            imageData = trip.getImage();
        } catch (TripsException e) {
            //Trip not found
        }
        return imageData;
    }

    @RequestMapping(value = "/editTripPic/{tripId}", method = RequestMethod.POST)
    public ModelAndView editProfilePic(@PathVariable int tripId,@RequestParam("file") MultipartFile file)
    {
        try {
            byte[] bFile = file.getBytes();
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            tripsService.addImageToTrip(trip, user, bFile);
            return new ModelAndView("editTripPicView", "trip", trip);
        } catch (IOException | TripsException e) {
            //TODO: tripsexception kan zijn: user bestaat niet of bfile is foute type (niet jpeg, gif of png)
        }
        return new ModelAndView("tripsView");
    }
}
