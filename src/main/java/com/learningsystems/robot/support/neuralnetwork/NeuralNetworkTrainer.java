package com.learningsystems.robot.support.neuralnetwork;

import com.learningsystems.backpropagation.Backpropagation;
import com.learningsystems.backpropagation.FeedforwardLayer;
import com.learningsystems.backpropagation.FeedforwardNetwork;
import com.learningsystems.backpropagation.Train;
import com.learningsystems.robot.support.model.Pair;
import com.learningsystems.robot.support.predictor.NeuralNetworkPredictor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.StringJoiner;

public class NeuralNetworkTrainer {
    private final FeedforwardNetwork network;
    private TrainingConstants trainingConstants;

    public NeuralNetworkTrainer(TrainingConstants trainingConstants) {
        this.trainingConstants = trainingConstants;
        network = new FeedforwardNetwork();
        network.addLayer(new FeedforwardLayer(trainingConstants.getInputNodes()));
        network.addLayer(new FeedforwardLayer(trainingConstants.getHiddenNodes()));
        network.addLayer(new FeedforwardLayer(1));
        network.reset();
    }

    public static void main(String[] args) {
//        NeuralNetworkTrainer neuralNetwork = new NeuralNetworkTrainer(new ExplodedTrainingConstants());
        NeuralNetworkTrainer neuralNetwork = new NeuralNetworkTrainer(new EncodedTrainingConstants());
//        NeuralNetworkTrainer neuralNetwork = new NeuralNetworkTrainer(new UnNormalizedExplodedTrainingConstants());
//        NeuralNetworkTrainer neuralNetwork = new NeuralNetworkTrainer(new UnNormalizedLUTTrainingConstants());
        File epochLog = new File(new StringJoiner("-")
                .add(neuralNetwork.trainingConstants.stringRepresentation())
                .add("EpochData.csv")
                .toString());
        neuralNetwork.train(epochLog);
    }

    private void train(File epochData) {
        Pair<double[][], double[][]> trainingData = getTrainingData();
        Train train = new Backpropagation(
                network,
                trainingData.getKey(),
                trainingData.getValue(),
                trainingConstants.learnRate(),
                trainingConstants.momentum());
        int epoch = 0;
        double oldError = 0;
        double currentError = 0;
        NeuralNetworkPredictor.Input input = new NeuralNetworkPredictor.EncodedInput();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(epochData));
             BufferedWriter writer1 = new BufferedWriter(new FileWriter("Diff.log"))) {
            do {
                oldError = currentError;
                train.iteration();
                epoch++;
                currentError = train.getError();
                System.out.println("Epoch: " + epoch + " Error:" + currentError);
                if (epoch % 100 == 0) {
                    writer.write(epoch + "," + currentError + "\n");
                    /*double output613 = Math.abs(-0.001638835 - network.computeOutputs(input.normalize(613d, Constants.ACTION.FIRE))[0]);
                    double output642 = Math.abs(-0.004537928 - network.computeOutputs(input.normalize(642d, Constants.ACTION.FIRE))[0]);
                    double output2074 = Math.abs(-0.009823073 - network.computeOutputs(input.normalize(2074d, Constants.ACTION.FIRE))[0]);
                    writer1.write(output613 + "," + output642 + "," + output2074 + "\n");*/
                }
            } while ((epoch < 10000) && (train.getError() > 0.005) && currentError != oldError);
            network.persist(trainingConstants.stringRepresentation() + "-Weights.ser");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Pair<double[][], double[][]> getTrainingData() {

        TrainingConstants.InputFileProcessingResult result = trainingConstants.processInputFile();

        double[][] inputs = trainingConstants.normalization().apply(result.getNumberOfStates(), result.getInputs());
        double[][] scaledQValues = trainingConstants.normalization().apply(result.getNumberOfStates(), result.getOutputs());

        System.out.println("scaledQValues = " + Arrays.deepToString(scaledQValues));
        System.out.println("inputs = " + Arrays.deepToString(inputs));

        return Pair.of(inputs, scaledQValues);

    }
}

