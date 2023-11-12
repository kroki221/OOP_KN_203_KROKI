package teleg.comands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import teleg.Bot;

public abstract class Commands extends Bot {
    public static String handleCommandmain(String input) {
        if (input=="start"){
            return StartCommand.handleCommand(input);
        }
        if (input=="help"){
            return HelpCommand.handleCommand(input);
        }
        else{
            return "";
        }
    }
}


