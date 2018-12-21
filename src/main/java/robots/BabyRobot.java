package robots;

import com.learningsystems.babyrobot.support.model.BattleAuditor;
import com.learningsystems.babyrobot.support.model.BattleGroundDimension;
import com.learningsystems.babyrobot.support.model.Enemy;
import com.learningsystems.babyrobot.support.model.QuantizedState;
import com.learningsystems.babyrobot.support.predictor.Predictor;
import com.learningsystems.babyrobot.support.rl.Options;
import com.learningsystems.babyrobot.support.rl.QLearner;
import com.learningsystems.babyrobot.support.util.Constants;
import com.learningsystems.babyrobot.support.util.Constants.MOVE_POLICY;
import com.learningsystems.babyrobot.support.util.Constants.REWARD;
import com.learningsystems.babyrobot.support.util.Constants.REWARD_POLICY;
import com.learningsystems.babyrobot.support.util.ReportGenerator;
import robocode.*;

import java.awt.*;


public class BabyRobot extends AdvancedRobot {

    //QLearner and QuantizedState are kept static so that we don't have to read and write the lookup table to the file at the end of each round
    private static QuantizedState quantizedState = new QuantizedState(new BattleGroundDimension(800, 600));
    private static Predictor predictor = Predictor.get(Predictor.FLAVOR.LOOKUP_TABLE, quantizedState);
    private static QLearner qLearner = new QLearner(predictor);
    private static BattleAuditor battleAuditor;
    private static boolean isNewBattle = true;
    private static MOVE_POLICY oldMovePolicy;
    private double reward;
    private Options options;
    private Enemy enemy = Enemy.ABSENT_ENEMY;

    @Override
    public void run() {
        onStartOfNewBattle(() -> {
//            qLearner.learnFromLastBattle(getDataFile(Constants.LOOKUP_TABLE_DB));
            battleAuditor = new BattleAuditor(getNumRounds(), Constants.BATTLE_AUDIT_BATCH_PERCENTAGE);
        });
        initRobot();
        while (true) {
            options = getOptions(Constants.POLICY.OFF_POLICY, REWARD_POLICY.INTERMEDIATE, 0);
            setTurnRadarRight(360);
            int state = getState();
            Constants.ACTION action = qLearner.selectAction(state, options.getMovePolicy());
            action.perform(this, enemy);
            execute();
            if (reward != 0) {
                if (options.getMovePolicy() != MOVE_POLICY.GREEDY) {
                    qLearner.learn(state, action, reward, options.getPolicy());
                }
                reward = 0.0;
            }
        }
    }

    @Override
    public void onBulletHitBullet(BulletHitBulletEvent event) {
        if (options.intermediateRewardsAllowed()) {
            reward += REWARD.ON_BAD_MOVE.reward();
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        enemy = Enemy.update(e);//TODO: This should be more memory efficient
    }

    @Override
    public void onBulletHit(BulletHitEvent e) {
        if (options.intermediateRewardsAllowed()) {
            reward += REWARD.ON_GOOD_MOVE.reward();

        }
    }

    @Override
    public void onBulletMissed(BulletMissedEvent e) {
        if (options.intermediateRewardsAllowed()) {
            reward += REWARD.ON_BAD_MOVE.reward();
        }

    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        if (options.intermediateRewardsAllowed()) {
            reward += REWARD.ON_BAD_MOVE.reward();
        }
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        if (options.intermediateRewardsAllowed()) {
            reward += REWARD.ON_BAD_MOVE.reward();
        }
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        if (options.intermediateRewardsAllowed()) {
            reward += REWARD.ON_BAD_MOVE.reward();
        }
    }

    @Override
    public void onWin(WinEvent event) {
        reward += REWARD.ON_WIN.reward();
        battleAuditor.recordWin();
        onEndOfBattle();
    }

    @Override
    public void onDeath(DeathEvent event) {
        reward += REWARD.ON_LOSE.reward();
        battleAuditor.recordLoss();
        onEndOfBattle();
    }

    //There is an issue with the event 'onBattleEnded'. Hence, using this makeshift method.
    //The 'onBattleEnded' event does not seem to be the last method called. For e.g., onWin or onDeath could be called after this. Non-deterministic
    private void onEndOfBattle() {
        if (getRoundNum() == getNumRounds() - 1) {
            qLearner.save(getDataFile(Constants.LOOKUP_TABLE_DB));
            new ReportGenerator().generate(battleAuditor, getDataFile(Constants.BATTLE_REPORT_FILE));
        }
    }

    private void onStartOfNewBattle(Runnable runnable) {
        if (isNewBattle) {
            runnable.run();
            isNewBattle = false;
        }
    }

    private void initRobot() {
        setColors(Color.blue, Color.red, Color.GREEN);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        //Initial Scan
        setTurnRadarRight(360);
    }

    private int getState() {
        QuantizedState.QueryBuilder queryBuilder = new QuantizedState.QueryBuilder();
        QuantizedState.Query query = queryBuilder
                .withHeading(getHeading())
                .withXPosition(getX())
                .withYPosition(getY())
                .withEnemyDistance(enemy.getDistance())
                .withEnemyBearing(enemy.getBearing())
                .build();
        return quantizedState.getStateRepresenting(query);
    }

    private Options getOptions(Constants.POLICY policy, REWARD_POLICY rewardPolicy, int epsilon) {
        MOVE_POLICY movePolicy;

        if (epsilon == 0) {
            epsilon = 80; //default exploratory policy to 80%
        }
        //the next move is selected randomly with probability ε and greedily with probability 1 − ε
        movePolicy = getRoundNum() > (((double) epsilon / 100) * getNumRounds()) ? MOVE_POLICY.GREEDY : MOVE_POLICY.EXPLORATORY;
        logPolicyChange(movePolicy, getRoundNum());
        return Options.update(rewardPolicy, movePolicy, policy);
    }

    private void logPolicyChange(MOVE_POLICY movePolicy, int roundNum) {
        if (oldMovePolicy != movePolicy) {
            System.out.println("Old policy: " + oldMovePolicy + " New policy: " + movePolicy + " Round: " + roundNum);
            oldMovePolicy = movePolicy;
        }
    }
}
