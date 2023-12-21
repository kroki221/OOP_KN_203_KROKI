package teleg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeocodingService {

    public static String geoBotToken() {
        String apiKey = null;
        try {
            apiKey = new String(Bot2.class.getResourceAsStream("/apikeyGeoCod").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apiKey;
    }
    static String apiKey = geoBotToken();

    public static double getLatitude(String city) {
        try {
            String apiUrl = "https://api.opencagedata.com/geocode/v1/json?key=" + apiKey + "&q=" + city;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject object = results.getJSONObject(0);
                JSONObject geometry = object.getJSONObject("geometry");
                double latitude = geometry.getDouble("lat");
                System.out.println(results);
                System.out.println(latitude);
                return latitude;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
    public static double getLongitude(String city) {
        try {
            String apiUrl = "https://api.opencagedata.com/geocode/v1/json?key=" + apiKey +"&q=" + city;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray results = jsonResponse.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject object = results.getJSONObject(0);
                JSONObject geometry = object.getJSONObject("geometry");
                double longitude = geometry.getDouble("lng");
                System.out.println(longitude);
                return longitude;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}

