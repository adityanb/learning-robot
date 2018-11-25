package robots;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.function.Consumer;


public class RandomMoveRobot extends AdvancedRobot {

    private static int total = 0;
    private static int forward = 0;
    private static int backward = 0;
    private static int left = 0;
    private static int right = 0;
    private static int fire = 0;

    @Override
    public void run() {

        initRobot();
        while (true) {
            setTurnRadarRight(360);
            AN_ACTION action = AN_ACTION.random();
            action.perform(this);
            total++;
            execute();
        }
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        double forwardPer = 100 * ((double) forward / total);
        double backwardPer = 100 * ((double) backward / total);
        double leftPer = 100 * ((double) left / total);
        double rightPer = 100 * ((double) right / total);
        double firePer = 100 * ((double) fire / total);

        System.out.println("forwardPer = " + forwardPer);
        System.out.println("backwardPer = " + backwardPer);
        System.out.println("leftPer = " + leftPer);
        System.out.println("rightPer = " + rightPer);
        System.out.println("firePer = " + firePer);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        aimGunAtEnemy(e);
    }

    // normalizes a bearing to between +180 and -180
    private double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    //    http://mark.random-article.com/weber/java/robocode/lesson4.html
    private void aimGunAtEnemy(ScannedRobotEvent e) {
        double turn = getHeading() - getGunHeading() + e.getBearing();
        // normalize the turn to take the shortest path there
        setTurnGunRight(normalizeBearing(turn));
    }

    private void initRobot() {
        setColors(Color.blue, Color.red, Color.GREEN);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        //Initial Scan
        setTurnRadarRight(360);
    }

    public enum AN_ACTION {
        FORWARD(robot -> {
            robot.setAhead(150);
            forward++;
        }),
        BACKWARD(robot -> {
            robot.setBack(100);
            backward++;
        }),
        LEFT(robot -> {
            robot.setTurnLeft(15);
            left++;
        }),
        RIGHT(robot -> {
            robot.setTurnRight(15);
            right++;
        }),
        FIRE(robot -> {
            if (robot.getGunHeat() == 0) {
                robot.setFire(1);
            }
            fire++;
        });

        private Consumer<AdvancedRobot> robotConsumer;

        AN_ACTION(Consumer<AdvancedRobot> robotConsumer) {
            this.robotConsumer = robotConsumer;
        }

        public static AN_ACTION random() {
            return values()[(int) (Math.random() * 5)];
        }

        public void perform(AdvancedRobot robot) {
            robotConsumer.accept(robot);
        }
    }


}
