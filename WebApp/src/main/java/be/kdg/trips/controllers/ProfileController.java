package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;

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

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public String showProfile() {
        return "/users/profileView";
    }

    @RequestMapping(value = "/users/editCredentials", method = RequestMethod.GET)
    public String showEditCredentials() {
        return "/users/editCredentialsView";
    }

    @RequestMapping(value = "/users/editCredentials", method = RequestMethod.POST)
    public ModelAndView editCredentials(HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            try {
                tripsService.changePassword(user, request.getParameter("oldPassword"),
                        request.getParameter("newPassword"));
                session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
            } catch (TripsException e) {
                //TODO:Error msg for edit credentials
                return new ModelAndView("/users/profileView");
            }
            return new ModelAndView("indexView");
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/editProfilePic", method = RequestMethod.GET)
    public String showEditProfilePic() {
        return "/users/editProfilePicView";
    }


    @RequestMapping(value = "/users/profilePic", method = RequestMethod.GET, produces = "image/jpg")
    public @ResponseBody byte[] showProfilePic(){
       User user = (User) session.getAttribute("user");
        byte[] imageData = user.getProfilePicture();
        return imageData;
    }

    @RequestMapping(value = "/users/editProfile", method = RequestMethod.POST)
    public ModelAndView editProfile(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String street,
                                    @RequestParam String houseNr, @RequestParam String city, @RequestParam String postalCode,
                                    @RequestParam String province, @RequestParam String country) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.updateUser(user, firstName, lastName, street, houseNr, city, postalCode, province, country, null);
                session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
            } catch (TripsException e) {
                return new ModelAndView("/users/profileView");
            }
            return new ModelAndView("/users/profileView");
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/editProfilePic", method = RequestMethod.POST)
    public String editProfilePic(HttpServletRequest request) {
        File file = new File(request.getParameter("picPath"));
        byte[] bFile = new byte[(int) file.length()];
        try {
            tripsService.updateUser((User) session.getAttribute("user"), "", "", "", "", "", "", "", "", bFile);
            session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
        } catch (TripsException e) {
            return "/users/profileView";
        }
        //TODO test for return to /users/profileView
        return "/users/profileView";
    }

    @RequestMapping(value = "/users/deleteProfile", method = RequestMethod.GET)
    public String deleteProfile() {
        try {
            tripsService.deleteUser((User) session.getAttribute("user"));
            session.invalidate();
        } catch (TripsException e) {
            return "/users/profileView";
        }
        return "indexView";
    }

}
