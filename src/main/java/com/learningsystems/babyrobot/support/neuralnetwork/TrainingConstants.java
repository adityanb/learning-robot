package com.learningsystems.babyrobot.support.neuralnetwork;

import com.learningsystems.babyrobot.support.util.Normalization;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiFunction;

interface TrainingConstants {
    Integer getInputNodes();

    Integer getHiddenNodes();

    Double momentum();

    Double learnRate();

    String getNameOfTraining();

    InputFileProcessingResult processInputFile();

    default String stringRepresentation() {
        StringJoiner joiner = new StringJoiner("-");
        joiner
                .add(getNameOfTraining())
                .add("IN")
                .add(getInputNodes().toString())
                .add("HN")
                .add(getHiddenNodes().toString())
                .add("MTM")
                .add(momentum().toString())
                .add("LR")
                .add(learnRate().toString())
        ;
        return joiner.toString();
    }

    default BiFunction<Integer, List<Double>[], double[][]> normalization() {
        return (numberOfStates, inputs) -> {
            double[][] doubles;
            String statsFileName = stringRepresentation() + "-stats.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFileName))) {
                doubles = Normalization.meanScaled(numberOfStates, writer, inputs);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return doubles;
        };
    }

    class InputFileProcessingResult {
        private List<Double>[] inputs;
        private int numberOfStates;
        private List<Double>[] outputs;

        InputFileProcessingResult(List<Double>[] inputs, int numberOfStates, List<Double>[] outputs) {
            this.inputs = inputs;
            this.numberOfStates = numberOfStates;
            this.outputs = outputs;
        }

        List<Double>[] getInputs() {
            return inputs;
        }

        int getNumberOfStates() {
            return numberOfStates;
        }

        List<Double>[] getOutputs() {
            return outputs;
        }
    }

}
