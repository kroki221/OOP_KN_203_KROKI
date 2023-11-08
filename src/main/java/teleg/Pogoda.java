package teleg;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Pogoda extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "java_knbot";
    }
    @Override
    public String getBotToken() { return "6554591084:AAFZGOmECqtzr6i2EMLbSNscN-qAu27XPgs"; }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText();
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());

            if (inputText.startsWith("/")) {
                inputText = inputText.substring(1).toLowerCase();
                switch (inputText) {
                    case "start" -> {
                        message.setText(
                                "Ты можешь узнать погоду, услышать шутку(несмешную) и узнать сдашь ли ты сессию, а остольное в разработке \n\n/weather - погода\n/joke - не смешная шутка\n/exam - результат экзамена");
                    }
                    case "help" -> {
                        message.setText("/weather и название города");
                    }
                    default -> {
                        message.setText("Такой команды нет");
                    }
                }
            } else if(inputText.startsWith("Погода")) {
                inputText = inputText.substring(7);
                JsonParser jsonParser = new JsonParser();
                try {
                    message.setText(jsonParser.getWeatherData(inputText));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
