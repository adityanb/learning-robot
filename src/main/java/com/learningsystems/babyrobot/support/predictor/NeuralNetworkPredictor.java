package com.learningsystems.babyrobot.support.predictor;

import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.util.Constants;
import com.learningsystems.babyrobot.support.util.Normalization;
import com.learningsystems.backpropagation.FeedforwardNetwork;

import java.io.File;
import java.util.List;

public class NeuralNetworkPredictor implements Predictor {
    private Input input;

    public NeuralNetworkPredictor(Input input) {
        this.input = input;
    }

    public static void main(String[] args) {
        /*NeuralNetworkPredictor neuralNetworkPredictor = new NeuralNetworkPredictor(state);
        Pair<Constants.ACTION, Double> bestAction = neuralNetworkPredictor.getBestAction(908);
        System.out.println("bestAction = " + bestAction);*/
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

    @Override
    public double getQValue(int state, Constants.ACTION action) {
        double[] doubles = input.network().computeOutputs(input.normalize(state, action));
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

    interface Input {
        static double scaleMean(int ordinal, Normalization.Stat stat) {
            return (ordinal - stat.getAverage()) / (stat.getMax() - stat.getMin());
        }

        double[] normalize(int state, Constants.ACTION action);

        FeedforwardNetwork network();
    }

    static class EncodedInput implements Input {

        private final Normalization.Stat encodedStateStat;
        private final Normalization.Stat encodedAction;

        public EncodedInput() {
            List<Normalization.Stat> stats = Normalization.Stat.load("/Users/aditya/Development/ML/LearningSystemsRLRobot/data/TrainingRuns/EncodedLUT-Normalized-IN-2-HN-9-MTM-0.5-LR-6.0E-4-stats.txt");
            encodedStateStat = stats.get(0);
            encodedAction = stats.get(1);
        }

        @Override
        public double[] normalize(int state, Constants.ACTION action) {
            return new double[]{Input.scaleMean(state, encodedStateStat), Input.scaleMean(action.ordinal(), encodedAction)};
        }

        @Override
        public FeedforwardNetwork network() {
            return FeedforwardNetwork.loadFromFile("/Users/aditya/Development/ML/LearningSystemsRLRobot/data/TrainingRuns/EncodedLUT-Normalized-IN-2-HN-9-MTM-0.5-LR-6.0E-4-Weights.ser");
        }
    }
}
