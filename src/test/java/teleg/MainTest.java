package teleg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.mockito.Mockito.*;


public class MainTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

        @Test
        public void testGetJokes() {
            String joke = ChuckJokes.GetJokes();
            assertNotNull(joke);
            assertFalse(joke.isEmpty());
        }
    @Test
    public void testGetWeatherData_validCity() {
        // Arrange
        String city = "New York";

        // Act
        String weatherData = JsonParser.getWeatherData(city);

        // Assert
        assertNotNull(weatherData);
        assertTrue(weatherData.contains("Погода в городе New York на данный момент"));
    }

    @Test
    public void testGetWeatherData_invalidCity() {
        // Arrange
        String city = "Invalid City";

        // Act
        String weatherData = JsonParser.getWeatherData(city);

        // Assert
        assertEquals("", weatherData);
    }


    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
