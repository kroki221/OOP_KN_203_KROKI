package teleg;
import java.nio.charset.StandardCharsets;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;
import java.util.Properties;



public class Pogoda extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "java_knbot";
    }
    @Override
    public String getBotToken() {
        String str = null;
        try {
            str = new String(Pogoda.class.getResourceAsStream("/config.txt").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

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
                                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                                keyboardMarkup.setSelective(true);
                                keyboardMarkup.setResizeKeyboard(true);
                                keyboardMarkup.setOneTimeKeyboard(false);

                                // Создаем ряды кнопок
                                KeyboardRow row1 = new KeyboardRow();
                                row1.add("/help");
                                row1.add("/weather");

                                // Добавляем ряды в разметку
                                keyboardMarkup.setKeyboard(List.of(row1));

                                // Устанавливаем разметку для сообщения
                                message.setReplyMarkup(keyboardMarkup);
                    }
                    case "help" -> {
                        message.setText("'Погода' и название города");
                    }
                    default -> {
                        message.setText("Такой команды нет");
                        message.setText(
                                "Видимо вы не посмотрели /help! \n\n/weather - погода\n/joke - не смешная шутка\n/exam - результат экзамена");
                    }
                }
            } else if(inputText.startsWith("Погода")) {
                inputText = inputText.substring(7);
                JsonParser jsonParser = new JsonParser();
                message.setText(jsonParser.getWeatherData(inputText));
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
