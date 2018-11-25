package com.learningsystems.babyrobot.support.model;

import robocode.ScannedRobotEvent;

public class Enemy {

    private final double distance;
    private final double bearing;

    public static final Enemy ABSENT_ENEMY = new Enemy(0, 0);

    private Enemy(double distance, double bearing) {
        this.distance = distance;
        this.bearing = bearing;
    }

    public static Enemy update(ScannedRobotEvent e) {
        return new Enemy(e.getDistance(), e.getBearing());
    }

    public double getDistance() {
        return distance;
    }

    public double getBearing() {
        return bearing;
    }
}
