package com.learningsystems.babyrobot.support.neuralnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ExplodedTrainingConstants implements TrainingConstants {

    @Override
    public Integer getInputNodes() {
        return 6;
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
        return new File("/Users/aditya/Development/ML/LearningSystemsRLRobot/target/classes/robots/BabyRobot.data/Another.txt");
    }

    @Override
    public InputFileProcessingResult processInputFile() {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getInputFile()))) {
            String line;
            int numberOfStates = Integer.parseInt(bufferedReader.readLine());
            List<List<Double>> allInputs = new ArrayList<>();
            List<List<Double>> allOutputs = new ArrayList<>();
            List<Double> selfBearing = new ArrayList<>();
            List<Double> enemyDistance = new ArrayList<>();
            List<Double> enemyBearing = new ArrayList<>();
            List<Double> xPosition = new ArrayList<>();
            List<Double> yPosition = new ArrayList<>();
            List<Double> actions = new ArrayList<>();
            List<Double> qValues = new ArrayList<>();

            allInputs.add(selfBearing);
            allInputs.add(enemyDistance);
            allInputs.add(enemyBearing);
            allInputs.add(xPosition);
            allInputs.add(yPosition);
            allInputs.add(actions);

            allOutputs.add(qValues);

            while ((line = bufferedReader.readLine()) != null) {
                String[] dataSplit = line.split(";");
                String[] inputs = dataSplit[0].split(",");

                selfBearing.add(Double.valueOf(inputs[0]));
                enemyDistance.add(Double.valueOf(inputs[1]));
                enemyBearing.add(Double.valueOf(inputs[2]));
                xPosition.add(Double.valueOf(inputs[3]));
                yPosition.add(Double.valueOf(inputs[4]));
                actions.add(Double.valueOf(inputs[5]));

                qValues.add(Double.valueOf(dataSplit[1]));
            }
            return new InputFileProcessingResult(allInputs.toArray(new List[]{}), numberOfStates, allOutputs.toArray(new List[]{}));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
