package be.kdg.trips.controllers;

import be.kdg.trips.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

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
    @ModelAttribute
    public User newRequest(@RequestParam(required=false) Integer id) {
        return (new User());
    }

    /**
     * <p>Saves a person.</p>
     *
     * <p>Expected HTTP POST and request '/person/form'.</p>
     */
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public void form(User person, Model model) {
        System.out.println("asdfasdfasdf"+ person.getPassword());
    }


    /**
     * <p>Person form request.</p>
     *
     * <p>Expected HTTP GET and request '/person/form'.</p>
     */
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public void form() {
    }
}
