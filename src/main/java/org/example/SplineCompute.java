package org.example;

import java.util.ArrayList;

public class SplineCompute {

    private double[] x;
    private double[] y;
    private double[] h;
    private double[] a;
    private double[] b;
    private double[] c;
    private double[] d;

    public SplineCompute(double[] args, double[] values) {
        this.x = args;
        this.y = values;
        int n = x.length;
        h = new double[n - 1];
        a = new double[n];
        b = new double[n - 1];
        c = new double[n - 1];
        d = new double[n - 1];

        // Вычисляем шаги h
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }



        // Вычисляем коэффициенты a
        a = y.clone();
        // Вычисляем коэффициенты b, c и d
        double[] cRightPart = new double[n - 1];
        for (int i = 1; i < n - 1; i++) {
            cRightPart[i] = 3.0 * ((a[i + 1] - a[i]) / h[i] - (a[i] - a[i - 1]));
        }
        //задаем значения для трехдиагональной матрицы
        double[] diag = new double[n - 1];
        double[] lowerDiag = new double[n - 1];
        double[] higherDiag = new double[n - 1];
        for (int i = 1; i < n; i++) {
            diag[i] = 2 * (h[i] + h[i + 1]);
            lowerDiag[i] = h[i];
            higherDiag[i] = h[i + 1];
        }
        getProgCoef(cRightPart, diag, lowerDiag, higherDiag);
        c[n - 2] = 0.0;
        higherDiag[n - 2] = 0.0;
        for (int j = n - 2; j >= 0; j--) {
            c[j] = h[j] - lowerDiag[j] * c[j + 1];
            b[j] = (a[j + 1] - a[j]) / h[j] - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }

    }

    //получение коэффициентов прогонки
    private void getProgCoef(double[] alpha, double[] l, double[] mu, double[] z) {
        int n = alpha.length;
        z[n - 1] = (alpha[n - 1] - h[n - 2] * z[n - 2]) / (2.0 * (x[n - 1] - x[n - 2]));
        for (int j = n - 2; j >= 0; j--) {
            z[j] = z[j] - mu[j] * z[j + 1];
        }
    }
}
