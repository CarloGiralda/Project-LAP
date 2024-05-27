package MACC.GUI.Gui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CarInsertionController {

    @RequestMapping(path="/carinsert/insertoffer")
    public String getOfferForm3() { return "templates/CarInsertion/offerForm"; }
    @RequestMapping(path="/carinsert/insertcar")
    public String getOfferForm4() { return "templates/CarInsertion/carForm"; }
    @RequestMapping(path="/carinsert/insertutilities")
    public String getOfferForm5() { return "templates/CarInsertion/utilitiesForm"; }

    @RequestMapping(path="/carinsert/modify/{id}")
    public String getModificationForm(@PathVariable Long id) { return "templates/CarInsertion/modificationForm"; }


    @RequestMapping(path="/success")
    public String getOfferForm6() { return "templates/CarInsertion/success"; }
    @RequestMapping(path="/failed")
    public String getOfferForm7() { return "templates/failed"; }

}
