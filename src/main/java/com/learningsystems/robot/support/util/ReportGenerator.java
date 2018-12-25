package com.learningsystems.robot.support.util;

import com.learningsystems.robot.support.model.BattleAuditor;
import robocode.RobocodeFileOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
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
