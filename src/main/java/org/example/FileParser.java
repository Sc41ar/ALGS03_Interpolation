package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    public List<String> readFileLines(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader reader = new BufferedReader(fr);
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            System.out.println(e.getMessage() + "\nНеккоректный адрес входного файла");
            return null;
        }
    }

    public double[] getArray(String s) {
        String[] words = s.split("[ ,]");
        double[] resultArray = new double[words.length];
        for (int i = 0; i < words.length; i++) {
            try {
                resultArray[i] = Double.parseDouble(words[i]);
            }catch (Exception e){
                System.out.println(e.getMessage());
                resultArray[i] = 0;
            }
        }
        return resultArray;
    }
}
