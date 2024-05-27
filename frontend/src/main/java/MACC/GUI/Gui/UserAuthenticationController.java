package MACC.GUI.Gui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserAuthenticationController {

    @RequestMapping(path="/registration")
    public String getRegistrationForm() { return "templates/UserAuthentication/registration"; }

    @RequestMapping(path="/login")
    public String getLoginForm() { return "templates/UserAuthentication/login"; }

    @RequestMapping(path="/userSettings")
    public String getUserSettingsPage(){ return "templates/UserSettings/userSettings"; }

    @RequestMapping(path="/recovery")
    public String getRecoveryPage() { return "templates/UserSettings/recovery"; }

    @RequestMapping(path="/upload-file")
    public String getUploadPage(){ return "templates/Upload/upload"; }

}
