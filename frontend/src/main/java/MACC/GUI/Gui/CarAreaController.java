package MACC.GUI.Gui;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
public class CarAreaController {

    @RequestMapping(path = "/map")
    public String map(){
        return "templates/Area/map";
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String search(@RequestParam("query") String query, @RequestParam("radius") String radius, Model model) throws JSONException {
        String location;

        try {

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String encodedRadius = URLEncoder.encode(radius, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://api-gw:8080/carsearch/search?query=" + encodedQuery + "&radius=" + encodedRadius))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("RESPONSE FROM SEARCH SERVICE:" + response.body());
            location = response.body();
            JsonArray jsonArray;
            JsonObject jsonObject;
            Gson gson = new Gson();
            Type listType = new TypeToken<List<?>>() {}.getType();
            jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            jsonArray = jsonObject.get("response").getAsJsonArray();
            List<?> ll = gson.fromJson(jsonArray, listType);
            model.addAttribute("loc", location);
            model.addAttribute("radius", radius);
            model.addAttribute("locations", ll);

            // TODO test
            model.addAttribute("locationName", query);


        } catch (URISyntaxException | InterruptedException | IOException | JsonSyntaxException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return "templates/Area/map";
    }



}
