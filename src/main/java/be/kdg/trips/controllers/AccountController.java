package be.kdg.trips.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String getLoginPage(ModelMap map)
    {
        map.addAttribute("message", "Login page");
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(ModelMap map)
    {
        map.addAttribute("message", "Registered");
        return "index";
    }
}
