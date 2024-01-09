package MACC.GUI.Gui;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class CarAreaController {

    @RequestMapping(path = "/map")
    public String map(){
        return "templates/Area/map";
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public String search(@RequestParam("query") String query, @RequestParam("radius") String radius,@RequestParam("token") String token, Model model) throws JSONException {
        String location;
        System.out.println("---------"+token);
        try {
            //this.sendGet(query, radius);
            String url= URLEncoder.encode(query, StandardCharsets.UTF_8.name());
            System.out.println(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/area/search?query=" + url+"&radius="+radius))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("RESPONSE FROM AREA SERVICE:"+response.body());
            location = response.body();
            JsonArray jsonArray ;
            JsonObject jsonObject ;
            Gson gson = new Gson();
            Type listType = new TypeToken<List<?>>() {}.getType();
            jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            jsonArray=jsonObject.get("response").getAsJsonArray();
            List<?> ll = gson.fromJson(jsonArray, listType);
            model.addAttribute("loc", location);
            model.addAttribute("radius", radius);
            model.addAttribute("locations", ll);
            System.out.println(jsonArray);

        } catch (URISyntaxException | InterruptedException | IOException | JsonSyntaxException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        return "templates/Area/map.html";
    }
    private void sendGet(String query,String radius) throws IOException {
        URL obj = new URL("http://localhost:8080/area/search?query=" + query+"&radius="+radius);
        System.out.println("OOOOO"+obj);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            var in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request did not work.");
        }

    }



}
