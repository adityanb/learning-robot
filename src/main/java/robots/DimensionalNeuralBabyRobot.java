package robots;

import com.learningsystems.babyrobot.support.Logger;
import com.learningsystems.babyrobot.support.model.BattleAuditor;
import com.learningsystems.babyrobot.support.model.DimensionReducerQuery;
import com.learningsystems.babyrobot.support.predictor.NeuralNetworkPredictor;
import com.learningsystems.babyrobot.support.util.Constants;

//TODO: This class needs design cleanup
public class DimensionalNeuralBabyRobot extends NeuralBabyRobot {


    private NeuralNetworkPredictor.ExplodedInput input = new NeuralNetworkPredictor.ExplodedInput(null);//TODO: Ugh!

    @Override
    public void run() {
        logger = new Logger(getDataFile("Log.log"));
        onStartOfNewBattle(() -> {
            battleAuditor = new BattleAuditor(getNumRounds(), Constants.BATTLE_AUDIT_BATCH_PERCENTAGE);
        });
        initRobot();
        while (true) {
            options = getOptions(Constants.POLICY.OFF_POLICY, Constants.REWARD_POLICY.INTERMEDIATE, 50);
            setTurnRadarRight(360);
            String stateAsString = getState();
            Constants.ACTION action = selectAction(stateAsString, options.getMovePolicy());
            action.perform(this, enemy);
            logger.log(action.name() + " - " + stateAsString);
            execute();

        }
    }

    public Constants.ACTION selectAction(String stateAsString, Constants.MOVE_POLICY option) {
        return option.equals(Constants.MOVE_POLICY.GREEDY) ? getBestAction(stateAsString) : Constants.ACTION.random();
    }

    private Constants.ACTION getBestAction(String stateAsString) {
        //Best action is the one that has the maximum Q-value
        double qValue = Double.NEGATIVE_INFINITY;
        Constants.ACTION action = null;
        for (Constants.ACTION currentAction : Constants.ACTION.values()) {
            double currentQValue = getQValue(stateAsString, currentAction);
            if (currentQValue > qValue) {
                qValue = currentQValue;
                action = currentAction;
            }
        }
        return action;
    }

    private String getState() {
        DimensionReducerQuery.Builder builder = new DimensionReducerQuery.Builder();
        DimensionReducerQuery dimensionReducerQuery = builder
                .withSelfBearing(getHeading())
                .withXPosition(getX())
                .withYPosition(getY())
                .withEnemyDistance(enemy.getDistance())
                .withEnemyBearing(enemy.getBearing())
                .build();

        return dimensionReducerQuery.toString();
    }

    private double getQValue(String stateAsString, Constants.ACTION action) {
        double[] doubles = input.network().computeOutputs(input.normalize(action, stateAsString.split(",")));
        return doubles[0];
    }

}
