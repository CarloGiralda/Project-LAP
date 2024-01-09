package MACC.areaservice.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class AreaServiceMap {

    private final OkHttpClient httpClient = new OkHttpClient();

    public JsonObject search(String query) throws IOException, JSONException {
        HttpRequest request = null;
        String url= URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("https://nominatim.openstreetmap.org/search?format=json&q=" + url))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        /*
        Request request = new Request.Builder()
                .url("https://nominatim.openstreetmap.org/search?format=json&q=" + query)
                .build();

         */
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JsonArray jsonArray ;
        try {
            jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
        return jsonArray.get(0).getAsJsonObject();

    }

    public JsonObject searchName(String query) throws IOException, JSONException {
        HttpRequest request = null;
        try {
            String[] lat_lon = query.split("/");

            request = HttpRequest.newBuilder()
                    .uri(new URI("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=" + lat_lon[0] +  "&lon=" + lat_lon[1]))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newHttpClient();
        /*
        Request request = new Request.Builder()
                .url("https://nominatim.openstreetmap.org/search?format=json&q=" + query)
                .build();

         */
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        JsonObject jsonObject ;
        try {
            jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            System.out.println(jsonObject);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
        return jsonObject;

    }

}
