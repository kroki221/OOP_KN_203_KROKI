package teleg.service;

import java.io.IOException;

public interface ITranslator {
    String translateCity(String city) throws IOException;
}
