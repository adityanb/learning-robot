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


    Pair<Constants.ACTION, Double> getBestAction(int state) {
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

    double getQValue(int state, Constants.ACTION action) {
        return table[state][action.ordinal()];
    }

    void setQValue(int state, Constants.ACTION action, double value) {
        table[state][action.ordinal()] = value;
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

    void save(File file) {
        try (PrintStream write = new PrintStream(new RobocodeFileOutputStream(file))) {
            for (int i = 0; i < quantizedState.getNumberOfStates(); i++) {
                for (int j = 0; j < Constants.ACTION.values().length; j++) {
                    write.println(new Double(table[i][j]));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialise() {
        for (int i = 0; i < quantizedState.getNumberOfStates(); i++)
            for (int j = 0; j < Constants.ACTION.values().length; j++)
                table[i][j] = 0.0;
    }
}
