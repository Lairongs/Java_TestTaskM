
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
public class Main{
    public static void main(String[] args) {

        // Параметры для учета дополнительных функций:
        boolean append = false;             // Параметр: "Существует ли файл для перезаписи"
        boolean detailedStats = false;      // Параметр: Полная статистика
        boolean shortStats = false;         // Параметр: Краткая статистика
        String outputDir = "";             // Параметр: Указание директории
        String prefix = "";                 // Параметр: Содержит дополнительный префикс

        ArrayList<String> checkFiles = new ArrayList<String>(); // Сохраняем информацию о расположении анализируемых файлов

        ArrayList<Long> longList = new ArrayList<Long>();
        ArrayList<Double> floatsList = new ArrayList<Double>();
        ArrayList<String> stringsList = new ArrayList<String>();


        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-a":
                    append = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    detailedStats = true;
                    break;
                case "-o":
                    outputDir = args[++i];
                    break;
                case "-p":
                    prefix = args[++i];
                    break;
                default:
                    checkFiles.add(args[i]);
                    break;
            }
        }

        for(int i = 0; i < checkFiles.size(); i++){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(checkFiles.get(i)));
                String line = reader.readLine().trim();

                while(line != null) {
                    line = line.trim();
                    try{
                        longList.add(Long.parseLong(line)); // Попытка преобразовать String в int
                    }
                    catch (NumberFormatException exception_1) // В случае неудачи, пробуем преобразовать в тип double
                    {
                        try{
                            floatsList.add(Double.parseDouble((line)));
                        }
                        catch (Exception exception_2)
                        {
                            stringsList.add(line); // В случае, если преобразовать стркои не удается, оставляем ее строкой
                        }
                    }
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + checkFiles.get(i));
                e.printStackTrace();
            }
        }

        // Записываем информацию в файлы
        writeToFile(outputDir, prefix, append, "integers.txt", longList);
        writeToFile(outputDir, prefix, append, "floats.txt", floatsList);
        writeToFile(outputDir, prefix, append, "strings.txt", stringsList);


        if(detailedStats) detailStaticInformation(longList,floatsList,stringsList);
        if(shortStats){
            if(!detailedStats) shortStaticInformation(longList,floatsList,stringsList);
        }
        System.out.println(checkFiles.size());
        System.out.println("Адрес = " + outputDir);

    }

    private static void detailStaticInformation(ArrayList<Long> longList, ArrayList<Double> floatsList, ArrayList<String> stringsList)
    {
        System.out.println("Полная статистика: \n");
        //========================= Целые числа =========================
        System.out.println("============================= Целые числа ============================");
        System.out.println("Количество элементов: " + longList.size());
        if(!longList.isEmpty()){
            long integerSum = 0;
            for (int i = 0; i < longList.size(); i++)
            {
                integerSum += longList.get(i);
            }
            System.out.println("Минимальное значение: " + Collections.min(longList));
            System.out.println("Максимальное значение: " + Collections.max(longList));
            System.out.println("Сумма элементов: " + integerSum);
            System.out.println("Среднее значение элементов: " + integerSum/longList.size());
        }

        //========================= Вещественные числа =========================
        System.out.println("\n========================= Вещественные числа =========================");
        System.out.println("Количество элементов: " + floatsList.size());
        if(!floatsList.isEmpty()){
            double floatSum = 0;
            for (int i = 0; i < floatsList.size(); i++)
            {
                floatSum += floatsList.get(i);
            }
            System.out.println("Минимальное значение: " + Collections.min(floatsList));
            System.out.println("Максимальное значение: " + Collections.max(floatsList));
            System.out.println("Сумма элементов: " + floatSum);
            System.out.println("Среднее значение элементов: " + floatSum/floatsList.size());
        }
        //=============================== Строки ===============================
        System.out.println("\n=============================== Строки ===============================");
        System.out.println("Количество элементов: " + stringsList.size());
        if(!stringsList.isEmpty()) {

            int minLength = stringsList.getFirst().length();
            int maxLength = minLength;
            int length;
            for(int i = 1; i <stringsList.size(); i++) {
                length = stringsList.get(i).length();
                if(maxLength < length){
                    maxLength = length;
                }
                if(minLength>length){
                    minLength = length;
                }
            }

            System.out.println("Длина самой короткой строки: " + minLength);
            System.out.println("Длина самой длинной строки: " + maxLength);
        }
    }

    private static void shortStaticInformation(ArrayList<Long> longList, ArrayList<Double> floatsList, ArrayList<String> stringsList){
        System.out.println("============================= Целые числа ============================");
        System.out.println("Количество элементов: " + longList.size());
        System.out.println("\n========================= Вещественные числа =========================");
        System.out.println("Количество элементов: " + floatsList.size());
        System.out.println("\n=============================== Строки ===============================");
        System.out.println("Количество элементов: " + stringsList.size());
    }

    private static void writeToFile(String outputDir, String prefix, boolean append, String fileName, List<?> dataList){
        if(dataList.isEmpty()){
            return;
        }
        File fileDirectory;
        File outputFile;
        if(outputDir.isEmpty()) {
            URL jarLocation = Main.class.getProtectionDomain().getCodeSource().getLocation();
            String fullPath = new File(jarLocation.getPath()).getAbsolutePath();

            // Получаем директорию, где находится JAR-файл
            fileDirectory = new File(fullPath).getParentFile();
            outputFile = new File(fileDirectory, prefix + fileName);

        }
        else {
            String fullPath = Paths.get(outputDir, prefix + fileName).toString();
            outputFile = new File(fullPath);
        }

        try {
            FileWriter writer = new FileWriter(outputFile, append);
            for (int i = 0; i < dataList.size(); i++){
                writer.write(dataList.get(i).toString());
                writer.append('\n');
            }
            writer.flush();
        }
        catch(IOException ex){
            System.err.println("Ошибка при записи в файл: " + outputDir + prefix + fileName);
            ex.printStackTrace();
        }
    }


}