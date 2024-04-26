package teleg.service;

public class WeatherData {
    private final double temp;
    private final double windSpeed;
    private final double feelsLike;
    private final String daytime;

    public WeatherData(double temp, double windSpeed, double feelsLike, String daytime) {
        this.temp = temp;
        this.windSpeed = windSpeed;
        this.feelsLike = feelsLike;
        this.daytime = daytime;

    }

    public double getTemp() {
        return temp;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public String getDaytime() {
        return daytime;
    }
}
