package com.learningsystems.babyrobot.support.predictor;

import com.learningsystems.babyrobot.support.Logger;
import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.model.QuantizedState;
import com.learningsystems.babyrobot.support.rl.LookUpTable;
import com.learningsystems.babyrobot.support.util.Constants;
import com.learningsystems.backpropagation.Backpropagation;
import com.learningsystems.backpropagation.FeedforwardLayer;
import com.learningsystems.backpropagation.FeedforwardNetwork;
import com.learningsystems.backpropagation.Train;

import java.io.File;

public class NeuralNetworkPredictor implements Predictor {
    private final FeedforwardNetwork network;

    public NeuralNetworkPredictor(QuantizedState state) {
        network = new FeedforwardNetwork();
        network.addLayer(new FeedforwardLayer(2));
        network.addLayer(new FeedforwardLayer(10));
        network.addLayer(new FeedforwardLayer(1));
        network.reset();

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
        double[] doubles = network.computeOutputs(new double[]{state, action.ordinal()});
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

    public void train(LookUpTable lookUpTable, Logger logger) {
//        Pair<double[][], double[][]> trainingData = Pair.of(new double[][]{ { 0.0, 0.0 }, { 1.0, 0.0 },
//                { 0.0, 1.0 }, { 1.0, 1.0 } }, new double[][]{ { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } });
        Pair<double[][], double[][]> trainingData = lookUpTable.getTrainingData();
        Train train = new Backpropagation(network, trainingData.getKey(), trainingData.getValue(), 0.7, 0.9);
        int epoch = 0;
//        while ((epoch < 5000) && (train.getError() > 0.001)) {
        do {
            train.iteration();
            epoch++;
//            if(epoch % 500 == 0){
            logger.log("Epoch: " + epoch + " Error:" + train.getError());
//            }
        } while ((epoch < 2000) && (train.getError() > 0.001));
        logger.log("Epoch: " + epoch + " Error:" + train.getError());
    }
}
