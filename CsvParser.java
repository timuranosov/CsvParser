import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 10.04.2017.
 */
public class CsvParser {
    private static final String STRING_DELIMITER = ",";
    private static List<List<String>> data;
    private static final String lineSeparator = System.getProperty("line.separator");

    //создание csv-файла
    public static  void generateCsvFile(String fileName, int n, int key) {
        try (FileWriter writer = new FileWriter(fileName)) {
                for (int i = 0; i < n; i++) {
                    Random random = new Random();
                    int randomNumber = random.nextInt(1000);
                    writer.write(i*key + STRING_DELIMITER + randomNumber + lineSeparator);
                }
            }
        catch (IOException e) {
            throw new IllegalStateException("Can't create CSV!");
        }
    }

    public static List<String> getColumn(int column){
        List<String> result = new ArrayList<>();
        for(List<String> line : data){
            result.add(line.get(column - 1));
        }
        return result;
    }

    //вставить столбец по индексу
    public static void addColumn(List<String> column, int index) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).add(index, column.get(i));
        }
    }

    //вставить столбец в конец файла(списка)
    public static void addColumn(List<String> column) {
        addColumn(column, data.get(0).size());
    }

    //считает количество строк в файле
    public static int linesNumber(String fileName) {
        int number = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            while(br.readLine() != null) {
                number++;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can't count the number");
        }
        return number;
    }

    //читает весь csv-файл(считаем количество строчек, разделяем(String[].split) их и записываем в лист)
    public static List<List<String>> readAll(String fileName) {
        List<List<String>> content = new ArrayList<List<String>>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String[] tokens;
            int lines = linesNumber(fileName);
            for (int i = 0; i < lines; i++) {
                tokens = br.readLine().split(STRING_DELIMITER);
                content.add(new ArrayList<>(Arrays.asList(tokens)));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can't read CSV file");
        }
        return content;
    }

    //записываем строчки из data в csv-файл
    public static void writeToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (List<String> lines : data) {
                String line = "";
                for (int i = 0 ; i < lines.size(); i++) {
                    if (i == lines.size() - 1) {
                        line = line + lines.get(i) + "";
                    }
                    else {
                        line = line + lines.get(i) + STRING_DELIMITER;
                    }
                }
                writer.write(line + lineSeparator);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can't write to this file");
        }
    }
    //расчет скользящего среднего(если попадаем в диапазон period, то считаем среднее; иначе, оставляем пустую строку)
    public static void movingAverage(List<List<String>> input, int period) {
        List<String> listOfAverages = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            if (((i - period) >= 0) && ((i + period) < input.size())) {
                int avg = 0;
                for (int j = i - period; j <= i + period; j++) {
                    avg += Integer.parseInt(input.get(j).get(1));
                }
                avg /= 2 * period + 1;
                listOfAverages.add(i, "" + (double)avg);
            } else {
                listOfAverages.add(i, "");
            }
        }
        addColumn(listOfAverages);
    }

    /*Решил сделать все методы статиками, чтобы не создавать какой-то отдельный экземпляр CSVParser, а то это как-то не очень,
    именно поэтому main запилен прям в классе, чтобы не делать отдельный класс Main и опять же не создавать экземпляр"
     */
    public static void main(String[] args) {
        generateCsvFile("untitled.csv", 100, 3); // создание ста строк с ключом 3
        data = readAll("untitled.csv"); // считываем содержимое файла в List data
        movingAverage(data, 5); // считаем скользящее среднее
        writeToFile("untitled.csv"); // записываем новые значения в файл
    }
}


