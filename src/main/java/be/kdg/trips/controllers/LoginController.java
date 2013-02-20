package be.kdg.trips.controllers;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;


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

    @RequestMapping(value = "/service/login", method = RequestMethod.GET)
    public  @ResponseBody String loginService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid",tripsService.checkLogin(username, password) ) ;
        return js.toString();
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

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        session.invalidate();
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request) {
        try {
            User user = tripsService.createUser(request.getParameter("email"), request.getParameter("password"));
            tripsService.updateUser(user, request.getParameter("firstName"), request.getParameter("lastName"), request.getParameter("street"),
                    request.getParameter("houseNr"), request.getParameter("city"), request.getParameter("postalCode"),
                    request.getParameter("province"), request.getParameter("country"));
        } catch (TripsException e) {
            //Register failed
        }
        return "login";
    }

}
