package be.kdg.trips.controllers;

import be.kdg.trips.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

/**
 * Subversion $Id$
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
@SessionAttributes
public class TripsController {
    @Autowired
    ApplicationContext ctx;

    @Autowired
    private HttpSession session;

    @RequestMapping(value="/trips", method= RequestMethod.GET)
    public void showTrips(){
        TripService service = (TripService) ctx.getBean("TripService");
        //session.setAttribute("tripsList", service.findAllTrips());
    }

}