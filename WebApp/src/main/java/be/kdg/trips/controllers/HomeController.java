package be.kdg.trips.controllers;

import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.trip.Trip;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private HttpSession session;

    @Autowired
    private TripsService tripsService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index() {
        User user = (User) session.getAttribute("user");
        List<Trip> trips = new ArrayList<>();
        try {
            trips.addAll(tripsService.findAllNonPrivateTrips(user));
            if(user!=null){
                trips.addAll(tripsService.findPrivateTrips(user));
            }
        } catch (TripsException e) {
            // can't happen
        }
        return new ModelAndView("indexView", "trips", trips);
    }

    @RequestMapping(value = "/trip/{tripId}/banner", method = RequestMethod.GET, produces = "image/jpg")
    public @ResponseBody byte[] showBanner(@PathVariable int tripId){
        User u = (User) session.getAttribute("user");
        Trip trip = null;
        try {
            trip = tripsService.findTripById(tripId, u);
        } catch (TripsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return trip.getImage();
    }
}
