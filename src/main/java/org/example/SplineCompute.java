package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
        b = new double[n];
        c = new double[n];
        d = new double[n] ;

        a = y.clone();

        // Вычисляем шаги h
        for (int i = 1; i < n; i++) {
            h[i - 1] = x[i] - x[i - 1];
        }

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
        solveTridiagonalSystem(mD, lD, uD, cRightPart);
        c[0] = c[n-1] = 0;
        for (int i = n - 2; i > 0; i--) {
            b[i] = (a[i] - a[i - 1]) / h[i] - h[i] * (c[i] + 2.0 * c[i - 1]) / 3.0;
            d[i] = (c[i] - c[i - 1]) / (3.0 * h[i]);
        }
        b[0] = (a[1] - a[0]) / h[0] - h[0] * (c[1] + 2.0 * c[0]) / 3.0;
        d[0] = (c[1] - c[0]) / (3.0 * h[0]);
    }

    public void setX(Double[] x) {
        this.x = Arrays.stream(x).mapToDouble(Double::doubleValue).toArray();
    }

    public void setY(Double[] y) {
        this.y = Arrays.stream(y).mapToDouble(Double::doubleValue).toArray();
    }

    private void solveTridiagonalSystem(double[] mainDiag, double[] lowerDiag, double[] upperDiag, double[] rightPart) {
        double[] alph = new double[n];
        double[] beta = new double[n];
        int i;

        alph[0] = -upperDiag[0] / mainDiag[0];
        beta[0] = rightPart[0] / mainDiag[0];

        for (i = 1; i < n - 1; i++) {
            alph[i] = -upperDiag[i] / (mainDiag[i] + lowerDiag[i] * alph[i - 1]);
            beta[i] = (rightPart[i] - lowerDiag[i] * beta[i - 1]) / (mainDiag[i] + lowerDiag[i] * alph[i - 1]);
        }
        c[n - 2] = (rightPart[n - 2] - lowerDiag[n - 2] * beta[n - 2]) / (mainDiag[n - 2] + lowerDiag[n - 2] * alph[n - 2]);

        for (i = n - 3; i > -1; i--) {
            c[i] = c[i + 1] * alph[i] + beta[i];
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

    public void printPolynomials() {
        for (int i = 1; i < n; i++) {
            System.out.println("Полином " + i + " интервала [" + x[i - 1] + ", " + x[i] + "]:");
            System.out.println("f(x) = " + a[i - 1] + " + " + b[i - 1] + " * (x - " + x[i - 1] + ") + " + c[i - 1] + " * (x - " + x[i - 1] + ")^2 + " + d[i - 1] + " * (x - " + x[i - 1] + ")^3");
        }

        createPlot();
    }

    public void createPlot() {
        XYSeries originalData = new XYSeries("Функция");
        for (int i = 0; i < x.length; i++) {
            originalData.add(x[i], y[i]);
        }

        XYSeries interpolatedData = new XYSeries("Сплайн");
        double step = (x[x.length - 1] - x[0]) / 100.0;
        for (double i = x[0]; i <= x[x.length - 1]; i += step) {
            interpolatedData.add(i, interpolate(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(originalData);
        dataset.addSeries(interpolatedData);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Интерполяция кубическими сплайнами",
                "x",
                "y",
                dataset
        );

        ApplicationFrame frame = new ApplicationFrame("Интерполяция кубическими сплайнами");
        frame.setContentPane(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }


}
