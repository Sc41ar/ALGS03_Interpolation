package org.example;

import java.util.Arrays;

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
        b = new double[n];
        c = new double[n];
        d = new double[n];

        // Вычисляем шаги h
        for (int i = 1; i < n; i++) {
            h[i-1] = x[i] - x[i - 1];
        }

        // Вычисляем коэффициенты a
        a = y.clone();

        double[] cRightPart = new double[n - 1];
        for (int i = 1; i < n - 1; i++) {
            cRightPart[i] = 3.0 * ((a[i + 1] - a[i]) / h[i] - (a[i] - a[i - 1]) / h[i - 1]);
        }
        //задаем значения для трехдиагональной матрицы
        double[] l = new double[n - 1];
        double[] mu = new double[n - 1];
        double[] z = new double[n - 1];
        solveTridiagonalSystem(cRightPart, l, mu, z);
        c[n - 2] = 0.0;
        z[n - 2] = 0.0;
        for (int j = n - 2; j >= 0; j--) {
            c[j] = z[j] - mu[j] * c[j + 1];
            b[j] = (a[j + 1] - a[j]) / h[j] - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }

    }

    private void solveTridiagonalSystem(double[] alpha, double[] l, double[] mu, double[] z) {
        int n = alpha.length;
        l[0] = 1.0;
        mu[0] = 0.0;
        z[0] = 0.0;
        for (int i = 1; i < n; i++) {
            l[i] = 2.0 * (x[i + 1] - x[i - 1]) - h[i - 1] * mu[i - 1];
            mu[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }
        z[n - 1] = (alpha[n - 1] - h[n - 2] * z[n - 2]) / (2.0 * (x[n - 1] - x[n - 2]));
        for (int j = n - 2; j >= 0; j--) {
            z[j] = z[j] - mu[j] * z[j + 1];
        }
    }

    public double interpolate(double xi) {
        int n = x.length;
        int k = Arrays.binarySearch(x, xi);
        if (k < 0) {
            k = -(k + 1);
        }
        k = Math.min(k, n - 2);
        double hk = x[k + 1] - x[k];
        double t = (xi - x[k]) / hk;
        return a[k] + b[k] * t + c[k] * t * t + d[k] * t * t * t;
    }
}
