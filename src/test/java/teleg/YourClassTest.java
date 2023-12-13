package teleg;

import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YourClassTest {

    @Test
    public void testTranslate_validText() throws IOException {
        OkHttpClient mockClient = mock(OkHttpClient.class);
        String responseBody = "{ \"translations\": [ { \"text\": \"Привет, мир!\" } ] }";
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("http://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(responseBody, MediaType.parse("application/json")))
                .build();
        Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockClient.newCall(Mockito.any(Request.class))).thenReturn(mockCall);
        YandexTranslate yourClass = new YandexTranslate();
        String translatedText = yourClass.translate("Hello, world!", "ru");
        assertEquals("Привет, мир!", translatedText);
    }
}
