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
import teleg.service.IGeoService;

public class GeocodingService implements IGeoService {

    private final String apiKey;

    public GeocodingService() {
        apiKey = geoBotToken();
    }

    @Override
    public double getLatitude(String city) {
        return getCoordinates(city)[0];
    }

    @Override
    public double getLongitude(String city) {
        return getCoordinates(city)[1];
    }
    private double[] getCoordinates(String city) {
        try {
            String apiUrl = "https://api.opencagedata.com/geocode/v1/json?key=" + apiKey + "&q=" + city;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");

                if (results.length() > 0) {
                    JSONObject object = results.getJSONObject(0);
                    JSONObject geometry = object.getJSONObject("geometry");
                    double latitude = geometry.getDouble("lat");
                    double longitude = geometry.getDouble("lng");
                    return new double[]{latitude, longitude};
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return new double[]{0.0, 0.0};
    }

    private static String geoBotToken() {
        String apiKey = null;
        try {
            apiKey = new String(Bot2.class.getResourceAsStream("/apikeyGeoCod").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apiKey;
    }
}

