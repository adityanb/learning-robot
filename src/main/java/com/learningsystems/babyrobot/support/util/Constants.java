package com.learningsystems.babyrobot.support.util;

import com.learningsystems.babyrobot.support.model.Enemy;
import robocode.AdvancedRobot;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;

public class Constants {

    public static final String LOOKUP_TABLE_DB = "LookupTable.txt";
    public static final String EXPLODED_LOOKUP_TABLE_DB = "Exploded-LookupTable.txt";
    public static final String BATTLE_REPORT_FILE = "BattleReport.csv";
    public static final double LEARNING_RATE = 0.1;
    public static final double DISCOUNT_FACTOR = 0.9;
    public static final int BATTLE_AUDIT_BATCH_PERCENTAGE = 10;


    public enum ACTION {
        FORWARD((robot, enemy) -> {
            robot.setAhead(150);
        }),
        BACKWARD((robot, enemy) -> robot.setBack(100)),
        LEFT((robot, enemy) -> robot.setTurnLeft(15)),
        RIGHT((robot, enemy) -> robot.setTurnRight(15)),
        FIRE((robot, enemy) -> {
            if (robot.getGunHeat() == 0) {
                RobotFunctions.aimGunAtEnemy(robot, enemy);
                robot.setFire(1);
            }
        }),
        ;

        private BiConsumer<AdvancedRobot, Enemy> robotConsumer;

        ACTION(BiConsumer<AdvancedRobot, Enemy> robotConsumer) {
            this.robotConsumer = robotConsumer;
        }

        public static ACTION random() {
            return values()[ThreadLocalRandom.current().nextInt(values().length)];
        }

        public void perform(AdvancedRobot robot, Enemy enemy) {
            robotConsumer.accept(robot, enemy);
        }

    }

    public enum REWARD_POLICY {
        INTERMEDIATE, TERMINAL
    }

    public enum MOVE_POLICY {
        GREEDY, EXPLORATORY
    }

    public enum POLICY {
        OFF_POLICY, ON_POLICY
    }

    public enum REWARD {
        ON_WIN(1), ON_LOSE(-1), ON_GOOD_MOVE(0.01), ON_BAD_MOVE(-0.01);

        private double reward;

        REWARD(double reward) {
            this.reward = reward;
        }

        public double reward() {
            return reward;
        }
    }
}
