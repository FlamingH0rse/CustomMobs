package me.flaming.classes;

public class SpawnProperty {
    long minInterval;
    long maxInterval;
    int minAmount;
    int maxAmount;
    int maxMob;
    Boolean enabled;

    public SpawnProperty(long input1, long input2, int input3, int input4, int input5, boolean input6) {
        this.minInterval = input1;
        this.maxInterval = input2;
        this.minAmount = input3;
        this.maxAmount = input4;
        this.maxMob = input5;
        this.enabled = input6;
    }

    public long getMinInterval() {
        return this.minInterval;
    }
    public long getMaxInterval() {
        return this.maxInterval;
    }
    public int getMinAmount() {
        return this.minAmount;
    }
    public int getMaxAmount() {
        return this.maxAmount;
    }
    public int getMaxMob() {
        return this.maxMob;
    }
    public boolean isEnabled() {
        return this.enabled;
    }
}
