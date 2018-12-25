package com.learningsystems.robot.support.model;

import java.util.HashMap;
import java.util.Map;

public class BattleAuditor {
    private int numRoundsInBattle;
    private int battleAuditBatch;
    private int numberOfWins = 0;
    private int numberOfLosses = 0;
    private int runningCountOfRounds = 0;
    private Map<Integer, Double> result = new HashMap<>();

    public BattleAuditor(int numRoundsInBattle, int battleAuditBatchPercentage) {
        this.numRoundsInBattle = numRoundsInBattle;
        this.battleAuditBatch = Math.max(battleAuditBatchPercentage * numRoundsInBattle / 100, 1);
    }

    public void recordWin() {
        numberOfWins++;
        runningCountOfRounds++;
        saveBatch();
    }

    public void recordLoss() {
        numberOfLosses++;
        runningCountOfRounds++;
        saveBatch();
    }

    public Map<Integer, Double> getResult() {
        return result;
    }

    private void saveBatch() {
        if (runningCountOfRounds % battleAuditBatch == 0) {
            result.put(runningCountOfRounds, 100 * ((double) numberOfWins / battleAuditBatch));
            numberOfWins = 0;
            numberOfLosses = 0;
        }
    }
}
