package teleg.service;

public interface IWeatherService {
    WeatherData getWeather(double latitude, double longitude);
}
