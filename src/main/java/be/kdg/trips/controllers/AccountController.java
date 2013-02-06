package be.kdg.trips.controllers;

import be.kdg.trips.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Subversion 2
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController
{
    @RequestMapping(method = RequestMethod.GET)
    public String getLoginPage()
    {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(ModelMap map, @ModelAttribute ("user") User user, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            map.addAttribute("message", "Register Failed");
            return "index";
        }
        map.addAttribute("message", "Registered " + user.getEmail() + " / " + user.getPassword());
        return "index";
    }
}
