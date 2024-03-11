package org.example;

import java.util.List;

public class ArgsParser {
    void processArgs(String[] args){


        for (int i = 0; i < args.length; i++) {
            switch (args[i]){//аргументы для работы с таблицей значений функции
                case "-table", "-t" -> {
                    List<String> lines = fileParser.readFileLines(args[++i]);
                    if (lines.size() == 2) {
                        int[] funcArgs = fileParser.getArray(lines.get(0));
                        int[] funcValues = fileParser.getArray(lines.get(1));
                    }else {
                        System.out.println("Ошибка при вводе файла таблицы");
                    }
                }
            }
        }
    }
}
