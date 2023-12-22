package me.flaming.classes;

import org.bukkit.util.Vector;

public class SpawnLocation {
    private final Vector pos1;
    private final Vector pos2;
    private final SpawnProperty property;

    public SpawnLocation(Vector input1, Vector input2, SpawnProperty input3) {
        this.pos1 = input1;
        this.pos2 = input2;
        this.property = input3;
    }

    public Vector getPos1() {
        return this.pos1;
    }
    public Vector getPos2() {
        return this.pos2;
    }
    public SpawnProperty getProperty() {
        return this.property;
    }
}
