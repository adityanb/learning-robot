package com.learningsystems.babyrobot.support.predictor;

import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.model.QuantizedState;
import com.learningsystems.babyrobot.support.util.Constants;

import java.io.File;
import java.util.function.Function;

public interface Predictor {

    static Predictor get(FLAVOR flavor, QuantizedState quantizedState) {
        return flavor.getImplementation(quantizedState);
    }

    Pair<Constants.ACTION, Double> getBestAction(int state);

    double getQValue(int state, Constants.ACTION action);

    void learn(int state, Constants.ACTION action, double qValue);

    void save(File dataFile);

    void saveAnother(File dataFile);

    enum FLAVOR {
        LOOKUP_TABLE(LookupTablePredictor::new), NEURAL_NETWORK(state -> {
            return new NeuralNetworkPredictor();
        });

        private Function<QuantizedState, Predictor> predictorFunction;

        FLAVOR(Function<QuantizedState, Predictor> predictorFunction) {
            this.predictorFunction = predictorFunction;
        }

        public Predictor getImplementation(QuantizedState quantizedState) {
            return predictorFunction.apply(quantizedState);
        }
    }
}
