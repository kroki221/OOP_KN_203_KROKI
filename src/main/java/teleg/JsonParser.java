package teleg;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JsonParser{
    public static String jsonBotToken() {
        String apiKey = null;
        try {
            apiKey = new String(Bot2.class.getResourceAsStream("/jsonBotToken").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return apiKey;
    }
    static String apiKey = jsonBotToken();

    public static String getWeatherData(String city) {
        double latitude = GeocodingService.getLatitude(city);
        double longitude = GeocodingService.getLongitude(city);
        try {
            String geocodingUrl = "https://api.weather.yandex.ru/v2/forecast?lat="+latitude+"&lon="+longitude+"&extra=true";
            URL url = new URL(geocodingUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", apiKey);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            // Обработка полученных данных о погоде и извлечение необходимой информации
            return dropWeather(response.toString(), city);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String dropWeather(String request, String city) throws IOException {
        JSONObject jsonResponse = new JSONObject(request.toString());
        JSONObject factObject = jsonResponse.getJSONObject("fact");
        double temperature = factObject.getDouble("temp");
        double wind = factObject.getDouble("wind_speed");
        double fact_temp = factObject.getDouble("feels_like");
        String day_time = String.valueOf(factObject.getString("daytime"));
        if (Objects.equals(day_time, "d")) {
            day_time= "день";
        } else {
            day_time="ночь";
        }
        YandexTranslate translator = new YandexTranslate();
        String city_ru = translator.translate(city, "ru");
        // Форматирование данных о погоде в виде строки
        String weatherData = "Погода в городе " + city_ru + " на данный момент - "+ temperature + "°C,"+ " но ощущается как - "+ fact_temp+"°C"+"\n"+
                "Скорость ветра - " + wind+ "\n"+
                "Время суток - "+ day_time + "\n";
        return weatherData;
    }
}