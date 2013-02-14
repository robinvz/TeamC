package be.kdg.trips.controllers;

import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 7/02/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
@Controller
@SessionAttributes
public class ProfileController {
    @Autowired
    private TripsService service;

    @Autowired
    private HttpSession session;

    @RequestMapping(value="/profile", method=RequestMethod.GET)
    public String showProfile(){
        return "profile";
    }

    @RequestMapping(value = "/editCredentials", method = RequestMethod.GET)
    public String showEditCredentials() {
        return "editCredentials";
    }

    @RequestMapping(value = "/editCredentials", method = RequestMethod.POST)
    public String editCredentials(HttpServletRequest request) {
        //service.updateUser(request.getParameter("newPassword"););
        return "index";
    }

    @RequestMapping(value = "/deleteProfile", method = RequestMethod.GET)
    public String deleteProfile() {
        //service.deleteUser(session.getAttribute("user");
        return "index";
    }

}
