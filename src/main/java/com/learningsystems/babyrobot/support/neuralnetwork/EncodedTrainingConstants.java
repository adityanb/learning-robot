package com.learningsystems.babyrobot.support.neuralnetwork;

import com.learningsystems.babyrobot.support.util.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class EncodedTrainingConstants implements TrainingConstants {


    @Override
    public Integer getInputNodes() {
        return 2;
    }

    @Override
    public Integer getHiddenNodes() {
        return 9;
    }

    @Override
    public Double momentum() {
        return 0.01;
    }

    @Override
    public Double learnRate() {
        return 0.0005;
    }

    @Override
    public File getInputFile() {
        return new File("/Users/aditya/Development/ML/LearningSystemsRLRobot/target/classes/robots/BabyRobot.data/" + Constants.LOOKUP_TABLE_DB);
    }

    @Override
    public InputFileProcessingResult processInputFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getInputFile()))) {
            String line;
            int numberOfStates = Integer.parseInt(bufferedReader.readLine());

            List<List<Double>> allInputs = new ArrayList<>();
            List<List<Double>> allOutputs = new ArrayList<>();

            List<Double> encodedState = new ArrayList<>();
            List<Double> action = new ArrayList<>();
            List<Double> qValues = new ArrayList<>();

            allInputs.add(encodedState);
            allInputs.add(action);

            allOutputs.add(qValues);

            while ((line = bufferedReader.readLine()) != null) {
                String[] dataSplit = line.split(";");
                String[] inputs = dataSplit[0].split(",");

                encodedState.add(Double.valueOf(inputs[0]));
                action.add(Double.valueOf(inputs[1]));

                qValues.add(Double.valueOf(dataSplit[1]));
            }
            return new InputFileProcessingResult(allInputs.toArray(new List[]{}), numberOfStates, allOutputs.toArray(new List[]{}));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
