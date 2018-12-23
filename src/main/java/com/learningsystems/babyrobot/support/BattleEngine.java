package com.learningsystems.babyrobot.support;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public class BattleEngine {

    public static void main(String[] args) {
        RobocodeEngine.setLogMessagesEnabled(true);
        RobocodeEngine engine = new RobocodeEngine();
        engine.addBattleListener(new BattleObserver());

        int numberOfRounds = 1000;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
//        RobotSpecification[] selectedRobots = engine.getLocalRepository("robots.BabyRobot,sample.Corners");
        RobotSpecification[] selectedRobots = engine.getLocalRepository("robots.BabyRobot,sample.Corners");
        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);

        // Run our specified battle and let it run till it is over
        engine.runBattle(battleSpec, true); // waits till the battle finishes

        // Cleanup our RobocodeEngine
        engine.close();

        // Make sure that the Java VM is shut down properly
        System.exit(0);
    }

    public static class BattleObserver extends BattleAdaptor {
        long battleStart;
        long roundStart;
        private final BufferedWriter writer;
        private final StringJoiner buffer;

        public BattleObserver() {
            buffer = new StringJoiner("\n");
            try {
                writer = new BufferedWriter(new FileWriter("BattleStats-LUT.txt"));
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onBattleStarted(BattleStartedEvent event) {
            battleStart = System.currentTimeMillis();
            super.onBattleStarted(event);
        }

        @Override
        public void onRoundStarted(RoundStartedEvent event) {
            roundStart = System.currentTimeMillis();
            super.onRoundStarted(event);
        }

        @Override
        public void onRoundEnded(RoundEndedEvent event) {
            buffer.add("Round:" + (System.currentTimeMillis() - roundStart));
            super.onRoundEnded(event);
        }

        // Called when the battle is completed successfully with battle results
        public void onBattleCompleted(BattleCompletedEvent e) {
            buffer.add("Battle:" + (System.currentTimeMillis() - battleStart));
            System.out.println("-- Battle has completed --");

            // Print out the sorted results with the robot names
            System.out.println("Results:");
            for (robocode.BattleResults result : e.getSortedResults()) {
                System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
            }

            String timingInfo = buffer.toString();
            try {
                writer.write(timingInfo);
            } catch (IOException e1) {
                throw new RuntimeException(e1);
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
