package org.example;

public class LagComputer {
    private int[] args;
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
            for (int j = 0; j < args.length; j++) {
                if (j != i) {
                    numerator *= (x - args[j]);
                    denominator *= (args[i] - args[j]);
                }
            }
            sum += (numerator / denominator) * values[i];
        }
        return sum;
    }
}
