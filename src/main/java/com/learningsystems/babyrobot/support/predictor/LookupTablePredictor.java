package com.learningsystems.babyrobot.support.predictor;

import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.model.QuantizedState;
import com.learningsystems.babyrobot.support.rl.LookUpTable;
import com.learningsystems.babyrobot.support.util.Constants;

import java.io.File;

public class LookupTablePredictor implements Predictor {
    private LookUpTable lookUpTable;

    public LookupTablePredictor(QuantizedState state) {
        this.lookUpTable = new LookUpTable(state);
    }

    @Override
    public Pair<Constants.ACTION, Double> getBestAction(int state) {
        return lookUpTable.getBestAction(state);
    }

    @Override
    public double getQValue(int state, Constants.ACTION action) {
        return lookUpTable.getQValue(state, action);
    }

    @Override
    public void learn(int state, Constants.ACTION action, double qValue) {
        lookUpTable.setQValue(state, action, qValue);
    }

    @Override
    public void save(File dataFile) {
        lookUpTable.save(dataFile);
    }
}
