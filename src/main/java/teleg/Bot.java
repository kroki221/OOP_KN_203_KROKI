package teleg;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.SerializationUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import teleg.comands.Commands;
import teleg.comands.HelpCommand;
import teleg.comands.StartCommand;

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
                if (inputText.startsWith("weather")){
                    byte[] bytes=SerializationUtils.serialize(inputText);
                    inputCity = SerializationUtils.deserialize(bytes);
                    inputCity=inputCity.substring(8,length(inputText));
                    inputText=inputText.substring(0,7).toLowerCase();
                }
                switch (inputText) {
                    case "start" -> {
                        message.setText(
                                "Ты можешь узнать погоду, услышать шутку(несмешную)\n\n/weather - погода\n/joke - несмешная шутка\n/help - как работают команды");
                                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                                keyboardMarkup.setSelective(true);
                                keyboardMarkup.setResizeKeyboard(true);
                                keyboardMarkup.setOneTimeKeyboard(false);

                                // Создаем ряды кнопок
                                KeyboardRow row = new KeyboardRow();
                                KeyboardRow row1 = new KeyboardRow();
                                row1.add("/help");
                                row1.add("/weather");
                                row1.add("/joke");
                                row.add("ТЕСТ");
                                // Добавляем ряды в разметку
                                keyboardMarkup.setKeyboard(List.of(row1,row));
                                // Устанавливаем разметку для сообщения
                                message.setReplyMarkup(keyboardMarkup);
                    }
                    case "joke" -> {
                        message.setText(ChuckJokes.GetJokes());
                    }
                    case "help" -> {
                        message.setText("'Погода' и название города(на английском)");
                    }
                    case "weather" -> {
                        JsonParser jsonParser = new JsonParser();
                        message.setText(jsonParser.getWeatherData(inputCity));
                        System.out.println(inputCity);
                    }
                    default -> {
                        message.setText("Такой команды нет");
                        message.setText(
                                "Видимо вы не посмотрели /help! \n\n/weather - погода\n/joke - не смешная шутка");
                    }
                }
            }
            try {
                execute(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String inputText = update.getMessage().getText();
//            SendMessage message = new SendMessage();
//            message.setChatId(update.getMessage().getChatId());
//
//                if (inputText.startsWith("/")) {
//                    inputText = inputText.substring(1).toLowerCase();
//                    Commands commands = null;
//                    switch (inputText) {
//                        case "start" -> commands = new StartCommand();
//                        case "help" -> commands = new HelpCommand();
//                        case "weather" -> {
//                            inputText = inputText.substring(7);
//                            JsonParser jsonParser = new JsonParser();
//                            message.setText(jsonParser.getWeatherData(inputText));
//                        }
////        case "joke" -> commands = new JokeCommand();
//                        default -> message.setText("Такой команды нет");
//                    }
//                    if (commands != null) {
//                        message.setText(Commands.handleCommandmain(inputText));
//                    }
//                }
//                try {
//                    execute(message);
//                } catch (TelegramApiException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
    }

