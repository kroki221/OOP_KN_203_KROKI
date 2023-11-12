package teleg.comands;
public class HelpCommand extends Commands {

    public static String handleCommand(String input) {
        return "Доступные команды:\n/weather - получить текущую погоду\n/joke - рассмешиться";
    }
}
