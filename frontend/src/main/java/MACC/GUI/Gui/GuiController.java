package MACC.GUI.Gui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
public class GuiController {
    @RequestMapping(path="/")
    public String getHome(){ return "templates/index"; }

    // TODO
    @RequestMapping(path="/pages")
    public String getSuccessPage(@Param("type") String type){ return "templates/"+ type; }


}

