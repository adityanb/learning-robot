package com.learningsystems.robot.support.predictor;

import com.learningsystems.robot.support.model.Pair;
import com.learningsystems.robot.support.model.QuantizedState;
import com.learningsystems.robot.support.rl.LookUpTable;
import com.learningsystems.robot.support.util.Constants;

import java.io.File;

public class LookupTablePredictor implements Predictor {
    private LookUpTable lookUpTable;

    public LookupTablePredictor(QuantizedState state) {
        this.lookUpTable = new LookUpTable(state);
    }

    @Override
    public Pair<Constants.ACTION, Double> getBestAction(Double state) {
        return lookUpTable.getBestAction(state.intValue());
    }

    @Override
    public double getQValue(Double state, Constants.ACTION action) {
        return lookUpTable.getQValue(state.intValue(), action);
    }

    @Override
    public void learn(Double state, Constants.ACTION action, double qValue) {
        lookUpTable.setQValue(state.intValue(), action, qValue);
    }

    @Override
    public void save(File dataFile) {
        lookUpTable.save(dataFile);
    }

    @Override
    public void saveAnother(File dataFile) {
        lookUpTable.saveAnother(dataFile);
    }

    public LookUpTable getLookUpTable() {
        return lookUpTable;
    }
}
