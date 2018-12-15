package com.learningsystems.babyrobot.support.model;

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

    public int getStateRepresenting(Query query) {
        return uniqueStates[query.getSelfBearing()][query.getEnemyDistance()][query.getEnemyBearing()][query.getxPosition()][query.getyPosition()];
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

    public static class QueryBuilder {
        private BEARING selfBearing;
        private BEARING enemyBearing;
        private DISTANCE enemyDistance;
        private int xPosition;
        private int yPosition;

        public QueryBuilder withHeading(double heading) {
            this.selfBearing = BEARING.quantizedValue((int) heading);
            return this;
        }

        public QueryBuilder withEnemyBearing(double enemyBearing) {
//            Enemy bearing is a quantizedValue between -180 and +180. So we need to make this quantizedValue positive
            this.enemyBearing = BEARING.quantizedValue((int) (enemyBearing + 180d));
            return this;
        }

        public QueryBuilder withEnemyDistance(double enemyDistance) {
            this.enemyDistance = DISTANCE.quantizedValue((int) enemyDistance);
            return this;
        }

        public QueryBuilder withXPosition(double xPosition) {
            this.xPosition = quantizePositionOnGrid((int) xPosition);
            return this;
        }

        public QueryBuilder withYPosition(double yPosition) {
            this.yPosition = quantizePositionOnGrid((int) yPosition);
            return this;
        }

        public Query build() {
            return new Query(selfBearing, enemyBearing, enemyDistance, xPosition, yPosition);
        }

        private int quantizePositionOnGrid(int position) {
            return position / 100;
        }
    }

    public static class Query {

        private final BEARING selfBearing;
        private final BEARING enemyBearing;
        private final DISTANCE enemyDistance;
        private final int xPosition;
        private final int yPosition;

        private Query(BEARING selfBearing, BEARING enemyBearing, DISTANCE enemyDistance, int xPosition, int yPosition) {
            this.selfBearing = selfBearing;
            this.enemyBearing = enemyBearing;
            this.enemyDistance = enemyDistance;
            this.xPosition = xPosition;
            this.yPosition = yPosition;
        }

        int getSelfBearing() {
            return selfBearing.ordinal();
        }

        int getEnemyBearing() {
            return enemyBearing.ordinal();
        }

        int getEnemyDistance() {
            return enemyDistance.ordinal();
        }

        int getxPosition() {
            return xPosition;
        }

        int getyPosition() {
            return yPosition;
        }

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