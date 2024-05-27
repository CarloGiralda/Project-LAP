package MACC.GUI.Gui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ChatController {

    @RequestMapping(path = "/chat/page")
    public String getFormPage() { return "templates/Chat/formPage"; }

    @RequestMapping(path = "/chat/m", method = GET)
    public String messages(Model model, @RequestParam String sender, @RequestParam String receiver) {
        model.addAttribute("sender", sender);
        model.addAttribute("receiver", receiver);
        return "templates/Chat/chatIndex";}

}
