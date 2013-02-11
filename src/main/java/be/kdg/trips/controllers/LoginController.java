package be.kdg.trips.controllers;

import be.kdg.trips.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.attribute.UserPrincipalLookupService;


/**
 * Subversion 2
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
public class LoginController {
    //  @Autowired
    //  private UserService userService;

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
        //User user = null;                                       //TODO fix get from session
        //user = userService.getUserByEmail(email);
        User user = new User("alfred", "kwak");
        session.setAttribute("user", user);
        return "profile";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        session.removeAttribute("user");
        session.invalidate();
        return "index";
    }

}
