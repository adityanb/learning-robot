package com.learningsystems.robot.support;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private File logFile;

    public Logger(File logFile) {
        this.logFile = logFile;
    }

    public void log(String s) {
        try (FileWriter fileWriter = new FileWriter(logFile, true)) {
            fileWriter.write(s);
            fileWriter.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
