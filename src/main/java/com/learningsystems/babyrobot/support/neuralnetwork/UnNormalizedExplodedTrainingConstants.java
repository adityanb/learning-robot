package com.learningsystems.babyrobot.support.neuralnetwork;

import com.learningsystems.babyrobot.support.util.Normalization;

import java.util.List;
import java.util.function.BiFunction;

public class UnNormalizedExplodedTrainingConstants extends ExplodedTrainingConstants {

    @Override
    public Double momentum() {
        return super.momentum();
    }

    @Override
    public Double learnRate() {
        return 0.0001;
    }

    @Override
    public String getNameOfTraining() {
        return "ExplodedLUT-UnNormalized";
    }

    @Override
    public BiFunction<Integer, List<Double>[], double[][]> normalization() {
        return (numberOfStates, inputs) -> Normalization.unNormalized(numberOfStates, inputs);
    }

}
