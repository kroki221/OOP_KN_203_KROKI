package teleg.service;

import java.io.IOException;

public interface ITranslator {
    String translateCity(String city, String targetlanguage) throws IOException;

}
