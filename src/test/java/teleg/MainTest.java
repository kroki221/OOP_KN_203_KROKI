package teleg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;


public class MainTest {
    public static String read(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        line = reader.readLine();
        return line;
    }
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void testGetJokes() {
        ChuckJokes chuckJokes = new ChuckJokes();
        String joke = chuckJokes.getJokes();
        assertNotNull(joke);
        assertFalse(joke.isEmpty());
    }
    @Test
    public void testGetWeatherData_validCity() throws IOException {
        JsonParser weater = new JsonParser();
        String request = read("./src/main/resources/testweatherjson.txt");
        String Translated_City="Лондон";
        String answer = "Погода в городе " + Translated_City+ " на данный момент - 6.0°C, но ощущается как - 3.0°C\n" +
                "Скорость ветра - 1.7\n" +
                "Время суток - ночь\n";
        Assert.assertEquals(weater.dropWeather(request, "London"), answer);
    }
    @Test
    void testHandleStartCommand() {
        Bot2 bot = new Bot2();
        SendMessage message = new SendMessage();
        bot.handleStartCommand(message);
        String expectedAnswer = "Ты можешь узнать погоду, услышать шутку(несмешную)\n\n/weather - погода\n/joke - несмешная шутка\n/help - как работают команды";
        assertEquals(expectedAnswer, message.getText());
    }
    @Test
    void testHelp() {
        Bot2 bot = new Bot2();
        SendMessage message = new SendMessage();
        bot.handleHelpCommand(message);
        String expectedAnswer = "Погода и название города на английском";
        assertEquals(expectedAnswer, message.getText());
    }
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}

