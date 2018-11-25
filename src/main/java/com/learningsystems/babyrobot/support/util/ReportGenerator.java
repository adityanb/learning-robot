package com.learningsystems.babyrobot.support.util;

import com.learningsystems.babyrobot.support.model.BattleAuditor;
import robocode.RobocodeFileOutputStream;

import java.io.*;
import java.util.Map;

public class ReportGenerator {
    public void generate(BattleAuditor battleAuditor, File dataFile) {
        try (PrintStream writer = new PrintStream(new RobocodeFileOutputStream(dataFile))) {
            Map<Integer, Double> battleAuditorResult = battleAuditor.getResult();
            for (Map.Entry<Integer, Double> entry : battleAuditorResult.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
