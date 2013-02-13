package be.kdg.trips.controllers;

import be.kdg.trips.exceptions.TripException;
import be.kdg.trips.model.TripPrivacy;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    ApplicationContext ctx;

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
        TripService service = (TripService) ctx.getBean("TripService");

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
            } catch (TripException e) {
                //failed to create timeBoundTrip
            }
        }

        return "trip/";
    }

}
