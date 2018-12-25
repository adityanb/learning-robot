package com.learningsystems.robot.support.rl;

import com.learningsystems.robot.support.Logger;
import com.learningsystems.robot.support.model.Pair;
import com.learningsystems.robot.support.predictor.Predictor;
import com.learningsystems.robot.support.util.Constants;

import java.io.File;

public class QLearner {

    private Predictor predictor;
    private double previousState;
    private Constants.ACTION previousAction = Constants.ACTION.FORWARD;
    private Logger logger;

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
        return option.equals(Constants.MOVE_POLICY.GREEDY) ? getBestAction(state) : Constants.ACTION.random();
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private Constants.ACTION getBestAction(Double state) {
        Pair<Constants.ACTION, Double> bestActionQValuePair = predictor.getBestAction(state);
        Constants.ACTION action = bestActionQValuePair.getKey();
        Double qValue = bestActionQValuePair.getValue();
        logger.log(state + "," + action.ordinal() + "," + qValue);
        return action;
    }

    public void save(File dataFile) {
        predictor.save(dataFile);
    }

    public void saveAnother(File dataFile) {
        predictor.saveAnother(dataFile);
    }
}

