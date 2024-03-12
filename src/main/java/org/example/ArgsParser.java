package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ArgsParser {
    void processArgs(String[] args) {
        FileParser fileParser = new FileParser();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {//аргументы для работы с таблицей значений функции
                case "-table", "-t" -> {
                    List<String> lines = fileParser.readFileLines(args[++i]);
                    if (lines.size() == 2) {
                        int[] funcArgs = fileParser.getArray(lines.get(0));
                        int[] funcValues = fileParser.getArray(lines.get(1));
                        LagComputer lagComputer = new LagComputer(funcArgs, funcValues);
                        //TODO добавить sout
                        Scanner s = new Scanner(System.in);
                        var input = s.nextDouble();
                        double y = lagComputer.computeValueAtPoint(input);
                        System.out.println(lagComputer.getInterpolationPolynomial());
                        System.out.println(y);
                    } else {
                        System.out.println("Ошибка при вводе файла таблицы");
                    }
                }//аргументы командной строки для работы с аналитически заданной функцией
                case "-func", "-f" -> {
                    Scanner s = new Scanner(System.in);
                    System.out.println("Введите адрес файла с массивом значений аргумента");
                    String filePath = s.next();
                    int[] funcArgs = new int[0];
                    try {
                        //Достаем из файла строки, подразумевается, что пользователь передаст корректный файл
                        // после чего из первой строки достаем массив аргументов
                        funcArgs = fileParser.getArray(fileParser.readFileLines(filePath).get(0));
                    } catch (Exception e) {
                        //вывод информации о возможной ошибке
                        System.out.println(e.getMessage());
                    }
                    double[] funcValues = new double[funcArgs.length];
                    System.out.println("""
                            Выберите функцию
                            1: 3x^2 - 2x + 1
                            2: sin(2x)
                            3: e^x
                            4: sqrt(3x + 4)
                            """);
                    int chosenFunc = s.nextInt();
                    switch (chosenFunc) {
                        case 1:
                            calcFirstFunc(funcArgs, funcValues);
                            break;
                        case 2:
                            calcSecondFunc(funcArgs, funcValues);
                            break;
                        case 3:
                            calcThirdFunc(funcArgs, funcValues);
                            break;
                        case 4:
                            calcFourthFunc(funcArgs, funcValues);
                            break;
                    }
                    System.out.println(Arrays.stream(funcValues).boxed().toList());
                    int[] integerValues = String.valueOf(funcValues).replace("\\D", "").chars().map(Character::getNumericValue).toArray();
                    //перевод массива в целочисленный тип с использованием клсса Stream
                    //
                    LagComputer lagComputer = new LagComputer(funcArgs, integerValues);
                    System.out.println(lagComputer.getInterpolationPolynomial());
                }
            }
        }
    }

    private void calcFourthFunc(int[] funcArgs, double[] funcValues) {
        for (int i = 0; i < funcArgs.length; i++) {
            funcValues[i] = Math.sqrt(3 * funcArgs[i] + 4);
        }
    }

    private void calcThirdFunc(int[] funcArgs, double[] funcValues) {
        for (int i = 0; i < funcArgs.length; i++) {
            funcValues[i] = Math.exp(funcArgs[i]);
        }
    }

    private void calcSecondFunc(int[] funcArgs, double[] funcValues) {
        for (int i = 0; i < funcArgs.length; i++) {
            funcValues[i] = Math.sin(2 * funcArgs[i]);
        }
    }

    private void calcFirstFunc(int[] funcArgs, double[] funcValues) {
        for (int i = 0; i < funcArgs.length; i++) {
            funcValues[i] = 3 * funcArgs[i] * funcArgs[i] - 2 * funcArgs[i] + 1;
        }
    }


}
