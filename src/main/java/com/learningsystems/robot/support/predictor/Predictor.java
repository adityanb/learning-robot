package com.learningsystems.robot.support.predictor;

import com.learningsystems.robot.support.model.Pair;
import com.learningsystems.robot.support.model.QuantizedState;
import com.learningsystems.robot.support.util.Constants;

import java.io.File;
import java.util.function.Function;

public interface Predictor {

    static Predictor get(FLAVOR flavor, QuantizedState quantizedState) {
        return flavor.getImplementation(quantizedState);
    }

    Pair<Constants.ACTION, Double> getBestAction(Double state);

    double getQValue(Double state, Constants.ACTION action);

    void learn(Double state, Constants.ACTION action, double qValue);

    void save(File dataFile);

    void saveAnother(File dataFile);

    enum FLAVOR {
        LOOKUP_TABLE(LookupTablePredictor::new),
        NEURAL_NETWORK_ENCODED(state -> {
            NeuralNetworkPredictor.Input input = new NeuralNetworkPredictor.EncodedInput();
            return new NeuralNetworkPredictor(input);
        }),
        NEURAL_NETWORK_EXPLODED(state -> {
            NeuralNetworkPredictor.Input input = new NeuralNetworkPredictor.ExplodedInput(state);
            return new NeuralNetworkPredictor(input);
        }),
        ;

        private Function<QuantizedState, Predictor> predictorFunction;

        FLAVOR(Function<QuantizedState, Predictor> predictorFunction) {
            this.predictorFunction = predictorFunction;
        }

        public Predictor getImplementation(QuantizedState quantizedState) {
            return predictorFunction.apply(quantizedState);
        }
    }
}
