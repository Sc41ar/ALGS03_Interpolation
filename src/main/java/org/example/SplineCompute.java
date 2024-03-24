package org.example;

import java.util.Arrays;

public class SplineCompute {

    // Массив коэффициентов
    static double[][] coef;
    // Массив b
    double[] a;
    double[] b;
    double[] c;
    double[] d;

    private double[] x;
    private double[] y;
    // Количество узлов
    private int n;

    public SplineCompute(double[] args, double[] values) {
        n = args.length;
        this.x = args;
        this.y = values;
        // Инициализация переменных
        a = new double[n ];
        c = new double[n ];
        d = new double[n ];
        double[] delta = new double[n - 1];
        double[] h = new double[n - 1];
        double[][] triDiagMatrix = new double[3][n];
        double[] f = new double[n];
        double x3, xn;
        int i;

        // Вычисление начальных значений
        x3 = x[2] - x[0];
        xn = x[n - 1] - x[n - 3];

        // Цикл вычисления параметров сплайна
        for (i = 0; i < n - 1; i++) {
            a[i] = y[i];
            h[i] = x[i + 1] - x[i];
            delta[i] = (y[i + 1] - y[i]) / h[i];
            triDiagMatrix[0][i] = i > 0 ? h[i] : x3;
            f[i] = i > 0 ? 3 * (h[i] * delta[i - 1] + h[i - 1] * delta[i]) : 0;
        }

        // Вычисление коэффициентов трехдиагональной матрицы
        triDiagMatrix[1][0] = h[0];
        triDiagMatrix[2][0] = h[0];
        for (int j = 1; j < n - 1; j++) {
            triDiagMatrix[1][j] = 2 * (h[j] + h[j - 1]);
            triDiagMatrix[2][j] = h[j];
        }
        triDiagMatrix[1][n - 1] = h[n - 2];
        triDiagMatrix[2][n - 1] = xn;
        triDiagMatrix[0][n - 1] = h[n - 2];

        // Вычисление правой части системы линейных уравнений
        i = n - 1;
        f[0] = ((h[0] + 2 * x3) * h[1] * delta[0] + Math.pow(h[0], 2) * delta[1]) / x3;
        f[n - 1] = (Math.pow(h[i - 1], 2) * delta[i - 2] + (2 * xn + h[i - 1]) * h[i - 2] * delta[i - 1]) / xn;

        // Решение системы линейных уравнений
        b = new double[n];
        solveTriDiag(triDiagMatrix, f);

        // Вычисление коэффициентов c и d
        for (i = 0; i < n - 1; i++) {
            d[i] = (b[i + 1] - b[i]) / (3 * h[i]);
            c[i] = (b[i + 1] - b[i]) / h[i] - h[i] * (d[i + 1] + 2 * d[i]) / 3;
        }

        // Создание массива коэффициентов
        coef = new double[n][4];
        for (i = 0; i < n - 1; i++) {
            coef[i][0] = a[i];
            coef[i][1] = b[i];
            coef[i][2] = c[i];
            coef[i][3] = d[i];
        }
    }




    public void setX(Double[] x) {
        this.x = Arrays.stream(x).mapToDouble(Double::doubleValue).toArray();
    }

    public void setY(Double[] y) {
        this.y = Arrays.stream(y).mapToDouble(Double::doubleValue).toArray();
    }

    void solveTriDiag(double[][] TDM, double[] F) {
        double[] alph = new double[n - 1];
        double[] beta = new double[n - 1];

        int i;

        // Алгоритм прогонки
        alph[0] = -TDM[2][0] / TDM[1][0];
        beta[0] = F[0] / TDM[1][0];

        for (i = 1; i < n - 1; i++) {
            alph[i] = -TDM[2][i] / (TDM[1][i] + TDM[0][i] * alph[i - 1]);
            beta[i] = (F[i] - TDM[0][i] * beta[i - 1]) / (TDM[1][i] + TDM[0][i] * alph[i - 1]);
        }
        b[n - 1] = (F[n - 1] - TDM[0][n - 1] * beta[n - 2]) / (TDM[1][n - 1] + TDM[0][n - 1] * alph[n - 2]);

        // Обход в обратном направлении
        for (i = n - 2; i > -1; i--) {
            b[i] = b[i + 1] * alph[i] + beta[i];
        }
    }

    public void printPolynomials() {
        for (int i = 1; i < n; i++) {
            System.out.println("Полином " + i + " интервала [" + x[i - 1] + ", " + x[i] + "]:");
            System.out.println("f(x) = " + a[i - 1] + " + " + b[i - 1] + " * (x - " + x[i - 1] + ") + " + c[i - 1] + " * (x - " + x[i - 1] + ")^2 + " + d[i - 1] + " * (x - " + x[i - 1] + ")^3");
        }
    }


}
