package com.learningsystems.babyrobot.support.rl;

import com.learningsystems.babyrobot.support.predictor.Predictor;
import com.learningsystems.babyrobot.support.util.Constants;

import java.io.File;

public class QLearner {

    public Predictor predictor;
    private Double previousState;
    private Constants.ACTION previousAction = Constants.ACTION.FORWARD;

    public QLearner(Predictor predictor) {
        this.predictor = predictor;
    }

    public void learn(Double currentState, Constants.ACTION currentAction, double reward, Constants.POLICY policy) {
        double lastQValue = predictor.getQValue(previousState, previousAction);
        double error;
        if (policy.equals(Constants.POLICY.OFF_POLICY)) {
            error = Constants.LEARNING_RATE * (reward + Constants.DISCOUNT_FACTOR * predictor.getBestAction(currentState).getValue() - lastQValue);
            double currentQ = lastQValue + error;
            predictor.learn(previousState, previousAction, currentQ);
        } else {
            error = Constants.LEARNING_RATE * (reward + Constants.DISCOUNT_FACTOR * predictor.getQValue(currentState, currentAction) - lastQValue);
            double currentQ = lastQValue + error;
            predictor.learn(previousState, previousAction, currentQ);
        }
        previousState = currentState;
        previousAction = currentAction;
    }

    public Constants.ACTION selectAction(Double state, Constants.MOVE_POLICY option) {
        return option.equals(Constants.MOVE_POLICY.GREEDY) ? predictor.getBestAction(state).getKey() : Constants.ACTION.random();
    }

    public void save(File dataFile) {
        predictor.save(dataFile);
    }

    public void saveAnother(File dataFile) {
        predictor.saveAnother(dataFile);
    }
}

