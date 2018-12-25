package com.learningsystems.robot.support.model;

import robocode.ScannedRobotEvent;

public class Enemy {

    private final double distance;
    private final double bearing;
    public static final Enemy ABSENT_ENEMY = new Enemy(-1, -1, -1);
    private double energy;

    private Enemy(double distance, double bearing, double energy) {
        this.distance = distance;
        this.bearing = bearing;
        this.energy = energy;
    }

    public static Enemy update(ScannedRobotEvent e) {
        return new Enemy(e.getDistance(), e.getBearing(), e.getEnergy());
    }

    public double getDistance() {
        return distance;
    }

    public double getBearing() {
        return bearing;
    }

    public double getEnergy() {
        return energy;
    }
}
