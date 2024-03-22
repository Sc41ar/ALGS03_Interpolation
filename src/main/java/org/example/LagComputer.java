package org.example;

public class LagComputer {
    private int[] args;

    public void setArgs(int[] args) {
        this.args = args;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    private int[] values;


    public LagComputer(int[] args, int[] values) {
        this.args = args;
        this.values = values;
    }

    //интерполяция значения
    public double computeValueAtPoint(double x) {
        double sum = 0;
        for (int i = 0; i < args.length; i++) {
            double numerator = 1, denominator = 1;
            for (int j = 0; j < args.length; j++) { // перебираем все аргументы, кроме текущего
                if (j != i) {
                    numerator *= (x - args[j]); // по формуле
                    denominator *= (args[i] - args[j]);
                }
            }
            sum += (numerator / denominator) * values[i]; // добавляем к сумме значение интерполяционного многочлена в точке x
        }
        return sum; // возвращаем значение интерполяционного многочлена в точке x
    }


    public String getInterpolationPolynomial() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            double coefficient = 1;
            //вычисляем числитель
            for (int j = 0; j < args.length; j++) {
                if (j != i) {
                    coefficient *= (args[i] - args[j]);
                }
            }
            //делим значение функции на числитель
            coefficient = values[i] / coefficient;
            String term = String.format("%.2f", coefficient);
            //если первое слогаемое - не добавляем плюс
            if (i == 0) {
                sb.append(term);
            } else {
                sb.append(" + ");
                if (Math.abs(coefficient) != 1) {
                    sb.append(term);
                }
            }
            //добавляем к строке неизвестное
            for (int j = 0; j < args.length; j++) {
                if (j != i) {
                    sb.append(" * (x - ");
                    sb.append(String.valueOf(args[j]));
                    sb.append(")");
                }
            }
        }
        return sb.toString();
    }

    public void setArgs(Integer[] arrayTempCopy) {
        args = new int[arrayTempCopy.length];
        for (int i = 0; i < arrayTempCopy.length; i++) {
            args[i] = arrayTempCopy[i];
        }
    }

    public void setValues(Double[] arrayTempCopy) {
        values = new int[arrayTempCopy.length];
        for (int i = 0; i < arrayTempCopy.length; i++) {
            values[i] = (int)(Math.ceil(arrayTempCopy[i]*1000)/1000);
        }
    }
}
