package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.location.Location;
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
import org.springframework.web.util.HtmlUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
            return new ModelAndView("tripsView");
        }
        parameters.put("allNonPrivateTrips", allNonPrivateTrips);
        parameters.put("allPrivateTrips", allPrivateTrips);
        parameters.put("allOrganizedTrips", allOrganizedTrips);
        parameters.put("allEnrollments", allEnrollments);
        return new ModelAndView("tripsView", parameters);
    }

    @RequestMapping(value = "/trip/{tripId}", method = RequestMethod.GET)
    public ModelAndView getTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            Map map = new HashMap();
            map.put("trip", trip);
            Map<String, Integer> requisitesPerTrip = new HashMap<>();
            if (isLoggedIn()) {
                Set<Enrollment> enrollmentSet = user.getEnrollments();
                for (Enrollment enrollment : enrollmentSet) {
                    if (enrollment.getTrip().equals(trip)) { //check whether user is enrolled for This trip
                        requisitesPerTrip.putAll(enrollment.getRequisites());
                        map.put("requisites", requisitesPerTrip);
                    }
                }
            }
            return new ModelAndView("tripView", map);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/trip/{tripId}/editTrip", method = RequestMethod.POST)
    public ModelAndView editTrip(@PathVariable int tripId, @RequestParam String title, @RequestParam String description,
                                 @RequestParam boolean chatAllowed, @RequestParam boolean positionVisible, Locale locale) {
        Trip trip = null;
        try {
            User user = (User) session.getAttribute("user");
            trip = tripsService.findTripById(tripId, user);
            tripsService.editTripDetails(trip, title, description, chatAllowed, positionVisible, user);
            return new ModelAndView("tripView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", e.getMessage());
        } catch (RuntimeException r) {
            Map map = new HashMap();
            map = putInMap(map, trip, "error", messageSource.getMessage("TitleDescriptionError", null, locale));
            return new ModelAndView("tripView", map);
        }
    }

    @RequestMapping(value = "/createTrip", method = RequestMethod.GET)
    public String createTrip() {
        return "users/createTripView";
    }

    @RequestMapping(value = "/createTimeBoundTrip", method = RequestMethod.POST)
    public ModelAndView createTimeBoundTrip(@RequestParam String title, @RequestParam String description, @RequestParam TripPrivacy privacy,
                                            @RequestParam String startDate, @RequestParam String endDate, @RequestParam String repeat,
                                            @RequestParam String limit, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                startDate = startDate.replace("T", " ");
                endDate = endDate.replace("T", " ");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Repeatable rp = null;
                Integer limitInt = null;
                if (!repeat.contentEquals("ONCE")) {
                    rp = Repeatable.valueOf(repeat);
                    limitInt = Integer.parseInt(limit);
                }
                Trip test = tripsService.createTimeBoundTrip(HtmlUtils.htmlEscape(title), HtmlUtils.htmlEscape(description), privacy, user, sdf.parse(startDate), sdf.parse(endDate), rp, limitInt);
                return new ModelAndView("redirect:trip/" + test.getId());
            } catch (TripsException e) {
                if (e.getMessage().contains("future")) {
                    return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("StartDateFuture", null, locale));
                } else {
                    return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("StartDateBefore", null, locale));
                }
            } catch (ParseException e) {
                return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("ParseError", null, locale));
            } catch (RuntimeException r) {
                return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("NotANumberError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/createTimeLessTrip", method = RequestMethod.POST)
    public ModelAndView createTimeLessTrip(@RequestParam String title, @RequestParam String description, @RequestParam TripPrivacy privacy,
                                           Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                Trip test = tripsService.createTimelessTrip(HtmlUtils.htmlEscape(title), HtmlUtils.htmlEscape(description), privacy, user);
                return new ModelAndView("redirect:trip/" + test.getId());
            } catch (TripsException e) {
                return new ModelAndView("/users/createTripView");
            } catch (RuntimeException r) {
                return new ModelAndView("/users/createTripView", "error", messageSource.getMessage("NotANumberError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/deleteTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView deleteTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
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
                map = putInMap(map, trip, "error", messageSource.getMessage("NotOrganizerError", null, locale));
                return new ModelAndView("tripView", map);
            }
        } catch (MessagingException e) {
            map = putInMap(map, trip, "error", e.getMessage());
            return new ModelAndView("tripView", map);
        }
    }

    @RequestMapping(value = "/isEnrolled/{tripId}", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean isEnrolled(@PathVariable int tripId) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
        } catch (TripsException e) {
            //trip not found
        }
        boolean isEnrolled = tripsService.isUserEnrolled(user, trip);
        if (isEnrolled) return true;
        return false;
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public ModelAndView subscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Map map = new HashMap();
            Trip trip = null;
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.subscribe(trip, user);
                map = putInMap(map, trip, "success", messageSource.getMessage("Subscribed", null, locale));
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("published")) {
                    map = putInMap(map, trip, "error", messageSource.getMessage("NotSubscribedError", null, locale));
                    return new ModelAndView("tripView", map);
                } else {
                    map = putInMap(map, trip, "error", messageSource.getMessage("EnrollmentExistsError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/unSubscribe", method = RequestMethod.POST)
    public ModelAndView unSubscribe(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.disenroll(trip, user);
                map = putInMap(map, trip, "success", messageSource.getMessage("UnSubscribed", null, locale));
                return new ModelAndView("tripView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else {
                    map = putInMap(map, trip, "error", messageSource.getMessage("UnSubscribeError", null, locale));
                    return new ModelAndView("tripView", map);
                }
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/publishTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView publish(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Map map = new HashMap();
        try {
            trip = tripsService.findTripById(tripId, user);
            tripsService.publishTrip(trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("IsPublished", null, locale));
            return new ModelAndView("tripView", map);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else if (e.getMessage().contains("is not the organizer")) {
                map = putInMap(map, trip, "error", messageSource.getMessage("NotOrganizerError", null, locale));
                return new ModelAndView("tripView", map);
            } else {   //e.getMessage().contains("published")
                map = putInMap(map, trip, "error", messageSource.getMessage("AlreadyPublishedError", null, locale));
                return new ModelAndView("tripView", map);
            }
        }
    }

    @RequestMapping(value = "/users/labels/{tripId}", method = RequestMethod.GET)
    public ModelAndView addLabel(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            return new ModelAndView("/users/labelsView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/users/labels/{tripId}", method = RequestMethod.POST)
    public ModelAndView addLabel(@PathVariable int tripId, @RequestParam String label, Locale locale) {
        Trip trip = null;
        Map map = new HashMap();
        try {
            User user = (User) session.getAttribute("user");
            trip = tripsService.findTripById(tripId, user);
            if(label.length()==0){
                map.put("trip", trip);
                map.put("error", messageSource.getMessage("LabelEmptyError", null, locale));
                return new ModelAndView("/users/labelsView", map);
            } else {
                tripsService.addLabelToTrip(trip, user, HtmlUtils.htmlEscape(label));
                map = putInMap(map, trip, "success", messageSource.getMessage("LabelAdded", null, locale));
                return new ModelAndView("/users/labelsView", map);
            }
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else {    //e.getMessage().contains("is not the organizer")
                map = putInMap(map, trip, "error", messageSource.getMessage("NotOrganizerError", null, locale));
                return new ModelAndView("tripView", map);
            }
        }
    }

    @RequestMapping(value = "/requirements/{tripId}", method = RequestMethod.GET)
    public ModelAndView requirements(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                Trip trip = tripsService.findTripById(tripId, user);
                return new ModelAndView("requirementsView", "trip", trip);
            } catch (TripsException e) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/requirements/{tripId}", method = RequestMethod.POST)
    public ModelAndView requirements(@PathVariable int tripId, @RequestParam String requisite, @RequestParam String amount, Locale locale)
    {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Trip trip = null;
            Map map = new HashMap();
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.addRequisiteToTrip(HtmlUtils.htmlEscape(requisite), Integer.parseInt(amount), trip, user);
                map = putInMap(map, trip, "success", messageSource.getMessage("RequisiteAdded", null, locale));
                return new ModelAndView("requirementsView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("is not the organizer")) {
                    map = putInMap(map, trip, "error", messageSource.getMessage("NotOrganizerError", null, locale));
                    return new ModelAndView("requirementsView", map);
                } else {    //e.getMessage().contains("already active")
                    map = putInMap(map, trip, "error", messageSource.getMessage("AlreadyActiveError", null, locale));
                    return new ModelAndView("requirementsView", map);
                }
            } catch (NumberFormatException n) {
                map = putInMap(map, trip, "error", messageSource.getMessage("RequisiteWrongError", null, locale));
                return new ModelAndView("requirementsView", map);
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/addRequirementToEnrollmentView/{userString}/{tripId}", method = RequestMethod.GET)
    public ModelAndView addRequirementToEnrollmentView(@PathVariable String userString, @PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Map map = new HashMap();
            try {
                Trip trip = tripsService.findTripById(tripId, user);
                map.put("userString", userString);
                map.put("trip", trip);
                return new ModelAndView("requirementsView", map);
            } catch (TripsException e) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/addRequirementToEnrollment/{userString}/{tripId}", method = RequestMethod.POST)
    public ModelAndView addRequirementToEnrollment(@PathVariable String userString, @PathVariable int tripId, @RequestParam String requisite,
                                                   @RequestParam String amount, Locale locale) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Map map = new HashMap();
            Trip trip = null;
            try {
                trip = tripsService.findTripById(tripId, user);
                User reqUser = tripsService.findUser(userString);
                tripsService.addRequisiteToEnrollment(requisite, Integer.parseInt(amount), trip, reqUser, user);
                map = putInMap(map, trip, "success", messageSource.getMessage("RequisiteAdded", null, locale));
                map.put("userString", userString);
                return new ModelAndView("requirementsView", map);
            } catch (TripsException e) {
                if (e.getMessage().contains("Trip with id")) {
                    return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
                } else if (e.getMessage().contains("doesn't exist")) {
                    map = putInMap(map, trip, "error", messageSource.getMessage("UserIncorrect.user", null, locale));
                    return new ModelAndView("requirementsView", map);
                } else if (e.getMessage().contains("is not the organizer")) {
                    map = putInMap(map, trip, "error", messageSource.getMessage("NotOrganizerError", null, locale));
                    return new ModelAndView("requirementsView", map);
                } else {
                    map = putInMap(map, trip, "error", e.getMessage());
                    return new ModelAndView("requirementsView", map);
                }
            } catch (NumberFormatException n) {
                map = putInMap(map, trip, "error", messageSource.getMessage("RequisiteWrongError", null, locale));
                return new ModelAndView("requirementsView", map);
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/startTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView startTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Map map = new HashMap();
        try {
            trip = tripsService.findTripById(tripId, user);
            tripsService.startTrip(trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("TripStarted", null, locale));
            return new ModelAndView("tripView", map);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else if (e.getMessage().contains("is already started")) {
                map = putInMap(map, trip, "error", messageSource.getMessage("EnrollmentAlreadyStartedError", null, locale));
                return new ModelAndView("tripView", map);
            } else if (e.getMessage().contains("exist")) {
                map = putInMap(map, trip, "error", messageSource.getMessage("EnrollmentUnExistingError", null, locale));
                return new ModelAndView("tripView", map);
            } else {
                map = putInMap(map, trip, "error", messageSource.getMessage("TripNotActiveError", null, locale));
                return new ModelAndView("tripView", map);
            }
        }
    }

    @RequestMapping(value = "/users/stopTrip/{tripId}", method = RequestMethod.GET)
    public ModelAndView endTrip(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Map map = new HashMap();
        try {
            trip = tripsService.findTripById(tripId, user);
            tripsService.stopTrip(trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("TripStopped", null, locale));
            return new ModelAndView("tripView", map);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else if (e.getMessage().contains("you haven't begun yet")) {
                map = putInMap(map, trip, "error", messageSource.getMessage("StopTripError", null, locale));
                return new ModelAndView("tripView", map);
            } else {
                map = putInMap(map, trip, "error", messageSource.getMessage("EnrollmentUnExistingError", null, locale));
                return new ModelAndView("tripView", map);
            }
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
            return new ModelAndView("tripsView", "error", e.getMessage());
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
                                       @RequestParam String correctAnswer, @RequestParam List possibleAnswers) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        byte[] bFile = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                if (question.isEmpty()) {
                    tripsService.addLocationToTrip(user, trip, latitude, longitude, street, houseNr.split("-")[0], city, postalCode,
                            country, title, description);
                } else {
                    if (!file.getOriginalFilename().isEmpty()) {
                        bFile = file.getBytes();
                    }
                    possibleAnswers.remove(0);
                    tripsService.addLocationToTrip(user, trip, latitude, longitude, HtmlUtils.htmlEscape(street), HtmlUtils.htmlEscape(houseNr.split("-")[0]), HtmlUtils.htmlEscape(city), HtmlUtils.htmlEscape(postalCode),
                            HtmlUtils.htmlEscape(country), HtmlUtils.htmlEscape(title), HtmlUtils.htmlEscape(description), HtmlUtils.htmlEscape(question), possibleAnswers, possibleAnswers.indexOf(correctAnswer), bFile);
                }
            } catch (TripsException e) {
                return new ModelAndView("tripsView");
            } catch (IOException e) {
                //TODO: bfile is foute type (niet jpeg, gif of png)
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations");
    }

    @RequestMapping(value = "/users/trip/{tripId}/locations/{locationId}/editLocation", method = RequestMethod.POST)
    public ModelAndView editLocation(@PathVariable int tripId, @PathVariable int locationId, @RequestParam String title, @RequestParam String description) {
        try {
            Map parameters = new HashMap();
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            Location location = tripsService.findLocationById(locationId);
            tripsService.editTripLocationDetails(user, trip, location, "", "", "", "", "", HtmlUtils.htmlEscape(title), HtmlUtils.htmlEscape(description));
            parameters.put("trip", trip);
            parameters.put("location", location);
            return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations/" + location.getId(), parameters);
        } catch (TripsException e) { // trip/location not found or edit locationDetails failed
            return new ModelAndView("tripsView", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/users/trip/{tripId}/locations/{locationId}/deleteLocation", method = RequestMethod.GET)
    public ModelAndView deleteLocation(@PathVariable int tripId, @PathVariable int locationId) {
        try {
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            tripsService.deleteLocation(trip, user, tripsService.findLocationById(locationId));
            return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations");
        } catch (TripsException e) { //trip not found or failed to delete/find location
            return new ModelAndView("tripsView", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/trip/switchLocation", method = RequestMethod.POST)
    public ModelAndView switchLocation(@RequestParam String id, @RequestParam int fromPosition,
                                       @RequestParam int toPosition, @RequestParam String direction) {
        User user = (User) session.getAttribute("user");
        String[] ids = id.split("-");
        int tripId = Integer.parseInt(ids[0]);
        int locationId = Integer.parseInt(ids[1]);
        Trip trip = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.switchLocationSequence(trip, user, fromPosition - 1, toPosition - 1);
                return new ModelAndView("locationsView", "trip", trip);
            } catch (TripsException e) { //trip not found or Switch location failed
                return new ModelAndView("locationsView", "trip", trip);
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/trip/{tripId}/participants", method = RequestMethod.GET)
    public ModelAndView participants(@PathVariable int tripId, Locale locale) {
        if (isLoggedIn()) {
            try {
                Map map = new HashMap();
                Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
                List<Enrollment> enr = tripsService.findEnrollmentsByTrip(trip);
                map.put("trip", trip);
                map.put("enrollments", enr);
                return new ModelAndView("users/participantsView", map);
            } catch (TripsException e) {          //TODO:find enrollments by trip error
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/getLocationsLatLng", method = RequestMethod.GET)
    @ResponseBody
    public String getLocationsLatLng(@PathVariable int tripId, @RequestParam String amount, @RequestParam int locationId) {
        JSONArray jsonArray = new JSONArray();
        try {
            Trip trip = tripsService.findTripById(tripId, (User) session.getAttribute("user"));
            List<Location> locs = new ArrayList<>();
            if(amount.equals("all")){
                locs = trip.getLocations();
            }else if(amount.equals("one")){
                locs.add(tripsService.findLocationById(locationId));
            }
            for (Location location : locs) {
                JSONObject coordinates = new JSONObject();
                coordinates.put("latitude", location.getLatitude());
                coordinates.put("longitude", location.getLongitude());
                jsonArray.add(coordinates);
            }
        } catch (TripsException e) {
            return "redirect:/trips";
        }
        return jsonArray.toString();
    }

    @RequestMapping(value = "/users/inviteUser/{tripId}", method = RequestMethod.GET)
    public ModelAndView inviteUser(@PathVariable int tripId, Locale locale) {
        try {
            Map parameters = new HashMap();
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            parameters.put("trip", trip);
            parameters.put("invitations", trip.getInvitations());
            return new ModelAndView("/users/inviteUserView", parameters);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/inviteUser/{tripId}/uninvite", method = RequestMethod.POST)
    public ModelAndView unInviteUser(@PathVariable int tripId, @RequestParam String uninviteEmail) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                Trip trip = tripsService.findTripById(tripId, user);
                tripsService.uninvite(trip, user, tripsService.findUser(uninviteEmail));
                return new ModelAndView("redirect:/users/inviteUser/" + trip.getId());
            } catch (TripsException e) { // trip/user not found or failed to uninvite
                return new ModelAndView("tripsView", "error", e.getMessage());
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/inviteUser/{tripId}/findUsersByKeyword", method = RequestMethod.GET)
    public ModelAndView findUsersByKeyword(@PathVariable int tripId, @RequestParam String keyword) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            try {
                Map map = new HashMap();
                Trip trip = tripsService.findTripById(tripId, user);
                map.put("trip", trip);
                map.put("invitations", trip.getInvitations());
                map.put("usersByKeyword", tripsService.findUsersByKeyword(keyword, user));
                return new ModelAndView("/users/inviteUserView", map);
            } catch (TripsException e) { // trip not found or keyword not found in users
                return new ModelAndView("tripsView", "error", e.getMessage());
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/inviteUser/{tripId}/sendInvite", method = RequestMethod.POST)
    public ModelAndView sendInvite(@PathVariable int tripId, @RequestParam String userByKeywordEmail) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                tripsService.invite(trip, user, tripsService.findUser(userByKeywordEmail));
                return new ModelAndView("redirect:/users/inviteUser/" + trip.getId());
            } catch (MessagingException e) {
                return new ModelAndView("tripsView", "error", e.getMessage());
            } catch (TripsException e) {
                return new ModelAndView("tripsView", "error", e.getMessage());
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/editTripPic/{tripId}", method = RequestMethod.GET)
    public ModelAndView showEditTripPic(@PathVariable int tripId, Locale locale) {
        try {
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            return new ModelAndView("/users/editTripPicView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/tripPic/{tripId}", method = RequestMethod.GET, produces = "image/jpg")
    public
    @ResponseBody
    byte[] showTripPic(@PathVariable int tripId) {
        byte[] imageData = null;
        try {
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            imageData = trip.getImage();
        } catch (TripsException e) {
            return null;
        }
        return imageData;
    }

    @RequestMapping(value = "/users/editTripPic/{tripId}", method = RequestMethod.POST)
    public ModelAndView editTripPic(@PathVariable int tripId, @RequestParam("file") MultipartFile file, Locale locale) {
        Trip trip = null;
        try {
            byte[] bFile = file.getBytes();
            User user = (User) session.getAttribute("user");
            trip = tripsService.findTripById(tripId, user);
            tripsService.addImageToTrip(trip, user, bFile);
            return new ModelAndView("/users/editTripPicView", "trip", trip);
        } catch (IOException | TripsException e) {
            return new ModelAndView("tripsView", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/users/addDate/{tripId}", method = RequestMethod.GET)
    public ModelAndView addDate(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            return new ModelAndView("/users/addDateView", "trip", trip);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/users/addDate/{tripId}", method = RequestMethod.POST)
    public ModelAndView addDate(@PathVariable int tripId, @RequestParam String startDate, @RequestParam String endDate, Locale locale) {
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Map map = new HashMap();
        try {
            trip = tripsService.findTripById(tripId, user);
            startDate = startDate.replace("T", " ");
            endDate = endDate.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            tripsService.addDateToTimeBoundTrip(sdf.parse(startDate), sdf.parse(endDate), trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("DatesAdded", null, locale));
            return new ModelAndView("/users/addDateView", map);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else if (e.getMessage().contains("future")) {
                map = putInMap(map, trip, "error", messageSource.getMessage("StartDateFuture", null, locale));
                return new ModelAndView("/users/addDateView", map);
            } else {
                map = putInMap(map, trip, "error", messageSource.getMessage("StartDateBefore", null, locale));
                return new ModelAndView("/users/addDateView", map);
            }
        } catch (ParseException e) {
            map = putInMap(map, trip, "error", messageSource.getMessage("ParseError", null, locale));
            return new ModelAndView("/users/addDateView", map);
        }
    }

    @RequestMapping(value = "/editTripTheme/{tripId}", method = RequestMethod.POST)
    public ModelAndView editTripTheme(@PathVariable int tripId, @RequestParam String theme, Locale locale) {
        try {
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            tripsService.changeThemeOfTrip(trip, theme);
            return new ModelAndView("redirect:editTripPicView/" + tripId, "trip", trip);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else {
                return new ModelAndView("tripsView", "error", e.getMessage());
            }
        }
    }

    @RequestMapping(value = "/users/costs/{tripId}", method = RequestMethod.GET)
    public ModelAndView costs(@PathVariable int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        try {
            Trip trip = null;
            Map map = new HashMap();
            trip = tripsService.findTripById(tripId, user);
            Map<String, Map<String, Double>> totalTripCosts = new HashMap<>();
            for (Enrollment enrollment : trip.getEnrollments()) {
                totalTripCosts.put(enrollment.getUser().toString(), enrollment.getCosts());
            }
            map.put("trip", trip);
            map.put("totalTripCosts", totalTripCosts);
            return new ModelAndView("costsView", map);
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/users/costs/{tripId}/createCost", method = RequestMethod.POST)
    public ModelAndView createCost(@PathVariable int tripId, @RequestParam String name, @RequestParam double amount, Locale locale) {
        User user = (User) session.getAttribute(("user"));
        try {
            Trip trip = tripsService.findTripById(tripId, user);
            tripsService.addCostToEnrollment(name, amount, trip, user);
            return new ModelAndView("redirect:/users/costs/" + trip.getId());
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/users/costs/{tripId}/deleteCost/{name}/{amount}", method = RequestMethod.GET)
    public ModelAndView deleteCost(@PathVariable int tripId, @PathVariable String name, @PathVariable double amount, Locale locale) {
        User user = (User) session.getAttribute("user");
        try {
            Map map = new HashMap();
            Trip trip = tripsService.findTripById(tripId, user);
            tripsService.removeCostFromEnrollment(name, amount, trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("CostAdded", null, locale));
            return new ModelAndView("redirect:/users/costs/" + trip.getId(), map); //TODO:error
        } catch (TripsException e) {
            return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
        }
    }

    @RequestMapping(value = "/users/acceptInvitation", method = RequestMethod.GET)
    public ModelAndView acceptInvitation(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        Map map = new HashMap();
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
            tripsService.acceptInvitation(trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("InvitationAccepted", null, locale));
            return new ModelAndView("tripView", map);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else if (e.getMessage().contains("Invitation for user")) {
                map = putInMap(map, trip, "error", messageSource.getMessage("InvitationUnExisting", null, locale));
                return new ModelAndView("tripView", map);
            } else {
                map = putInMap(map, trip, "error", messageSource.getMessage("AlreadyActiveError", null, locale));
                return new ModelAndView("tripView", map);
            }
        }
    }

    @RequestMapping(value = "/users/declineInvitation", method = RequestMethod.GET)
    public ModelAndView declineInvitation(@RequestParam int tripId, Locale locale) {
        User user = (User) session.getAttribute("user");
        Map map = new HashMap();
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, user);
            tripsService.declineInvitation(trip, user);
            map = putInMap(map, trip, "success", messageSource.getMessage("InvitationDeclined", null, locale));
            return new ModelAndView("tripView", map);
        } catch (TripsException e) {
            if (e.getMessage().contains("Trip with id")) {
                return new ModelAndView("tripsView", "error", messageSource.getMessage("FindTripError", null, locale));
            } else {
                map = putInMap(map, trip, "error", e.getMessage());
                return new ModelAndView("tripView", map);
            }
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/{locationId}/addQuestion", method = RequestMethod.POST)
    public ModelAndView addQuestion(@PathVariable int tripId, @PathVariable int locationId, @RequestParam String question,
                                    @RequestParam("file") MultipartFile file, @RequestParam List<String> possibleAnswers, @RequestParam String correctAnswer) {
        Map parameters = new HashMap();
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Location location = null;
        byte[] bFile = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                location = tripsService.findLocationById(locationId);
                parameters.put("trip", trip);
                parameters.put("location", location);
                if (!file.isEmpty()) {
                    bFile = file.getBytes();
                }
                tripsService.addQuestionToLocation(user, location, question, possibleAnswers, possibleAnswers.indexOf(correctAnswer), bFile);
            } catch (TripsException e) {
                return new ModelAndView("tripsView");
            } catch (IOException e) {
                //TODO: bfile is foute type (niet jpeg, gif of png)
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations/" + location.getId(), parameters);
    }

    @RequestMapping(value = "/users/trip/{tripId}/locations/{locationId}/editQuestion", method = RequestMethod.POST)
    public ModelAndView editQuestion(@PathVariable int tripId, @PathVariable int locationId, @RequestParam String question,
                                     @RequestParam List<String> possibleAnswers, @RequestParam String correctAnswer) {
        try {
            Map parameters = new HashMap();
            User user = (User) session.getAttribute("user");
            Trip trip = tripsService.findTripById(tripId, user);
            Location location = tripsService.findLocationById(locationId);
            parameters.put("trip", trip);
            parameters.put("location", location);
            tripsService.editTripQuestionDetails(user, location, question, possibleAnswers, possibleAnswers.indexOf(correctAnswer), null);
            return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations/" + location.getId(), parameters);
        } catch (TripsException e) { //trip/loc not found or failed to edit q details
            return new ModelAndView("tripsView", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/users/trip/{tripId}/locations/{locationId}/deleteQuestion", method = RequestMethod.GET)
    public ModelAndView deleteQuestion(@PathVariable int tripId, @PathVariable int locationId) {
        Map parameters = new HashMap();
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Location location = null;
        try {
            trip = tripsService.findTripById(tripId, user);
            location = tripsService.findLocationById(locationId);
            parameters.put("trip", trip);
            parameters.put("location", location);
            tripsService.removeQuestionFromLocation(user, location);
            return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations/" + location.getId(), parameters);
        } catch (TripsException e) { // trip/loc not found or failed to remove q from loc
            return new ModelAndView("tripsView", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/users/trip/{tripId}/locations/{locationId}/deleteQuestionImage", method = RequestMethod.GET)
    public ModelAndView deleteQuestionImage(@PathVariable int tripId, @PathVariable int locationId) {
        Map parameters = new HashMap();
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Location location = null;
        try {
            trip = tripsService.findTripById(tripId, user);
            location = tripsService.findLocationById(locationId);
            parameters.put("trip", trip);
            parameters.put("location", location);
            tripsService.removeImageFromQuestion(user, location.getQuestion());
            return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations/" + location.getId(), parameters);
        } catch (TripsException e) { //trip/loc not found or failed to remove img from q
            return new ModelAndView("tripsView", "error", e.getMessage());
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/{locationId}/questionPic", method = RequestMethod.GET, produces = "image/jpg")
    public
    @ResponseBody
    byte[] showQuestionPic(@PathVariable int tripId, @PathVariable int locationId) {
        try {
            Location location = tripsService.findLocationById(locationId);
            return location.getQuestion().getImage();
        } catch (TripsException e) {
            // location not found
            return new byte[0];
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/{locationId}", method = RequestMethod.GET)
    public ModelAndView getLocation(@PathVariable int tripId, @PathVariable int locationId) {
        User user = (User) session.getAttribute("user");
        if (isLoggedIn()) {
            Map parameters = new HashMap();
            try {
                parameters.put("trip", tripsService.findTripById(tripId, user));
                parameters.put("location", tripsService.findLocationById(locationId));
                return new ModelAndView("/users/locationView", parameters);
            } catch (TripsException e) {
                return new ModelAndView("tripsView");
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/trip/{tripId}/locations/{locationId}/editLocationPic", method = RequestMethod.POST)
    public ModelAndView editQuestionPic(@PathVariable int tripId, @PathVariable int locationId, @RequestParam("file") MultipartFile file) {
        Map parameters = new HashMap();
        User user = (User) session.getAttribute("user");
        Trip trip = null;
        Location location = null;
        if (isLoggedIn()) {
            try {
                trip = tripsService.findTripById(tripId, user);
                location = tripsService.findLocationById(locationId);
                parameters.put("trip", trip);
                parameters.put("location", location);
                byte[] bFile = file.getBytes();
                tripsService.editTripQuestionDetails(user, location, "", new ArrayList<String>(), null, bFile);
            } catch (TripsException e) {
                return new ModelAndView("tripsView");
            } catch (IOException e) {
                //TODO: tripsexception kan zijn: user bestaat niet of bfile is foute type (niet jpeg, gif of png)
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
        return new ModelAndView("redirect:/trip/" + trip.getId() + "/locations/" + location.getId(), parameters);
    }

    public Map putInMap(Map map, Trip value1, String key2, String value2) {
        map.put("trip", value1);
        map.put(key2, value2);
        return map;
    }

    public boolean isLoggedIn() {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }
}