package MACC.GUI.Gui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CarSearchController {
    @RequestMapping(path="/searchbar")
    public String getOfferForm() { return "templates/CarSearch/newSearchBar"; }
    @RequestMapping(path = "/carsearch/{id}")
    public String getCarDetails(@PathVariable String id) { return "templates/CarSearch/carRetrieved"; }
}
