package be.kdg.trips.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
        //userService.saveUser(request.getParameter("password"););
        return "index";
    }

    @RequestMapping(value = "/deleteProfile", method = RequestMethod.GET)
    public String deleteProfile() {
        //userService.deleteUser(user);
        return "index";
    }

}
