package com.learningsystems.babyrobot.support.model;

public class BattleGroundDimension {

    private final int x;
    private final int y;

    public BattleGroundDimension(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getQuantizedY() {
        return y / 100;
    }

    int getQuantizedX() {
        return x / 100;
    }
}
