import java.io.*;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook {
    private ConcurrentHashMap<String, String> phoneBookMap = null;

    public PhoneBook() {
        phoneBookMap = new ConcurrentHashMap<>();
    }

    public String get(String name) {
        String result = "";

        String number = phoneBookMap.get(name);
        if (number == null)
            result = "ERROR no name in phonebook";
        else result = "OK " + number;

        return result;
    }

    public String put(String name, String number) {
        phoneBookMap.put(name, number);
        return "OK";
    }

    public String replace(String name, String number) {
        String result = "";

        if(phoneBookMap.containsKey(name)) {
            phoneBookMap.replace(name, number);
            result = "OK";
        }
        else result = "ERROR no name in phonebook";
        return result;
    }

    public String delete(String name) {
        String result = "";

        if(phoneBookMap.containsKey(name)) {
            phoneBookMap.remove(name);
            result = "OK";
        }
        else result = "ERROR no name in phonebook";
        return result;
    }

    public String list() {
        String result = "OK ";

        for (String key : phoneBookMap.keySet()) {
            result += key + " ";
        }

        return result;
    }

    public String save(String filename) {
        String result = "";
        Properties pairsToSave = new Properties();

        for (String key : phoneBookMap.keySet()) {
           pairsToSave.put(key, phoneBookMap.get(key));
        }

        File file = new File(filename);

        try {
            file.createNewFile();

            try (FileOutputStream os = new FileOutputStream(filename)) {
                pairsToSave.store(os, null); //zapis
                result = "OK";
            }
            catch (IOException e) {
                result = "ERROR during saving data to file";
            }
        } catch (IOException e) {
            result = "ERROR during opening the file";
        }

        return result;
    }

    public String load(String filename) {
        String result = "";

        try {
            FileInputStream is = new FileInputStream(filename);

            Properties pairsToLoad = new Properties();
            try {
                pairsToLoad.load(is); //odczyt z pliku do zmiennej pairsToLoad

                for (Object keyByte : pairsToLoad.keySet()) {
                    if (keyByte instanceof String) {
                        String key = (String) keyByte; //tworzymy stringa z postaci bajtowej
                        phoneBookMap.putIfAbsent(key, pairsToLoad.getProperty(key));
                    }
                    else {
                        result = "ERROR during loading data from the file";
                        break;
                    }
                }
                result = "OK";
            } catch (IOException e) {
                result = "ERROR during loading data from the file";
            }
        } catch (FileNotFoundException e) {
            result = "ERROR file " + filename + " not found";
        }

        return result;
    }
}
