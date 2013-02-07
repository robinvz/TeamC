package be.kdg.trips.controllers;

import be.kdg.trips.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Subversion 2
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
@SessionAttributes
public class AccountController
{


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String addContact(@ModelAttribute("login")
                             User user, BindingResult result) {

        System.out.println("Email:" + user.getEmail() +
                "Password:" + user.getPassword());

        return "index";
    }

    @RequestMapping("/login")
    public ModelAndView showLogin() {

        return new ModelAndView("login", "command", new User());
    }
}
