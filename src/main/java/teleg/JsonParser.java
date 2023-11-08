package teleg;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class JsonParser{
    private final String apiKey = "0c9893b0-794e-4ab1-a779-d7d43643ab5b";

    public String getWeatherData(String city) {
        try {
            URL url = new URL("https://api.weather.yandex.ru/v2/forecast?lat=55.75396&lon=37.620393&extra=true");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Yandex-API-Key", apiKey);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject forecast = new JSONObject(response.toString());
            JSONObject jsonResponse = new JSONObject(response.toString());
            // Обработка полученных данных о погоде и извлечение необходимой информации
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

            // Форматирование данных о погоде в виде строки
            String weatherData = "Погода в городе " + city + " на данный момент - "+ temperature + "°C,"+ " но ощущается как - "+ fact_temp+"°C"+"\n"+
                    "Скорость ветра - " + wind+ "\n"+
                    "Время суток - "+ day_time + "\n";
            return weatherData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}