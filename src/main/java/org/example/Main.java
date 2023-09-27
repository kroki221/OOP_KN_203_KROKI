import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Привет! Я бот-ассистент. Чем могу помочь?");

        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        while (true) {
            String command = scanner.nextLine();
            switch (command) {
                case "/help" -> System.out.println(
                        """
                                Список команд:
                                /help - показать список команд
                                /weather - получить текущую погоду
                                /joke - рассказать шутку,\s
                                /exam - предсказать результат экзамена""");
                case "/weather" -> System.out.println(
                        "К сожалению, я не могу предоставить текущую погоду. Попробуйте воспользоваться онлайн-сервисом для получения актуальной информации о погоде в вашем городе.");
                case "/joke" -> System.out.println(
                        "Почему программисты не любят ходить гулять?" +
                                "\nПотому что они боятся выйти из своего комфортного (code)пространства!");
                case "/exam" -> {
                    int n = rand.nextInt(3);
                    System.out.println(
                            "Хочешь узнать как ты сдашь экзамен, ДА!?");
                    if (n == 0) System.out.println(
                            "ПЕРЕСДАЧА!"
                    );
                    if (n == 1) System.out.println(
                            "ТЫ СДАЛ,ВЕЗУНЧИК"
                    );
                    if (n == 2) System.out.println(
                            "ОТЧИЛСЛЕН!"
                    );
                }
                case "/exit" -> {
                    System.out.println("Пока!");
                    System.exit(0);
                }
                default -> System.out.println("Неизвестная команда. Введите /help для получения списка команд.");
            }
        }
    }
}