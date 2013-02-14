package be.kdg.trips.controllers;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Subversion 2
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
public class LoginController {
    @Autowired
    private TripsService tripsService;

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!tripsService.checkLogin(email, password)) {  //wrong password, redirect to index
            return "index";
        }
        try {
            User user = tripsService.findUser(email);
            session.setAttribute("user", user);
        } catch (TripsException e) {
            //Login failed
        }
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        session.invalidate();
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request) {
        try{
            tripsService.createUser(request.getParameter("email"), request.getParameter("password"));
        } catch (TripsException e) {
            //Register failed
        }
        return "login";
    }

}
