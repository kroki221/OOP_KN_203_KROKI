package teleg;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @Test
    void helpTest() {
        String expResult, actResult;

        Main example = new Main();
        example.command("/help");

        expResult = """
                            Список команд:
                            /help - показать список команд
                            /weather - получить текущую погоду
                            /joke - рассказать шутку,\s
                            /exam - предсказать результат экзамена""";
        actResult = output.toString().trim();
        assertEquals(expResult, actResult);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
