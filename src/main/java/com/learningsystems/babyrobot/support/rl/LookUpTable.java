package com.learningsystems.babyrobot.support.rl;

import com.learningsystems.babyrobot.support.model.Pair;
import com.learningsystems.babyrobot.support.model.QuantizedState;
import com.learningsystems.babyrobot.support.util.Constants;
import robocode.RobocodeFileOutputStream;

import java.io.*;

public class LookUpTable {

    private double[][] table;
    private QuantizedState quantizedState;

    public LookUpTable(QuantizedState quantizedState) {
        table = new double[quantizedState.getNumberOfStates()][Constants.ACTION.values().length];
        this.quantizedState = quantizedState;
        initialise();
    }

    /**
     * First two columns represent state and action, the last column represents Q values
     *
     * @return
     */
    public Pair<double[][], double[][]> getTrainingData() {
        double[][] inputs = new double[table.length * Constants.ACTION.values().length][2];
        double[][] outputs = new double[table.length * Constants.ACTION.values().length][1];
        int rowNum = 0;
        int colNum = 0;
        for (int i = 0; i < quantizedState.getNumberOfStates(); i++) {
            for (int j = 0; j < Constants.ACTION.values().length; j++) {
                inputs[rowNum][colNum] = i; // State
                inputs[rowNum][++colNum] = j; // Action
                outputs[rowNum][0] = table[i][j]; //QValue
                colNum = 0; //Reset colNum
                ++rowNum;
            }
        }
        return Pair.of(inputs, outputs);
    }

    public Pair<Constants.ACTION, Double> getBestAction(int state) {
        double maximumQValue = Double.NEGATIVE_INFINITY;
        int bestAction = 0;
        for (int i = 0; i < table[state].length; i++) {
            double qValue = table[state][i];
            if (qValue > maximumQValue) {
                maximumQValue = qValue;
                bestAction = i;
            }
        }
        return Pair.of(Constants.ACTION.values()[bestAction], maximumQValue);
    }

    public double getQValue(int state, Constants.ACTION action) {
        return table[state][action.ordinal()];
    }

    public void setQValue(int state, Constants.ACTION action, double value) {
        table[state][action.ordinal()] = value;
    }

    public void save(File file) {
        try (PrintStream standardFormatWriter = new PrintStream(new RobocodeFileOutputStream(file))) {
            standardFormatWriter.println(numberOfSamples());
            for (int i = 0; i < quantizedState.getNumberOfStates(); i++) {
                for (int j = 0; j < Constants.ACTION.values().length; j++) {
                    double qValue = table[i][j];
                    standardFormatWriter.println(i + "," + j + ";" + qValue);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAnother(File dataFile) {
        try (PrintStream explodedWriter = new PrintStream(new RobocodeFileOutputStream(dataFile))) {
            explodedWriter.println(numberOfSamples());
            for (int i = 0; i < quantizedState.getNumberOfStates(); i++) {
                String decodedState = quantizedState.decodeState(i);
                for (int j = 0; j < Constants.ACTION.values().length; j++) {
                    double qValue = table[i][j];
                    explodedWriter.println(decodedState + "," + j + ";" + qValue);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int numberOfSamples() {
        return quantizedState.getNumberOfStates() * Constants.ACTION.values().length;
    }

    void load(File file) {
        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < quantizedState.getNumberOfStates(); i++)
                for (int j = 0; j < Constants.ACTION.values().length; j++)
                    table[i][j] = Double.parseDouble(read.readLine());
        } catch (Exception e) {
            initialise();
        }
    }

    private void initialise() {
        for (int i = 0; i < quantizedState.getNumberOfStates(); i++)
            for (int j = 0; j < Constants.ACTION.values().length; j++)
                table[i][j] = 0.0;
    }
}
