package be.kdg.trips.controllers;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.enrollment.Enrollment;
import be.kdg.trips.model.enrollment.Status;
import be.kdg.trips.model.location.Location;
import be.kdg.trips.model.question.Question;
import be.kdg.trips.model.trip.TimelessTrip;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Subversion ${Id}
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */

@Controller
public class MobileController {
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
            js.accumulate("organizer", trip.getOrganizer().getEmail());
            js.accumulate("privacy", trip.getPrivacy());
            boolean isEnrolled = false;
            boolean isStarted = false;
            for (Enrollment enr : tripsService.findEnrollmentsByUser(user)) {
                if (enr.getTrip().getId() == trip.getId()) {
                    isEnrolled = true;
                    if (enr.getStatus() == Status.BUSY) {
                        isStarted = true;
                    }
                }
            }
            js.accumulate("isenrolled", isEnrolled);
            js.accumulate("isstarted", isStarted);
            js.accumulate("isactive", trip.isActive());
            js.accumulate("istimeless", (trip instanceof TimelessTrip) ? true : false);
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
                Map<Question, Boolean> answeredQuestions = null;
                for (Enrollment e : tripsService.findEnrollmentsByUser(user)) {
                    if (e.getTrip().getId() == trip.getId()) {
                        answeredQuestions = e.getAnsweredQuestions();
                    }
                }
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
                        if (answeredQuestions != null && answeredQuestions.size() > 0) {
                            boolean isAnswered = false;
                            isAnswered = answeredQuestions.containsKey(q);
                            loco.accumulate("answered", isAnswered);
                            if (isAnswered) {
                                loco.accumulate("correct", answeredQuestions.get(q));
                            }
                        }
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


    @RequestMapping(value = "/service/contacts", method = RequestMethod.POST)
    public
    @ResponseBody
    String contactsService(@RequestParam int id, @RequestParam String username, @RequestParam String password) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Trip trip = tripsService.findTripById(id, user);
                JSONArray jsonArray = new JSONArray();
                for (Enrollment enr : tripsService.findEnrollmentsByTrip(trip)) {
                    if (enr.getStatus() == Status.BUSY && enr.getUser().getId() != user.getId()) {
                        JSONObject loco = new JSONObject();
                        String firstname = enr.getUser().getFirstName() == null ? enr.getUser().getEmail() : enr.getUser().getFirstName();
                        String lastname = enr.getUser().getFirstName() == null ? " " : enr.getUser().getLastName();
                        loco.accumulate("firstName", firstname);
                        loco.accumulate("lastName", lastname);
                        loco.accumulate("email", enr.getUser().getEmail());
                        loco.accumulate("latitude", enr.getUser().getLatitude());
                        loco.accumulate("longitude", enr.getUser().getLongitude());
                        jsonArray.add(loco);
                        // loco.accumulate("city", enr.getLastLocationVisited().getAddress().getCity());
                    }
                }
                js.accumulate("contacts", jsonArray);
            }
        } catch (TripsException t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }

    @RequestMapping(value = "/service/answerQuestion", method = RequestMethod.POST)
    public
    @ResponseBody
    String questionService(@RequestParam String username, @RequestParam String password, @RequestParam int answerIndex, @RequestParam int locationId) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Location location = tripsService.findLocationById(locationId);
                Question question = location.getQuestion();
                Trip trip = location.getTrip();
                tripsService.setLastLocationVisited(trip, user, location);
                js.accumulate("correct", tripsService.checkAnswerFromQuestion(question, answerIndex, user));
            }
        } catch (Exception t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }

    @RequestMapping(value = "/service/move", method = RequestMethod.POST)
    public
    @ResponseBody
    String moveService(@RequestParam String username, @RequestParam String password, @RequestParam int locationId) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                Location location = tripsService.findLocationById(locationId);
                Trip trip = location.getTrip();
                tripsService.setLastLocationVisited(trip, user, location);
            }
        } catch (Exception t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }

    @RequestMapping(value = "/service/updateLocation", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateLocation(@RequestParam String username, @RequestParam String password, @RequestParam double latitude, @RequestParam double longitude) {
        JSONObject js = new JSONObject();
        try {
            js.accumulate("valid", tripsService.checkLogin(username, password));
            if (tripsService.checkLogin(username, password)) {
                User user = tripsService.findUser(username);
                tripsService.setUsersCurrentPosition(user, latitude, longitude);
            }
        } catch (Exception t) {
            js.put("valid", false);
        } finally {
            return js.toString();
        }
    }
}
