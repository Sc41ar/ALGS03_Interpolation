package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class ArgsParser {
    void processArgs(String[] args) {
        FileParser fileParser = new FileParser();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {//аргументы для работы с таблицей значений функции
                case "-table", "-t" -> {
                    List<String> lines = fileParser.readFileLines(args[++i]);
                    if (lines.size() == 2) {
                        double[] funcArgs = fileParser.getArray(lines.get(0));
                        double[] funcValues = fileParser.getArray(lines.get(1));
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
                    double[] funcArgs = new double[0];
                    try {
                        //Достаем из файла строки, подразумевается, что пользователь передаст корректный файл
                        // после чего из первой строки достаем массив аргументов
                        funcArgs = fileParser.getArray(fileParser.readFileLines(filePath).get(0));
                    } catch (Exception e) {
                        //вывод информации о возможной ошибке
                        System.out.println(e.getMessage());
                    }
                    //создается массив для значение аналогичной с массивом аргументов длины
                    double[] funcValues = new double[funcArgs.length];
                    System.out.println("""
                            Выберите функцию
                            1: 3x^2 - 2x + 1
                            2: sin(x)
                            3: e^x
                            4: sqrt(3x + 4)
                            5: abs(x)
                            6: e^-x
                            """);
                    int chosenFunc = s.nextInt();
                    Function<Double, Double> cFunc;
                    //в переменную записывается лямбда выражение, в зависимости от того, что выбирает пользователь
                    //потом используя эту переменную вычисляются значения функции в узлах интерполяции
                    switch (chosenFunc) {
                        case 1 -> {
                            cFunc = (x) -> (double) ((3 * x * x) - (x * 2) + 1);
                        }
                        case 2 -> {
                            cFunc = (x) -> Math.sin(x);
                        }
                        case 3 -> {
                            cFunc = (x) -> Math.exp(x);
                        }
                        case 4 -> {
                            cFunc = (x) -> Math.sqrt(3 * x + 4);
                        }
                        case 5 -> {
                            cFunc = (x) -> (double) Math.abs(x);
                        }
                        case 6 -> {
                            cFunc = (x) -> Math.exp(-x);
                        }
                        default -> {
                            cFunc = (x) -> (double) x;
                        }

                    }
                    //парметры метода передаются по ссылке
                    //поэтому funcValues изменится
                    calcFuncValues(funcArgs, funcValues, cFunc);
                    System.out.println(Arrays.toString(funcValues));
                    //                    System.out.println(Arrays.stream(funcValues).boxed().toList());//массив переводится в поток, после чего конверируется в список, чтобы иметь возможность вывести в 1 строчку
                    LagComputer lagComputer = new LagComputer(funcArgs, funcValues);
                    System.out.println(lagComputer.getInterpolationPolynomial());

                    var valuesclone = funcValues.clone();
                    //TODO tests
                    for (int j = 0; j < funcArgs.length; j++) {
                        List<Double> a = new ArrayList<>(Arrays.stream(funcArgs).boxed().toList());
                        a.remove(j);
                        Double[] arrayTempCopy = a.toArray(new Double[0]);
                        lagComputer.setArgs(arrayTempCopy);
                        List<Double> b = new ArrayList<>(Arrays.stream(funcValues).boxed().toList());
                        b.remove(j);
                        Double[] arrayCopy = b.toArray(b.toArray(new Double[0]));
                        lagComputer.setValues(arrayCopy);
                        valuesclone[j] = lagComputer.computeValueAtPoint(funcArgs[j]);
                    }
                    System.out.println(Arrays.toString(valuesclone));
                }
            }
        }
    }

    private void calcFuncValues(double[] funcArgs, double[] funcValues, Function<Double, Double> computingFunc) {
        for (int i = 0; i < funcArgs.length; i++) {
            funcValues[i] = computingFunc.apply(funcArgs[i]);
        }
    }

}
