package robots;

import com.learningsystems.robot.support.Logger;
import com.learningsystems.robot.support.model.*;
import com.learningsystems.robot.support.predictor.Predictor;
import com.learningsystems.robot.support.rl.Options;
import com.learningsystems.robot.support.rl.QLearner;
import com.learningsystems.robot.support.util.Constants;
import com.learningsystems.robot.support.util.Constants.MOVE_POLICY;
import com.learningsystems.robot.support.util.Constants.REWARD_POLICY;
import com.learningsystems.robot.support.util.ReportGenerator;
import robocode.AdvancedRobot;
import robocode.DeathEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

import java.awt.*;


public class MuscularRobot extends AdvancedRobot {

    //QLearner and QuantizedState are kept static so that we don't have to read and write the lookup table to the file at the end of each round
    private static QuantizedState quantizedState = new QuantizedState(new BattleGroundDimension(800, 600));
    private static Predictor predictor = Predictor.get(Predictor.FLAVOR.NEURAL_NETWORK_ENCODED, quantizedState);
    //    private static Predictor predictor = Predictor.get(Predictor.FLAVOR.NEURAL_NETWORK_EXPLODED, quantizedState);
    private static QLearner qLearner = new QLearner(predictor);
    static BattleAuditor battleAuditor;
    private static boolean isNewBattle = true;
    private static MOVE_POLICY oldMovePolicy = MOVE_POLICY.EXPLORATORY;
    Options options;
    Enemy enemy = Enemy.ABSENT_ENEMY;
    Logger logger;


    @Override
    public void run() {
        logger = new Logger(getDataFile("Log.log"));
        qLearner.setLogger(new Logger(getDataFile("StateAction.log")));
        onStartOfNewBattle(() -> {
            battleAuditor = new BattleAuditor(getNumRounds(), Constants.BATTLE_AUDIT_BATCH_PERCENTAGE);
        });
        initRobot();
        while (true) {
            options = getOptions(Constants.POLICY.OFF_POLICY, REWARD_POLICY.INTERMEDIATE, 50);
            setTurnRadarRight(360);
            double state = getState();
            Constants.ACTION action = qLearner.selectAction(state, options.getMovePolicy());
            action.perform(this, enemy);
//            logger.log(action.name());
            execute();

        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        enemy = Enemy.update(e);//TODO: This should be more memory efficient
    }

    @Override
    public void onWin(WinEvent event) {
        battleAuditor.recordWin();
        onEndOfBattle();
    }

    @Override
    public void onDeath(DeathEvent event) {
        battleAuditor.recordLoss();
        onEndOfBattle();
    }

    //There is an issue with the event 'onBattleEnded'. Hence, using this makeshift method.
    //The 'onBattleEnded' event does not seem to be the last method called. For e.g., onWin or onDeath could be called after this. Non-deterministic
    private void onEndOfBattle() {
        if (getRoundNum() == getNumRounds() - 1) {
            new ReportGenerator().generate(battleAuditor, getDataFile(Constants.BATTLE_REPORT_FILE));
        }
    }

    void onStartOfNewBattle(Runnable runnable) {
        if (isNewBattle) {
            runnable.run();
            isNewBattle = false;
        }
    }

    void initRobot() {
        setColors(Color.blue, Color.red, Color.GREEN);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        //Initial Scan
        setTurnRadarRight(360);
    }

    private int getState() {
        QuantizedQuery.Builder quantizingQueryBuilder = new QuantizedQuery.Builder();
        QuantizedQuery quantizedQuery = quantizingQueryBuilder
                .withHeading(getHeading())
                .withXPosition(getX())
                .withYPosition(getY())
                .withEnemyDistance(enemy.getDistance())
                .withEnemyBearing(enemy.getBearing())
                .build();
        return quantizedState.getStateRepresenting(quantizedQuery);
    }

    Options getOptions(Constants.POLICY policy, REWARD_POLICY rewardPolicy, int epsilon) {
        MOVE_POLICY movePolicy;
        //the next move is selected randomly with probability ε and greedily with probability 1 − ε
        movePolicy = getRoundNum() > (((double) epsilon / 100) * getNumRounds()) ? MOVE_POLICY.GREEDY : MOVE_POLICY.EXPLORATORY;
        onPolicyChange(movePolicy, getRoundNum());
        return Options.update(rewardPolicy, movePolicy, policy);
    }

    private void onPolicyChange(MOVE_POLICY movePolicy, int roundNum) {
        if (oldMovePolicy != movePolicy) {
            logger.log("Old policy: " + oldMovePolicy + " New policy: " + movePolicy + " Round: " + roundNum);
            oldMovePolicy = movePolicy;
        }
    }
}
