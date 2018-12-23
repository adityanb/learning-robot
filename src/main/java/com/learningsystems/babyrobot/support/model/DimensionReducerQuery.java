package com.learningsystems.babyrobot.support.model;

import java.util.StringJoiner;

public class DimensionReducerQuery {

    private final double selfBearing;
    private final double enemyBearing;
    private final double enemyDistance;
    private final double xPosition;
    private final double yPosition;

    public DimensionReducerQuery(double selfBearing, double enemyBearing, double enemyDistance, double xPosition, double yPosition) {
        this.selfBearing = selfBearing;
        this.enemyBearing = enemyBearing;
        this.enemyDistance = enemyDistance;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        return joiner
                .add(String.valueOf(getSelfBearing()))
                .add(String.valueOf(getEnemyDistance()))
                .add(String.valueOf(getEnemyBearing()))
                .add(String.valueOf(getxPosition()))
                .add(String.valueOf(getyPosition()))
                .toString();
    }

    double getSelfBearing() {
        return selfBearing;
    }

    double getEnemyBearing() {
        return enemyBearing;
    }

    double getEnemyDistance() {
        return enemyDistance;
    }

    double getxPosition() {
        return xPosition;
    }

    double getyPosition() {
        return yPosition;
    }

    public static class Builder {
        private double selfBearing;
        private double enemyBearing;
        private double enemyDistance;
        private double xPosition;
        private double yPosition;

        public Builder withSelfBearing(double heading) {
            this.selfBearing = (heading / 90);
            return this;
        }

        public Builder withEnemyBearing(double enemyBearing) {
            //Enemy bearing is a quantizedValue between -180 and +180. So we need to make this quantizedValue positive
            this.enemyBearing = ((enemyBearing + 180d) / 90);
            return this;
        }

        public Builder withEnemyDistance(double enemyDistance) {
            this.enemyDistance = enemyDistance / 500;
            return this;
        }

        public Builder withXPosition(double xPosition) {
            this.xPosition = quantizePositionOnGrid((int) xPosition);
            return this;
        }

        public Builder withYPosition(double yPosition) {
            this.yPosition = quantizePositionOnGrid((int) yPosition);
            return this;
        }

        public DimensionReducerQuery build() {
            return new DimensionReducerQuery(selfBearing, enemyBearing, enemyDistance, xPosition, yPosition);
        }

        private double quantizePositionOnGrid(int position) {
            return ((double) position) / 100;
        }
    }
}
