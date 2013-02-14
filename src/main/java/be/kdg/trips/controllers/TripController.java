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
import java.util.Date;

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
    private TripsService service;

    @Autowired
    private HttpSession session;

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

        if (request.getParameter("tripDate1") == null && request.getParameter("tripDate2") == null) {
            service.createTimelessTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                    TripPrivacy.PUBLIC, (User) session.getAttribute("user"));
                    //TODO: get TripPrivacy
        } else {
            try {
                service.createTimeBoundTrip(request.getParameter("tripTitle"), request.getParameter("tripDescription"),
                        TripPrivacy.PUBLIC, (User) session.getAttribute("user"), new Date(), new Date());
                        //request.getParameter("tripDate1"), request.getParameter("tripDate2"));
                        //TODO: get Dates
            } catch (TripsException e) {
                //failed to create timeBoundTrip
            }
        }

        return "trip/";
    }

}
