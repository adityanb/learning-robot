package com.learningsystems.babyrobot.support;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import robocode.control.events.BattleMessageEvent;

public class BattleEngine {

    public static void main(String[] args) {
        RobocodeEngine.setLogMessagesEnabled(true);
        RobocodeEngine engine = new RobocodeEngine();
        engine.addBattleListener(new BattleObserver());

        int numberOfRounds = 10000;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
//        RobotSpecification[] selectedRobots = engine.getLocalRepository("robots.BabyRobot,sample.Corners");
        RobotSpecification[] selectedRobots = engine.getLocalRepository("robots.NeuralBabyRobot,sample.Corners");
        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);

        // Run our specified battle and let it run till it is over
        engine.runBattle(battleSpec, true); // waits till the battle finishes

        // Cleanup our RobocodeEngine
        engine.close();

        // Make sure that the Java VM is shut down properly
        System.exit(0);
    }

    public static class BattleObserver extends BattleAdaptor {

        // Called when the battle is completed successfully with battle results
        public void onBattleCompleted(BattleCompletedEvent e) {
            System.out.println("-- Battle has completed --");

            // Print out the sorted results with the robot names
            System.out.println("Battle results:");
            for (robocode.BattleResults result : e.getSortedResults()) {
                System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
            }
        }

        // Called when the game sends out an information message during the battle
        public void onBattleMessage(BattleMessageEvent e) {
            System.out.println("Msg> " + e.getMessage());
        }

        // Called when the game sends out an error message during the battle
        public void onBattleError(BattleErrorEvent e) {
            System.out.println("Err> " + e.getError());
        }
    }
}
