package com.learningsystems.babyrobot.support.predictor;

import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.util.Constants;

import java.io.File;

public class NeuralNetworkPredictor implements Predictor {
    @Override
    public Pair<Constants.ACTION, Double> getBestAction(int state) {
        return null;
    }

    @Override
    public double getQValue(int state, Constants.ACTION action) {
        return 0;
    }

    @Override
    public void learn(int state, Constants.ACTION action, double qValue) {

    }

    @Override
    public void save(File dataFile) {

    }
}
