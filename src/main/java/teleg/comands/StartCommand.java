package teleg.comands;

public class StartCommand extends Commands {
    public static String handleCommand(String input) {
        return "Добро пожаловать! Я бот, который может помочь вам с погодой и рассмешить вас шуткой. Просто введите /help, чтобы увидеть доступные команды.";
    }
}
