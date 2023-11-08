package teleg;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

public class JsonParser {
    private final OkHttpClient client = new OkHttpClient();
    private final String API_KEY = "2fd984f1664e5d639892a45676712d28";

    public String getWeatherData(String city) throws IOException {
        // Make a GET request to the OpenWeatherMap API
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String jsonString = responseBody.string();

                // Extract relevant weather information from the JSON response
                JSONObject jsonObject = new JSONObject(jsonString);
                String cityName = jsonObject.getString("name");
                JSONObject mainObject = jsonObject.getJSONObject("main");
                double temperature = mainObject.getDouble("temp") - 273;

                // Format the weather data as a string
                String weatherData = "Current weather in " + cityName + ": " + temperature + "°C";
                return weatherData;
            }
        }
        return "";
    }
}
