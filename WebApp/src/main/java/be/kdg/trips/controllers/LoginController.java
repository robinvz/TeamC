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
        return "loginView";
    }

    @RequestMapping(value = "/service/login", method = RequestMethod.GET)
    public
    @ResponseBody
    String loginService(@RequestParam String username, @RequestParam String password) throws TripsException {
        JSONObject js = new JSONObject();
        js.accumulate("valid", tripsService.checkLogin(username, password));
        return js.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try {
            if (tripsService.checkLogin(email, password)) {  //wrong password, redirect to index
                User user = tripsService.findUser(email);
                session.setAttribute("user", user);
            } else {
                return "loginView";
            }
        } catch (TripsException e) {
            //will never throw
        }
        return "indexView";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        session.invalidate();
        return "indexView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        session.invalidate();
        return "registerView";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String street = request.getParameter("street");
        String houseNr = request.getParameter("houseNr");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postalCode");
        String province = request.getParameter("province");
        String country = request.getParameter("country");
        try {
            User user = tripsService.createUser(request.getParameter("email"), request.getParameter("password"));
            if(request.getParameter("firstName").isEmpty()){
                firstName=null;
            }
            if(request.getParameter("lastName").isEmpty()){
                lastName=null;
            }
            if(request.getParameter("street").isEmpty()){
                street=null;
            }
            if(request.getParameter("houseNr").isEmpty()){
                houseNr=null;
            }
            if(request.getParameter("city").isEmpty()){
                city=null;
            }
            if(request.getParameter("postalCode").isEmpty()){
                postalCode=null;
            }
            if(request.getParameter("province").isEmpty()){
                province=null;
            }
            if(request.getParameter("country").isEmpty()){
                country=null;
            }
            tripsService.updateUser(user, firstName, lastName, street,houseNr, city, postalCode,province, country);
            session.setAttribute("user", user);
            return "indexView";
        } catch (TripsException e) {
            //Register failed
            return "registerView";
        }
    }

}
