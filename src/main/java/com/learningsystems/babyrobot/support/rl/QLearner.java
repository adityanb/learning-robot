package com.learningsystems.babyrobot.support.rl;

import com.learningsystems.babyrobot.support.util.Constants;

import java.io.File;

public class QLearner {

    public LookUpTable lookUpTable;
    private int previousState;
    private Constants.ACTION previousAction = Constants.ACTION.FORWARD;

    public QLearner(LookUpTable lookUpTable) {
        this.lookUpTable = lookUpTable;
    }

    public void learn(int currentState, Constants.ACTION currentAction, double reward, Constants.POLICY policy) {
        double lastQValue = lookUpTable.getQValue(previousState, previousAction);
        double error;
        if (policy.equals(Constants.POLICY.OFF_POLICY)) {
            error = Constants.LEARNING_RATE * (reward + Constants.DISCOUNT_FACTOR * lookUpTable.getBestAction(currentState).getValue() - lastQValue);
            double current_Q = lastQValue + error;
            lookUpTable.setQValue(previousState, previousAction, current_Q);
        } else {
            error = Constants.LEARNING_RATE * (reward + Constants.DISCOUNT_FACTOR * lookUpTable.getQValue(currentState, currentAction) - lastQValue);
            double current_Q = lastQValue + error;
            lookUpTable.setQValue(previousState, previousAction, current_Q);
        }
        previousState = currentState;
        previousAction = currentAction;
    }

    public Constants.ACTION selectAction(int state, Constants.MOVE_POLICY option) {
        return option.equals(Constants.MOVE_POLICY.GREEDY) ? lookUpTable.getBestAction(state).getKey() : Constants.ACTION.random();
    }

    public void learnFromLastBattle(File dataFile) {
        lookUpTable.load(dataFile);
    }

    public void saveLookUpTable(File dataFile) {
        lookUpTable.save(dataFile);
    }
}

