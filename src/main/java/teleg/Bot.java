package teleg;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.SerializationUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.util.List;
import static org.apache.commons.io.IOUtils.length;


public class Bot extends TelegramLongPollingBot {
    public String inputCity;

    @Override
    public String getBotUsername() {
        return "java_knbot";
    }

    @Override
    public String getBotToken() {
        String str = null;
        try {
            str = new String(Bot.class.getResourceAsStream("/config.txt").readAllBytes(), StandardCharsets.UTF_8);
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
                if (inputText.startsWith("weather")) {
                    byte[] bytes = SerializationUtils.serialize(inputText);
                    inputCity = SerializationUtils.deserialize(bytes);
                    inputCity = inputCity.substring(8, length(inputText));
                    inputText = inputText.substring(0, 7).toLowerCase();
                }
                switch (inputText) {
                    case "start" -> {
                        handleStartCommand(message);
                        executeMessage(message);
                    }
                    case "joke" -> {
                        handleJokeCommand(message);
                        executeMessage(message);
                    }
                    case "help" -> {
                        handleHelpCommand(message);
                        executeMessage(message);
                    }
                    case "weather" -> {
                        handleWeatherCommand(message, inputCity);
                        executeMessage(message);
                    }
                    default -> {
                        handleUnknownCommand(message);
                        executeMessage(message);
                    }
                }
            }
        }
    }

    private void handleStartCommand(SendMessage message) {
        message.setText("Ты можешь узнать погоду, услышать шутку(несмешную)\n\n/weather - погода\n/joke - несмешная шутка\n/help - как работают команды");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow row = new KeyboardRow();
        row.add("TEST");

        KeyboardRow row1 = new KeyboardRow();
        row1.add("/help");
        row1.add("/weather");
        row1.add("/joke");

        keyboardMarkup.setKeyboard(List.of(row1, row));

        message.setReplyMarkup(keyboardMarkup);
    }

    public BotApiMethodMessage handleJokeCommand(SendMessage message) {
        message.setText(ChuckJokes.getJokes());
        return message;
    }

    private void handleHelpCommand(SendMessage message) {
        message.setText("'Погода' и название города(на английском)");
    }

    private void handleWeatherCommand(SendMessage message, String inputCity) {
        JsonParser jsonParser = new JsonParser();
        message.setText(jsonParser.getWeatherData(inputCity));
    }

    private void handleUnknownCommand(SendMessage message) {
        message.setText("Такой команды нет\n"+
                "Видимо вы не посмотрели /help!" +
                " \n/weather - погода\n" +
                "/joke - не смешная шутка");
    }
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
