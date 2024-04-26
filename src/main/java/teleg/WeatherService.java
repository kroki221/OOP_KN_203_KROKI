package teleg;

import org.json.JSONObject;
import teleg.service.IWeatherService;
import teleg.service.WeatherData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class WeatherService implements IWeatherService {
    private static final String API_KEY = getApiKey();

    private static String getApiKey() {
        String apiKey = null;
        try {
            apiKey = new String(WeatherService.class
                    .getResourceAsStream("/jsonBotToken").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apiKey;
    }

    @Override
    public WeatherData getWeather(double latitude, double longitude) {
        try {
            String geocodingUrl = "https://api.weather.yandex.ru/v2/forecast?lat=" + latitude + "&lon=" + longitude + "&extra=true";
            URL url = new URL(geocodingUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", API_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return parseWeatherResponse(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private WeatherData parseWeatherResponse(String request) throws IOException {
        JSONObject jsonResponse = new JSONObject(request.toString());
        JSONObject factObject = jsonResponse.getJSONObject("fact");

        double temperature = factObject.getDouble("temp");
        double windSpeed = factObject.getDouble("wind_speed");
        double feelsLike = factObject.getDouble("feels_like");
        String daytime = factObject.getString("daytime");

        if (Objects.equals(daytime, "d")) {
            daytime = "день";
        } else {
            daytime = "ночь";
        }

        return new WeatherData(temperature, windSpeed, feelsLike, daytime);
    }
}
