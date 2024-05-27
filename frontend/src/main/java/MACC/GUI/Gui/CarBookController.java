package MACC.GUI.Gui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CarBookController {


    @RequestMapping(path = "/book/{id}")
    public String reservation(@PathVariable String id){
        return "templates/CarBook/reservationForm";
    }

    @RequestMapping(path = "/extend")
    public String extendReservation(@RequestParam(name = "bid", required = false) String bid,
                                    @RequestParam(name = "cid", required = false) String cid,
                                    Model model) {
        model.addAttribute("bid", bid);
        model.addAttribute("cid", cid);

        return "templates/CarBook/extend";
    }
    @RequestMapping(path = "/bookingHistory")
    public String bookingHistory(){
        return "templates/CarBook/bookingHistory";
    }

    @RequestMapping(path="/inProgress")
    public String getOfferForm6() { return "templates/CarBook/inProgress"; }

}
