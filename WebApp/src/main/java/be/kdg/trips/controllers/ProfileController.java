package be.kdg.trips.controllers;

import be.kdg.trips.beans.LoginBean;
import be.kdg.trips.businessLogic.exception.TripsException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.interfaces.TripsService;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.TransactionRequiredException;
import javax.validation.ConstraintViolationException;
import java.io.*;
import java.util.Locale;

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

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public String showProfile() {
        return "/users/profileView";
    }

    @RequestMapping(value = "/users/editCredentials", method = RequestMethod.GET)
    public String showEditCredentials() {
        return "/users/editCredentialsView";
    }

    @RequestMapping(value = "/users/editCredentials", method = RequestMethod.POST)
    public ModelAndView editCredentials(HttpServletRequest request, Locale locale) {
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            String newpw1 = request.getParameter("newPassword1");
            String newpw2 = request.getParameter("newPassword2");
            if(newpw1.equals(newpw2)) {
                try {
                    tripsService.changePassword(user, request.getParameter("oldPassword"), newpw1);
                    session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
                    return new ModelAndView("/users/profileView", "success", messageSource.getMessage("PasswordChanged", null, locale));
                } catch (TripsException e) {
                    return new ModelAndView("/users/editCredentialsView", "error", messageSource.getMessage("EditCredentialsOldPwError", null, locale));
                }
            } else {
                return new ModelAndView("/users/editCredentialsView", "error", messageSource.getMessage("EditCredentialsNewPwError", null, locale));
            }
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
                                    @RequestParam String country) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.updateUser(user, firstName, lastName, street, houseNr, city, postalCode, country, null);
                session.setAttribute("user", tripsService.findUser(((User) session.getAttribute("user")).getEmail()));
                return new ModelAndView("/users/profileView");
            } catch (TripsException e) {
                return new ModelAndView("/users/profileView", "error", e.getMessage());
            } catch (RuntimeException e) {
                return new ModelAndView("/users/profileView", "error", "Please follow these guidelines:\n" +
                        "Street, City and country should only contain letters\n" +
                        "House number should be a number with a maximum of one letter");
            }
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/editProfilePic", method = RequestMethod.POST)
    public String editProfilePic(@RequestParam("file") MultipartFile file)
    {
        try {
            byte[] bFile = file.getBytes();
            tripsService.updateUser((User) session.getAttribute("user"), "", "", "", "", "", "", "", bFile);
        } catch (IOException | TripsException e) {
            //TODO: tripsexception kan zijn: user bestaat niet of bfile is foute type (niet jpeg, gif of png)
        }
        return "/users/profileView";
    }

    @RequestMapping(value = "/users/deleteProfile", method = RequestMethod.GET)
    public ModelAndView deleteProfile(Locale locale) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                tripsService.deleteUser(user);
                session.invalidate();
            } catch (TripsException e) {
                return new ModelAndView("/users/profileView", "error", messageSource.getMessage("DeleteUserError", null, locale));
            } catch (RuntimeException e) {
                return new ModelAndView("/users/profileView", "error", messageSource.getMessage("DeleteUserWithTripError", null, locale));
            }
            return new ModelAndView("indexView");
        } else {
            return new ModelAndView("loginView", "loginBean", new LoginBean());
        }
    }

    @RequestMapping(value = "/users/viewTripsHistory", method = RequestMethod.GET)
    public String editProfile()
    {
        return "/users/tripsHistoryView";
    }


}
