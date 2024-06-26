package src;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Menu extends Encryption {
    static Encryption encryption = new Encryption();
    static Scanner scanner = new Scanner(System.in);
    static Menu menuClass = new Menu();

    public static void main(String[] args) {
        menuClass.menu();
    }

    private void close() {
        System.out.println("\nПрограмма закрыта.\n");
    }

    private void menu() {
        try {
            System.out.println("""
                    
                    Вводите только число выбранного пункта и в последующих категорях тоже. Спасибо =)
                                        
                    Меню:
                    1. Зашифровать
                    2. Расшифровать
                    3. Ключ
                    4. Brute force
                    5. Закрыть программу
                    """);
            encryption.watchTheKey();
            switch (scanner.nextLine()) {
                case "1" -> menuOne();
                case "2" -> menuTwo();
                case "3" -> menuFree();
                case "4" -> menuFour();
                case "5" -> close();
                default -> menu();
            }
        } catch (Exception e) {
            myException();
        }
    }

    private void menuOne() {
        try {
            System.out.println("""
                    
                    Выберите один из пунктов, что зашифровать:
                    1. Текст
                    2. Файл
                    3. Вернуться в главное меню
                    
                    """);
            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println("\nВведите текст:\n");
                    String text = scanner.nextLine();
                    System.out.println("Зашифрованный текст - " + theEncryptText(text) + "\nКлюч - " + getKeyModifier());
                    menu();
                }
                case "2" -> {
                    encryptFile();
                    close();
                }
                case "3" -> menu();
                default -> menuOne();
            }
        } catch (Exception e) {
            myException();
        }
    }

    private void menuTwo() {
        try {
            System.out.println("""
                    
                    Выберите один из пунктов, что расшифровать:
                    1. Текст
                    2. Файл
                    3. Вернуться в главное меню
                                        
                    """);
            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println("\nВведите ключ, под котором зашифрован текст:\n");
                    setKeyModifier(Integer.valueOf(scanner.nextLine()));
                    System.out.println("\nВведите зашифрованный текст:\n");
                    String text = scanner.nextLine();
                    System.out.println("Расшифрованный текст - " + theDecryptText(text));
                    menu();
                }
                case "2" -> {
                    decryptFile();
                    close();
                }
                case "3" -> menu();
                default -> menuTwo();

            }
        } catch (Exception e) {
            myException();
        }
    }

    private void menuFree() {
        try {
            System.out.println("""
                    
                    ""Выберите один из пунктов:
                    1. Показать ключ
                    2. Задать новый ключ
                    3. Вернуться в главное меню
                                        
                    """);
            switch (scanner.nextLine()) {
                case "1" -> {
                    watchTheKey();
                    menuFree();
                }
                case "2" -> {
                    System.out.println("Введите новый ключ:\n");
                    setKeyModifier(Integer.valueOf(scanner.nextLine()));
                    System.out.println("Новый ключ - " + getKeyModifier());
                    menuFree();
                }
                case "3" -> menu();
                default -> menuFree();

            }
        } catch (Exception e) {
            myException();
        }
    }

    private void menuFour() {
        try {
            System.out.println("""
                    
                    Выберите один из пунктов, что расшифровать:
                    1. Текст
                    2. Файл
                    3. Вернуться в главное меню
                                        
                    """);
            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println("\nВведите текст:\n");
                    String text = scanner.nextLine();
                    System.out.println(bruteForceText(text));
                    menu();
                }
                case "2" -> {
                    bruteForceFile();
                    close();
                }
                case "3" -> menu();
                default -> menuFour();

            }
        } catch (Exception e) {
            myException();
        }
    }

    private void myException() {
        Random random = new Random();
        switch (random.nextInt(1, 7)) {
            case 1 -> throw new InputMismatchException("ты сломал мою программу, спасибо ;((((");
            case 2 -> throw new InputMismatchException("ты издеваешься, наверно? ;(");
            case 3 -> throw new InputMismatchException("опять дватцать пять, начинай сначала ;(");
            case 4 -> throw new InputMismatchException("без комментариев...");
            case 5 -> throw new InputMismatchException("вроде бы, ничего сложного, почему...");
            case 6 -> throw new InputMismatchException("с меня хватит, я ухожу.");
        }
    }
}
