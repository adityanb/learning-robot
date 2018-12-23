package com.learningsystems.babyrobot.support.neuralnetwork;

import java.io.File;
import java.util.List;
import java.util.StringJoiner;

interface TrainingConstants {
    Integer getInputNodes();

    Integer getHiddenNodes();

    Double momentum();

    Double learnRate();

    File getInputFile();

    InputFileProcessingResult processInputFile();

    default String stringRepresentation() {
        StringJoiner joiner = new StringJoiner("-");
        joiner
                .add(getInputNodes().toString())
                .add(getHiddenNodes().toString())
                .add(momentum().toString())
                .add(learnRate().toString())
                .add(getInputFile().getName())
        ;
        return joiner.toString();
    }

    class InputFileProcessingResult {
        private List<Double>[] inputs;
        private int numberOfStates;
        private List<Double>[] outputs;

        public InputFileProcessingResult(List<Double>[] inputs, int numberOfStates, List<Double>[] outputs) {
            this.inputs = inputs;
            this.numberOfStates = numberOfStates;
            this.outputs = outputs;
        }

        public List<Double>[] getInputs() {
            return inputs;
        }

        public int getNumberOfStates() {
            return numberOfStates;
        }

        public List<Double>[] getOutputs() {
            return outputs;
        }
    }

}
