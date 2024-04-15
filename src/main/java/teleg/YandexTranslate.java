package teleg;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class YandexTranslate {
    private static final String TRANSLATE_URL = "https://translate.api.cloud.yandex.net/translate/v2/translate";

    private OkHttpClient client;
    private String folderId1;
    private String iamToken1;
    public YandexTranslate() {
        this.client = new OkHttpClient();
        loadConfiguration();
    }

    private void loadConfiguration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                folderId1 = prop.getProperty("folderId");
                iamToken1 = prop.getProperty("iamToken");
            } else {
                throw new IOException("Unable to load configuration file");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String translate(String text, String targetLanguage) throws IOException {
        String folderId = folderId1;
        String iamToken = iamToken1;
        JSONArray texts = new JSONArray(Collections.singletonList(text));
        JSONObject body = new JSONObject()
                .put("targetLanguageCode", targetLanguage)
                .put("texts", texts)
                .put("folderId", folderId);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, body.toString());
        Request request = new Request.Builder()
                .url(TRANSLATE_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + iamToken)
                .build();
        System.out.println(request);
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                System.out.println((jsonResponse));
                JSONArray translations = jsonResponse.getJSONArray("translations");
                System.out.println(translations);
                if (translations.length() > 0) {
                    JSONObject translationObject = translations.getJSONObject(0);
                    String translatedText = translationObject.getString("text");
                    return translatedText;
                } else {
                    throw new IOException("No translations found in the response");
                }
            } else {
                throw new IOException("Error: " + response.code() + " - " + response.message());
            }
        }
    }
}
