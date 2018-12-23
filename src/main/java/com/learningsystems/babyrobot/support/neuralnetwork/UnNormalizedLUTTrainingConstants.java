package com.learningsystems.babyrobot.support.neuralnetwork;

import com.learningsystems.babyrobot.support.util.Normalization;

import java.util.List;
import java.util.function.BiFunction;

public class UnNormalizedLUTTrainingConstants extends EncodedTrainingConstants {

    @Override
    public Double momentum() {
        return 0.001;
    }

    @Override
    public Double learnRate() {
        return 0.00001;
    }

    @Override
    public String getNameOfTraining() {
        return "EncodedLUT-UnNormalized";
    }

    @Override
    public BiFunction<Integer, List<Double>[], double[][]> normalization() {
        return (numberOfStates, inputs) -> Normalization.unNormalized(numberOfStates, inputs);
    }

}
