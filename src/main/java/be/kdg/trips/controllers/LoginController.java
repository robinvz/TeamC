package be.kdg.trips.controllers;

import be.kdg.trips.exceptions.UserException;
import be.kdg.trips.model.user.User;
import be.kdg.trips.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    //  @Autowired
    //  private UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    ApplicationContext ctx;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String handleLogin(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UserService service = (UserService) ctx.getBean("UserService");
        if (!service.checkLogin(email, password)) {  //wrong password, redirect to index
            return "index";
        }
        try {
            User user = service.findUser(email);
            session.setAttribute("user", user);
        } catch (UserException e) {
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
        UserService service = (UserService) ctx.getBean("UserService");
        try{
            service.addUser(request.getParameter("email"), request.getParameter("password"));
        } catch (UserException e) {
            //Register failed
        }
        return "login";
    }

}
