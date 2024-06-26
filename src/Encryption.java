package src;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Encryption {
    private HashMap<Integer, String> alphabetRuUp = new HashMap<>();
    private HashMap<Integer, String> alphabetRuLow = new HashMap<>();
    private static final String ALPHABET_RU_UP =  "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ ,.:-!?";
    private static final String ALPHABET_RU_LOW = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private char[] charArrayUp = ALPHABET_RU_UP.toCharArray();
    private char[] charArrayLow = ALPHABET_RU_LOW.toCharArray();
    private static int keyModifier = 5;

    private String[] wordKey = {
            "привет", "по ", "на ", "то ", "или ", "зачем",
            "почему", "потому", "как", "за", "где", "когда",
            "сколько", "мы ", "вы ", "ты ", "он ", "она ",
            "они ", "её ", "его "};

    private void createHashMap() {
        int counterUp = 0;
        int counterLow = 0;

        for (int i = 0; i < charArrayUp.length; i++) {
            alphabetRuUp.put(counterUp, String.valueOf(charArrayUp[i]));
            counterUp++;
        }

        for (int i = 0; i < charArrayLow.length; i++) {
            alphabetRuLow.put(counterLow, String.valueOf(charArrayLow[i]));
            counterLow++;
        }

    }

    public void setKeyModifier(int keyModifier) {
        this.keyModifier = keyModifier;
    }

    public int getKeyModifier() {
        return keyModifier;
    }

    public void watchTheKey() {
        System.out.println("Ключ на данный момент - " + getKeyModifier() + "\n");
    }

    public String theEncryptText(String string) {
        createHashMap();

        char[] chars = string.toCharArray();
        String result = "";

        for (int i = 0; i < chars.length; i++) {
            for (Map.Entry<Integer, String> entry : alphabetRuUp.entrySet()) {  //Сравниваем символ полученного текста с каждым ключом HashMap(алфавив заглавных букв)
                Integer key = entry.getKey();                                   //Ключ буквы
                String value = entry.getValue();                                //Буква алфавита для сравнения с символом
                if (value.equals(String.valueOf(chars[i]))) {                   //Сравнение с учетом регистра
                    if (key + getKeyModifier() < charArrayUp.length) {          //Если сумма ключа буквы и ключа для сдвига букв дает меньше длины HashMap
                        result += alphabetRuUp.get(key + getKeyModifier());     //то добавлем символ в пустую строку(или в порядке очереди)
                    } else {                                                    //Если сумма ключа и HashMap превышает
                        int y = key + getKeyModifier() - charArrayUp.length;    //то вычитаем из суммы ключа и мапы длину алфавита, с получившимся числом бежим от начала алфавита
                        result += alphabetRuUp.get(y);
                    }
                }
            }
            for (Map.Entry<Integer, String> entry : alphabetRuLow.entrySet()) { //Всё то же самое, только с прописными буквами
                Integer key = entry.getKey();
                String value = entry.getValue();
                if (value.equals(String.valueOf(chars[i]))) {
                    if (key + getKeyModifier() < charArrayLow.length) {
                        result += alphabetRuLow.get(key + getKeyModifier());
                    } else {
                        int y = key + getKeyModifier() - charArrayLow.length;
                        result += alphabetRuLow.get(y);
                    }
                }
            }

        }
        return result;
    }

    public String theDecryptText(String string) {
        createHashMap();

        char[] chars = string.toCharArray();
        String result = "";

        for (int i = 0; i < chars.length; i++) {
            for (Map.Entry<Integer, String> entry : alphabetRuUp.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                if (value.equals(String.valueOf(chars[i]))) {
                    if (key - getKeyModifier() >= 0) {
                        result += alphabetRuUp.get(key - getKeyModifier());         //Все то же самое, только ключ вычитаем
                    } else {
                        int y = charArrayUp.length - (getKeyModifier() - key);      //Если при вычитании будет минус, то это число отнимаем от конца алфавита
                        result += alphabetRuUp.get(y);
                    }
                }
            }

            for (Map.Entry<Integer, String> entry : alphabetRuLow.entrySet()) {
                Integer key = entry.getKey();
                String value = entry.getValue();
                if (value.equals(String.valueOf(chars[i]))) {
                    if (key - getKeyModifier() >= 0) {                              //тут идентично
                        result += alphabetRuLow.get(key - getKeyModifier());
                    } else {
                        int y = charArrayLow.length - (getKeyModifier() - key);
                        result += alphabetRuLow.get(y);
                    }
                }
            }

        }
        return result;
    }

    public String bruteForceText(String string) {
        int theSelectionKey = 1;
        while (true) {
            setKeyModifier(theSelectionKey);                                                    //Начинаем с 1 ключа
            String textChecked = theDecryptText(string);                                          //Расшифровываем с заданым ключом
            for (int indexWordKey = 0; indexWordKey < wordKey.length; indexWordKey++) {         //Массив строк с ключевыми словами
                if (textChecked.indexOf(wordKey[indexWordKey]) != -1) {                         //Если есть совпадение в расшифрованном тексте
                    return "Расшифрованный текст - " + textChecked + "\nКлюч - " + theSelectionKey;//Возврашаем данный расш. текст
                } else if (theSelectionKey > charArrayUp.length) {                              //Если длина ключа превысила длину алфавита, то ключ не найден
                    return "Ключ не найден.";
                }
            }
            theSelectionKey++;                                                                  //Прибавляем ключ, если не одно из условий не выполнилось и по новой
        }
    }
    public void encryptFile() {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Введите путь к файлу с котого хотите зашифровать текст.");

            Path files1 = Path.of(scanner.nextLine());                  //Принимаем файл с которого будем брать текст
            Path directoryfiles1 = Path.of(files1.toUri()).getParent(); //Берем директорию у файла для создания нового с зашифрованным текстом
            String readFiles1 = Files.readString(files1);               //Считываем текст с полученного файла
            String writedFIles1 = theEncryptText(readFiles1);               //Шифруем этот текст медотом, которой создали в другом классе

            System.out.println("Введите имя нового создаваемого файла, в который поместим зашифрованный текст.");

            String nameNewFales = "\\" + scanner.nextLine();                            //Получаем имя для нового файла
            Files.createFile(Path.of(directoryfiles1.toString() + nameNewFales));  //Создаем новый файл
            Path path = Path.of(directoryfiles1.toString() + nameNewFales);        //Получаем новосозданный файл
            Files.writeString(path, writedFIles1);                                      //Записываем наш зашифрованный текст в новый файл
            System.out.println("Файл создан по указанному пути и содержит зашифрованный текст.");

        } catch (Exception e) {
            new FileNotFoundException("Введите правильный путь к файлу.");
        }
    }

    public void decryptFile() {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Введите путь к файлу с котого хотите расшифровать текст.");

            Path files1 = Path.of(scanner.nextLine());                  //Принимаем файл с котого будем брать зашифрованный текст
            Path directoryfiles1 = Path.of(files1.toUri()).getParent(); //Берем директорию у файла для создания нового с расшифрованным текстом
            String readFiles1 = Files.readString(files1);               //Считывает текст с полученного файлы
            String writedFIles1 = theDecryptText(readFiles1);             //Расшифровываем текст методом, который создали в другом классе

            System.out.println("Введите имя нового файла, в который поместим расшифрованный текст.");
            String nameNewFales = "\\" + scanner.nextLine();                                 //Получаем имя для нового файла
            Files.createFile(Path.of(directoryfiles1.toString() + nameNewFales));       //Создаем новый файл
            Path path = Path.of(directoryfiles1.toString() + nameNewFales);             //Получаем новосозданный файл
            Files.writeString(path, writedFIles1);                                           //Записываем расшифрованный текст в новый файл
            System.out.println("Файл создан по указанному пути и содержит расшифрованный текст.");

        } catch (Exception e) {
            new FileNotFoundException("Введите правильный путь к файлу.");
        }
    }

    public void bruteForceFile() {
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Введите путь к файлу с котого хотите расшифровать текст методом Brute force.");

            Path files1 = Path.of(scanner.nextLine());                  //Получаем файл с которого будем расшифровывать текст
            Path directoryfiles1 = Path.of(files1.toUri()).getParent(); //Получаем дерикторию для нового файла, в который поместим полученный результат
            String readFiles1 = Files.readString(files1);               //Читаем текст с полученного файла
            String bruteForceText = bruteForceText(readFiles1);         //Пытаемся расшифровать текст медотом, котороый создали в другом классе

            if (bruteForceText.equalsIgnoreCase("Ключ не найден.")) {    //Метод выдет "Ключ не найден." если не смог расшифровать,
                System.out.println("Ключ не найден.");                               //поэтому не будем продолжать работу метода и скажем об этом
                return;
            }

            System.out.println("Введите имя нового файла, в который поместим расшифрованный текст и подобранный ключ.");
            String nameNewFales = "\\" + scanner.nextLine();                                //Получаем имя для new file
            Files.createFile(Path.of(directoryfiles1.toString() + nameNewFales));      //Create new file
            Path path = Path.of(directoryfiles1.toString() + nameNewFales);            //Получаем наш new file
            Files.writeString(path, bruteForceText);                                        //Записываем в него результат
            System.out.println("Файл создан по указанному пути и содержит расшифрованный текст с ключом.");

        } catch (Exception e) {
            new FileNotFoundException("Введите правильный путь к файлу.");
        }
    }
}
