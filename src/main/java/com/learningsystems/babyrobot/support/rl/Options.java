package com.learningsystems.babyrobot.support.rl;

import com.learningsystems.babyrobot.support.util.Constants;
import com.learningsystems.babyrobot.support.util.Constants.MOVE_POLICY;

public class Options {

    private static final Options singleton = new Options();
    private Constants.REWARD_POLICY rewardPolicy;
    private MOVE_POLICY movePolicy;
    private Constants.POLICY policy;

    public static Options update(Constants.REWARD_POLICY rewardPolicy, MOVE_POLICY movePolicy, Constants.POLICY policy) {
        singleton.rewardPolicy = rewardPolicy;
        singleton.movePolicy = movePolicy;
        singleton.policy = policy;
        return singleton;
    }

    public boolean intermediateRewardsAllowed() {
        return rewardPolicy.equals(Constants.REWARD_POLICY.INTERMEDIATE);
    }

    public MOVE_POLICY getMovePolicy() {
        return movePolicy;
    }

    public Constants.POLICY getPolicy() {
        return policy;
    }

}
