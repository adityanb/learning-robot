package com.learningsystems.robot.support.util;

import com.learningsystems.robot.support.model.Enemy;
import robocode.AdvancedRobot;

public class RobotFunctions {

    //    http://mark.random-article.com/weber/java/robocode/lesson4.html
    public static void aimGunAtEnemy(AdvancedRobot robot, Enemy enemy) {
        double turn = robot.getHeading() - robot.getGunHeading() + enemy.getBearing();
        // normalize the turn to take the shortest path there
        robot.setTurnGunRight(normalizeBearing(turn));
    }

    // normalizes a bearing to between +180 and -180
    private static double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }
}
