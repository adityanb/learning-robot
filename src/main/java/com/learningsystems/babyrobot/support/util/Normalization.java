package com.learningsystems.babyrobot.support.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class Normalization {

    // Values are between 0 and 1
   /* public static double[][] minMaxScaled(int numberOfStates, List<Double>... inputs) {
        List<Double>[] scaledInputs = new List[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            List<Double> scaled = minMaxScale(inputs[i]);
            getStats(scaled);
            scaledInputs[i] = scaled;
        }
        return combineToArray(numberOfStates, scaledInputs);
    }*/

    public static double[][] unNormalized(int numberOfStates, List<Double>... inputs) {
        return combineToArray(numberOfStates, inputs);
    }

    // Values between -1 and 1
    public static double[][] meanScaled(int numberOfStates, BufferedWriter writer, List<Double>... inputs) throws IOException {
        List<Double>[] scaledInputs = new List[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            List<Double> scaled = meanScale(inputs[i]);
            scaledInputs[i] = scaled;
            String stats = getStats(inputs[i]).toString();
            writer.write(stats + "\n");
        }
        return combineToArray(numberOfStates, scaledInputs);
    }

    public static void main(String[] args) {
        List<Double> l1 = new ArrayList<>();
        l1.add(-.23);
        l1.add(11d);
        l1.add(0.34);
        l1.add(-45d);
        l1.add(-999d);

        List<Double> l2 = new ArrayList<>();
        l2.add(-0.3);
        l2.add(0d);
        l2.add(34d);
        l2.add(-45d);
        l2.add(0.55);
    }

    private static Stat getStats(List<Double> scaled) {
        double average = scaled.stream().mapToDouble(value -> value).average().orElseThrow(() -> new RuntimeException("No average!"));
        return new Stat(average, Collections.max(scaled), Collections.min(scaled));
    }

    private static double[][] combineToArray(Integer numberOfStates, List<Double>... inputs) {
        double[][] inputsToNeuralNetwork = new double[numberOfStates][inputs.length];
        for (int i = 0; i < numberOfStates; i++) {
            for (int j = 0; j < inputs.length; j++) {
                inputsToNeuralNetwork[i][j] = inputs[j].get(i);
            }
        }
        return inputsToNeuralNetwork;
    }

    private static List<Double> minMaxScale(List<Double> states) {
        Double max = Collections.max(states);
        Double min = Collections.min(states);
        return states.stream().map(x -> (x - min) / (max - min)).collect(Collectors.toList());
    }

    private static List<Double> meanScale(List<Double> states) {
        Double max = Collections.max(states);
        Double min = Collections.min(states);
        double average = states.stream().mapToDouble(value -> value).average().orElseThrow(() -> new RuntimeException("No average!"));
        return states.stream().map(x -> (x - average) / (max - min)).collect(Collectors.toList());
    }

    public static class Stat {
        double average;
        double max;
        double min;
        private final String representation;

        public Stat(double average, double max, double min) {
            this.average = average;
            this.max = max;
            this.min = min;
            StringJoiner stringJoiner = new StringJoiner(",");
            representation = stringJoiner
                    .add(String.valueOf(average))
                    .add(String.valueOf(max))
                    .add(String.valueOf(min))
                    .toString();
        }

        public static List<Stat> load(String statsFile) {
            List<Stat> allStats = new ArrayList<>();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(statsFile))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split(",");
                    allStats.add(new Stat(Double.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2])));
                }
                return allStats;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public double getAverage() {
            return average;
        }

        public double getMax() {
            return max;
        }

        public double getMin() {
            return min;
        }

        @Override
        public String toString() {
            return representation;
        }
    }
}
