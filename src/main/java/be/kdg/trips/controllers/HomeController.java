package be.kdg.trips.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Subversion Id
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2012-2013
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "indexView";
    }

}
