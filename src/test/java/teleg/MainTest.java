package teleg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Assert;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
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
        String joke = ChuckJokes.getJokes();
        assertNotNull(joke);
        assertFalse(joke.isEmpty());
    }
    @Test
    public void testGetWeatherData_validCity() throws IOException {
        String request = read("./src/main/resources/testweatherjson.txt");
        String answer = "Погода в городе Almaty на данный момент - 6.0°C, но ощущается как - 3.0°C\n" +
                "Скорость ветра - 1.7\n" +
                "Время суток - ночь\n";
        Assert.assertEquals(JsonParser.dropWeather(request, "Almaty"), answer);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}

