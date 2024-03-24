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
    private int n;

    public SplineCompute(double[] args, double[] values) {
        this.x = args;
        this.y = values;
        n = x.length;
        h = new double[n];
        a = new double[n];
        b = new double[n - 1];
        c = new double[n - 1];
        d = new double[n - 1];

        // Вычисляем шаги h
        for (int i = 1; i < n; i++) {
            h[i - 1] = x[i] - x[i - 1];
        }

        // Вычисляем коэффициенты a
        a = y.clone();

        double[] cRightPart = new double[n - 1];
        for (int i = 1; i < n - 1; i++) {
            cRightPart[i] = 3.0 * ((a[i + 1] - a[i]) / h[i] - (a[i] - a[i - 1]) / h[i - 1]);
        }
        //задаем значения для трехдиагональной матрицы
        double[] mD = new double[n - 1];
        double[] lD = new double[n - 1];
        double[] uD = new double[n - 1];
        //матрица смещена из-за того что с0 = 0
        for (int i = 0; i < n - 1; i++) {
            mD[i] = 2 * (h[i] + h[i + 1]);
            if (i > 0) {
                lD[i] = h[i];
            }
            if (i < n - 2) {
                uD[i] = h[i + 1];
            }
        }
        c = solveTridiagonalSystem(mD, lD, uD, cRightPart);
        for (int i = n - 2; i > 0; i--) {
            b[i] = (a[i] - a[i - 1]) / h[i] - h[i] * (c[i] + 2.0 * c[i - 1]) / 3.0;
            d[i] = (c[i] - c[i - 1]) / (3.0 * h[i]);
        }


    }

    private double[] solveTridiagonalSystem(double[] mainDiag, double[] lowerDiag, double[] upperDiag, double[] rightPart) {
        double[] alph = new double[n - 1];
        double[] beta = new double[n - 1];
        double[] x = new double[n - 1];
        int i;

        alph[0] = -upperDiag[0] / mainDiag[0];
        beta[0] = rightPart[0] / mainDiag[0];

        for (i = 1; i < n - 1; i++) {
            alph[i] = -upperDiag[i] / (mainDiag[i] + lowerDiag[i] * alph[i - 1]);
            beta[i] = (rightPart[i] - lowerDiag[i] * beta[i - 1]) / (mainDiag[i] + lowerDiag[i] * alph[i - 1]);
        }
        x[n - 2] = (rightPart[n - 2] - lowerDiag[n - 2] * beta[n - 2]) / (mainDiag[n - 2] + lowerDiag[n - 2] * alph[n - 2]);

        for (i = n - 3; i > -1; i--) {
            x[i] = x[i + 1] * alph[i] + beta[i];
        }
        return x;
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

    public void printPolynomials() {
        for (int i = 0; i < n - 1; i++) {
            System.out.println("Polynomial " + i + " for interval [" + x[i] + ", " + x[i + 1] + "]:");
            System.out.println("f(x) = " + a[i] + " + " + b[i] + " * (x - " + x[i] + ") + " + c[i] + " * (x - " + x[i] + ")^2 + " + d[i] + " * (x - " + x[i] + ")^3");
        }
    }


}
