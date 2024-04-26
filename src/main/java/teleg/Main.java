package teleg;
import teleg.service.*;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) throws TelegramApiException {
        IGeoService geocodingService = new GeocodingService();
        IWeatherService weatherService = new WeatherService();
        ITranslator translator = new YandexTranslate();
        GeoWeatherService geoWeatherService = new GeoWeatherService(geocodingService, weatherService, translator);

        Bot2 bot = new Bot2(geocodingService, weatherService, translator, geoWeatherService);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}