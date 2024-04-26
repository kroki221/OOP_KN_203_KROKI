package teleg;
import java.nio.charset.StandardCharsets;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import teleg.service.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Bot2 extends TelegramLongPollingBot {
    public String inputCity;
    public String TranslateText;
    private final IGeoService geocodingService;
    private final IWeatherService weatherService;
    private final ITranslator translator;
    private final GeoWeatherService geoWeatherService;

    public Bot2(IGeoService geocodingService, IWeatherService weatherService, ITranslator translator, GeoWeatherService geoWeatherService) {
        this.geocodingService = geocodingService;
        this.weatherService = weatherService;
        this.translator = translator;
        this.geoWeatherService = geoWeatherService;
    }

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
                try {
                    handleWeatherCommand(message, inputCity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                String commandText = update.getMessage().getText();
                SendMessage responseMessage = new SendMessage();
                long chatId = update.getMessage().getChatId();
                responseMessage.setChatId(chatId);

                Map<String, Runnable> commandHandlers = new HashMap<>();
                commandHandlers.put("/weather", () -> {
                    chatStates.put(chatId, "awaitingWeatherCity");
                    responseMessage.setText("Название города");
                    executeMessage(responseMessage);
                });
                commandHandlers.put("/translator", () -> {
                    chatStates.put(chatId, "awaitingTranslationText");
                    responseMessage.setText("Введите текст для перевода");
                    executeMessage(responseMessage);
                });
                commandHandlers.put("/start", () -> {
                    handleStartCommand(responseMessage);
                    executeMessage(responseMessage);
                });
                commandHandlers.put("/joke", () -> {
                    handleJokeCommand(responseMessage);
                    executeMessage(responseMessage);
                });
                commandHandlers.put("/help", () -> {
                    handleHelpCommand(responseMessage);
                    executeMessage(responseMessage);
                });

                Runnable commandHandler = commandHandlers.getOrDefault(commandText, () -> {
                    handleUnknownCommand(responseMessage);
                    executeMessage(responseMessage);
                });

                commandHandler.run();
            }
        } else {
            handleTextMessage(inputText, message);
            executeMessage(message);
        }
    }

    private void handleTextMessage(String inputText, SendMessage message) {
        message.setText("Вы написали: " + inputText + ", а такой команды нет");
    }

    private void handleTranslate(SendMessage message, String Translate){
        String userInput = Translate;
        System.out.println(userInput);
        try {
            String translatedText = translator.translateCity(userInput);
            message.setText("Переведенный текст: " + translatedText);
            executeMessage(message);
        } catch (IOException e) {
            System.err.println("Failed to translate text: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void handleStartCommand(SendMessage message) {
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
        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJokes();
        message.setText(joke);
        return message;
    }

    public void handleHelpCommand(SendMessage message) {
        message.setText("Погода и название города на английском");
    }

    private void handleWeatherCommand(SendMessage message, String inputCity) throws IOException {
        String weatherInfo = geoWeatherService.processWeatherRequest(inputCity);
        message.setText(weatherInfo);
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

