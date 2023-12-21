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
import java.util.HashMap;
import java.util.List;
import static org.apache.commons.io.IOUtils.length;


public class Bot2 extends TelegramLongPollingBot {
    public String inputCity;
    public String TranslateText;
    @Override
    public String getBotUsername() {
        return "java_knbot";
    }

    @Override
    public String getBotToken() {
        String str = null;
        try {
            str = new String(Bot2.class.getResourceAsStream("/config.txt").readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return str;
    }
    private HashMap<Long, String> chatStates = new HashMap<>();

    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);

            if (chatStates.containsKey(chatId) && chatStates.get(chatId).equals("awaitingWeatherCity")) {
                // Пользователь должен предоставить название города
                inputCity = inputText;
                chatStates.remove(chatId); // Сбросить состояние

                // Обработать команду погоды с предоставленным городом
                handleWeatherCommand(message, inputCity);
                executeMessage(message);
            } else if (chatStates.containsKey(chatId) && chatStates.get(chatId).equals("awaitingTranslationText")) {
                // Пользователь должен предоставить название города
                TranslateText = inputText;
                chatStates.remove(chatId); // Сбросить состояние

                // Обработать команду погоды с предоставленным городом
                handleTranslate(message, TranslateText);
            }
            else {
                // Пользователь не находится в специальном состоянии, обрабатывать команды как обычно
                processCommand(inputText, message, update); // Передача объекта update
            }
        }
    }

    private void processCommand(String inputText, SendMessage message, Update update) {
        if (inputText.startsWith("/")) {
            if (update != null && update.hasMessage() && update.getMessage().hasText()) {
                // Здесь изменено имя переменной inputText, чтобы избежать конфликта имен
                String commandText = update.getMessage().getText();
                SendMessage responseMessage = new SendMessage();
                long chatId = update.getMessage().getChatId(); // Используем update для получения чата
                responseMessage.setChatId(chatId);

                // Проверить команду "weather"
                if (commandText.startsWith("/weather")) { // Обратите внимание на "/"
                    chatStates.put(chatId, "awaitingWeatherCity");
                    responseMessage.setText("Название города");
                    executeMessage(responseMessage);
                    return;
                }

                // Проверить команду "translator"
                if (commandText.startsWith("/translator")) {
                    chatStates.put(chatId, "awaitingTranslationText");
                    responseMessage.setText("Введите текст для перевода");
                    executeMessage(responseMessage);
                    return;
                }

                // Другая логика обработки команд...

                switch (commandText) {
                    case "/start":
                        handleStartCommand(responseMessage);
                        executeMessage(responseMessage);
                        break;
                    case "/joke":
                        handleJokeCommand(responseMessage);
                        executeMessage(responseMessage);
                        break;
                    case "/help":
                        handleHelpCommand(responseMessage);
                        executeMessage(responseMessage);
                        break;
                    default:
                        handleUnknownCommand(responseMessage);
                        executeMessage(responseMessage);
                        break;
                }
            } else {
                // Обработка текстовых сообщений без команд, если не в состоянии ожидания
                handleTextMessage(inputText, message);
                executeMessage(message);
            }
        }
    }

    private void handleTextMessage(String inputText, SendMessage message) {
        // Обработка текстовых сообщений без команд
        message.setText("Вы написали: " + inputText + ", а такой команды нет");
    }

    private void handleTranslate(SendMessage message, String Translate){
        String userInput = Translate;
        System.out.println(userInput);
        String targetLanguage = "ru";
        try {
            YandexTranslate translator = new YandexTranslate();
            String translatedText = translator.translate(userInput, targetLanguage);
            message.setText("Переведенный текст: " + translatedText);
            executeMessage(message);
        } catch (IOException e) {
            System.err.println("Failed to translate text: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void handleStartCommand(SendMessage message) {
        message.setText("Ты можешь узнать погоду, услышать шутку(несмешную)\n\n/weather - погода\n/joke - несмешная шутка\n/help - как работают команды");
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow startRow = new KeyboardRow();
        startRow.add("/start");

        KeyboardRow row = new KeyboardRow();
        row.add("/translator");
        row.add("/weather");
        KeyboardRow row1 = new KeyboardRow();
        row1.add("/help");
        row1.add("/joke");

        keyboardMarkup.setKeyboard(List.of(startRow, row1, row));

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

