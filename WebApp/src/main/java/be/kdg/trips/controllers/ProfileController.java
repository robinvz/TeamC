package be.kdg.trips.controllers;

import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
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
    private TripsService tripsService;

    @Autowired
    private HttpSession session;

    @RequestMapping(value="/users/profile", method=RequestMethod.GET)
    public String showProfile(){
        return "/users/profileView";
    }

    @RequestMapping(value = "/users/editCredentials", method = RequestMethod.GET)
    public String showEditCredentials() {
        return "/users/editCredentialsView";
    }

    @RequestMapping(value = "/users/editCredentials", method = RequestMethod.POST)
    public String editCredentials(HttpServletRequest request) {
        try {
            tripsService.changePassword((User) session.getAttribute("user"), request.getParameter("oldPassword"),
                    request.getParameter("newPassword"));
            session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
        } catch (TripsException e) {
            return "/users/profileView";
            //Failed to update password
        }
        return "indexView";
    }

    @RequestMapping(value = "/users/editProfile", method = RequestMethod.POST)
    public String editProfile(HttpServletRequest request) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String street = request.getParameter("street");
        String houseNr = request.getParameter("houseNr");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postalCode");
        String province = request.getParameter("province");
        String country = request.getParameter("country");
        try {
            tripsService.updateUser((User) session.getAttribute("user"), firstName, lastName, street, houseNr, city, postalCode, province, country, null);
            session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
        } catch (TripsException e) {
            return "/users/profileView";
            //failed to edit user
        }
        //TODO test for return to /users/profileView
        return "/users/profileView";
        //return "indexView";
    }


    @RequestMapping(value = "/users/deleteProfile", method = RequestMethod.GET)
    public String deleteProfile() {
        try {
            tripsService.deleteUser((User) session.getAttribute("user"));
            session.invalidate();
        } catch (TripsException e) {
            return "/users/profileView";
            //failed to delete user
        }
        return "indexView";
    }

}
