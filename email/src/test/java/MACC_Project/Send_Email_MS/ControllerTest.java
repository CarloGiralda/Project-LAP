package MACC_Project.Send_Email_MS;

import MACC_Project.Send_Email_MS.SendConfirmation.Email.BookedCar;
import MACC_Project.Send_Email_MS.SendConfirmation.Email.ExecutedPayment;
import MACC_Project.Send_Email_MS.SendConfirmation.Email.SendEmailService;
import MACC_Project.Send_Email_MS.SendConfirmation.Email.SendRequest;
import MACC_Project.Send_Email_MS.SendConfirmation.Pass.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = MACC_Project.Send_Email_MS.SendConfirmation.SendEmailMsApplication.class)
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.email-from}")
    private String emailFrom; // get email-from url from file

    @MockBean
    private UserService user;

    @MockBean
    private SendEmailService emailSender;


    @Test
    void sendRecoveryEmail() throws Exception {
        String email = emailFrom;

        when(emailSender.send(any(String.class), any(String.class),anyString())).thenReturn("Done");
        when(user.updatePass(any(String.class), any(String.class))).thenReturn(1);
        mvc.perform(get("http://localhost:8081/email/recovery")
                        .queryParam("email",email)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void sendConfirmationEmail() throws Exception {
        SendRequest request = new SendRequest("test","test", emailFrom,"","");
        when(emailSender.send(any(String.class),any(String.class), anyString())).thenReturn("Done");
        RequestBuilder requestBuilder = post("http://localhost:9001/email/sendEmail")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)).accept(MediaType.TEXT_PLAIN);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        Assertions.assertEquals("Done", result.getResponse().getContentAsString());
    }


    @Test
    void sendPaymentEmail() throws Exception {
        ExecutedPayment request = new ExecutedPayment(emailFrom,emailFrom,1L);
        when(emailSender.send(any(String.class),any(String.class), anyString())).thenReturn("Done");
        RequestBuilder requestBuilder = post("http://localhost:9001/email/sendPaymentEmail")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)).accept(MediaType.TEXT_PLAIN);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        Assertions.assertEquals("Done", result.getResponse().getContentAsString());
    }

    @Test
    void sendCarBookedEmail() throws Exception {
        BookedCar request = new BookedCar(1L,emailFrom,emailFrom);
        when(emailSender.send(any(String.class),any(String.class), anyString())).thenReturn("Done");
        RequestBuilder requestBuilder = post("http://localhost:9001/email/sendCarBookedEmail")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)).accept(MediaType.TEXT_PLAIN);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        Assertions.assertEquals("Done", result.getResponse().getContentAsString());
    }



    @Test
    void modifyUserSettings() throws Exception {
        SendRequest request = new SendRequest("test","test", emailFrom,"","");

        when(user.updateNonNullFields(any(SendRequest.class))).thenReturn(1);

        mvc.perform(
                        post("http://localhost:9001/email/userSettings/modify")
                                .header("Logged-In-User", "user")
                                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }



}




