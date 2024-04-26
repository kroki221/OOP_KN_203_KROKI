package teleg.service;

import java.io.IOException;

public class GeoWeatherService {

    private final IGeoService geoService;
    private final IWeatherService weatherService;
    private final ITranslator translator;

    public GeoWeatherService(IGeoService geoService, IWeatherService weatherService, ITranslator translator) {
        this.geoService = geoService;
        this.weatherService = weatherService;
        this.translator = translator;

    }

    public String processWeatherRequest(String city) throws IOException {
        double longitude = geoService.getLongitude(city);
        double latitude = geoService.getLatitude(city);

        WeatherData weather = weatherService.getWeather(latitude, longitude);
        String translatedCity = translator.translateCity(city, "ru");

        String messageText = "Погода в городе " + translatedCity +
                " на данный момент - "+ weather.getTemp() + "°C,"+
                " но ощущается как - "+ weather.getFeelsLike()+"°C"+"\n"+
                "Скорость ветра - " + weather.getWindSpeed()+ "\n"+
                "Время суток - "+ weather.getDaytime() + "\n";
        return messageText;
    }
}
