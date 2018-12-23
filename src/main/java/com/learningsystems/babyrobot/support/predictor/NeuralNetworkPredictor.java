package com.learningsystems.babyrobot.support.predictor;

import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.util.Constants;
import com.learningsystems.backpropagation.FeedforwardNetwork;

import java.io.File;

//Does not work because I am yet to figure out how to do online training
public class NeuralNetworkPredictor implements Predictor {
    private final FeedforwardNetwork network;

    public NeuralNetworkPredictor() {
        network = FeedforwardNetwork.loadFromFile("/Users/aditya/Development/ML/LearningSystemsRLRobot/data/TrainingRuns/2-9-0.01-5.0E-4-LookupTable.txt-Weights.ser");
    }

    @Override
    public Pair<Constants.ACTION, Double> getBestAction(int state) {
        //Best action is the one that has the maximum Q-value
        double qValue = Double.NEGATIVE_INFINITY;
        Constants.ACTION action = null;
        for (Constants.ACTION currentAction : Constants.ACTION.values()) {
            double currentQValue = getQValue(state, currentAction);
            if (currentQValue > qValue) {
                qValue = currentQValue;
                action = currentAction;
            }
        }
        return Pair.of(action, qValue);
    }

    public static void main(String[] args) {
        NeuralNetworkPredictor neuralNetworkPredictor = new NeuralNetworkPredictor();
        Pair<Constants.ACTION, Double> bestAction = neuralNetworkPredictor.getBestAction(567);
        System.out.println("bestAction = " + bestAction);
    }

    @Override
    public double getQValue(int state, Constants.ACTION action) {
        double[] doubles = network.computeOutputs(new double[]{scaleMinMax(state), scaleMinMax(action.ordinal())});
        return doubles[0];

    }

    @Override
    public void learn(int state, Constants.ACTION action, double qValue) {
        //Not implemented. Will have to do this once I figure out how to train the neural network "online" i.e., in streaming mode.
    }

    @Override
    public void save(File dataFile) {
        //Not implemented. Need to save the weights of the neural network
    }

    @Override
    public void saveAnother(File dataFile) {

    }

    private double scaleMinMax(int ordinal) {
        int min = 0;
        int max = 1;

        return ordinal - min;
    }
}
