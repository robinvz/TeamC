package be.kdg.trips;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
public class HelloController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String sayHello(ModelMap modelMap)
    {
        modelMap.addAttribute("message", "Hello from Spring MVC.");
        return "index";
    }
}
