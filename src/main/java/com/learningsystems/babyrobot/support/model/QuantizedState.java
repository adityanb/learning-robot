package com.learningsystems.babyrobot.support.model;

import java.util.StringJoiner;

public class QuantizedState {
    private int numberOfStates;
    private int[][][][][] uniqueStates;
    private BattleGroundDimension battleGroundDimension;

    public QuantizedState(BattleGroundDimension battleGroundDimension) {
        this.battleGroundDimension = battleGroundDimension;
        uniqueStates = init();
    }

    public int getNumberOfStates() {
        return numberOfStates;
    }

    public int getStateRepresenting(QuantizedQuery quantizedQuery) {
        return uniqueStates[quantizedQuery.getSelfBearing()][quantizedQuery.getEnemyDistance()][quantizedQuery.getEnemyBearing()][quantizedQuery.getxPosition()][quantizedQuery.getyPosition()];
    }

    public String decodeState(int state) {
        for (int a = 0; a < BEARING.numberOfStates(); a++) {
            for (int b = 0; b < DISTANCE.numberOfStates(); b++) {
                for (int c = 0; c < BEARING.numberOfStates(); c++) {
                    for (int d = 0; d < battleGroundDimension.getQuantizedX(); d++) {
                        for (int e = 0; e < battleGroundDimension.getQuantizedY(); e++) {
                            if (uniqueStates[a][b][c][d][e] == state) {
                                return new StringJoiner(",")
                                        .add(Integer.toString(a))
                                        .add(Integer.toString(b))
                                        .add(Integer.toString(c))
                                        .add(Integer.toString(d))
                                        .add(Integer.toString(e))
                                        .toString();
                            }
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Could not find a representation of state " + state);
    }

    private int[][][][][] init() {
        int[][][][][] states = new int[BEARING.numberOfStates()][DISTANCE.numberOfStates()][BEARING.numberOfStates()][battleGroundDimension.getQuantizedX()][battleGroundDimension.getQuantizedY()];
        int count = 0;
        for (int a = 0; a < BEARING.numberOfStates(); a++) {
            for (int b = 0; b < DISTANCE.numberOfStates(); b++) {
                for (int c = 0; c < BEARING.numberOfStates(); c++) {
                    for (int d = 0; d < battleGroundDimension.getQuantizedX(); d++) {
                        for (int e = 0; e < battleGroundDimension.getQuantizedY(); e++) {
                            states[a][b][c][d][e] = count++;
                        }
                    }
                }
            }
        }
        numberOfStates = count;
        return states;

    }

    enum BEARING {
        NORTH, SOUTH, EAST, WEST;

        //TODO: Re-use implementation in the main robot class
        public static BEARING quantizedValue(int heading) {
            return BEARING.values()[(int) (heading / 90)];
        }

        public static int numberOfStates() {
            return values().length;
        }

    }

    enum DISTANCE {
        CLOSE, NEAR, FAR;

        public static int numberOfStates() {
            return values().length;
        }

        public static DISTANCE quantizedValue(int distance) {
            int reduced = distance / 100; //Maximum distance is hypotenuse of 800 by 600 = 1000
            if (reduced < 2) {
                return CLOSE;
            }

            if (reduced < 5) {
                return NEAR;
            }

            return FAR;
        }

    }
}