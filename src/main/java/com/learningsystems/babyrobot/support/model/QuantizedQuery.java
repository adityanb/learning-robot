package com.learningsystems.babyrobot.support.model;

public class QuantizedQuery {

    private final QuantizedState.BEARING selfBearing;
    private final QuantizedState.BEARING enemyBearing;
    private final QuantizedState.DISTANCE enemyDistance;
    private final int xPosition;
    private final int yPosition;

    private QuantizedQuery(QuantizedState.BEARING selfBearing, QuantizedState.BEARING enemyBearing, QuantizedState.DISTANCE enemyDistance, int xPosition, int yPosition) {
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

    public static class Builder {
        private QuantizedState.BEARING selfBearing;
        private QuantizedState.BEARING enemyBearing;
        private QuantizedState.DISTANCE enemyDistance;
        private int xPosition;
        private int yPosition;

        public Builder withHeading(double heading) {
            this.selfBearing = QuantizedState.BEARING.quantizedValue((int) heading);
            return this;
        }

        public Builder withEnemyBearing(double enemyBearing) {
            //            Enemy bearing is a quantizedValue between -180 and +180. So we need to make this quantizedValue positive
            this.enemyBearing = QuantizedState.BEARING.quantizedValue((int) (enemyBearing + 180d));
            return this;
        }

        public Builder withEnemyDistance(double enemyDistance) {
            this.enemyDistance = QuantizedState.DISTANCE.quantizedValue((int) enemyDistance);
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

        public QuantizedQuery build() {
            return new QuantizedQuery(selfBearing, enemyBearing, enemyDistance, xPosition, yPosition);
        }

        private int quantizePositionOnGrid(int position) {
            return position / 100;
        }
    }
}
