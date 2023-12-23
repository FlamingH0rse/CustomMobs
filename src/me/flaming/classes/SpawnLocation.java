package me.flaming.classes;

import org.bukkit.util.Vector;

public class SpawnLocation {
    private final Vector pos1;
    private final Vector pos2;
    private final String worldName;
    private final SpawnProperty property;

    public SpawnLocation(Vector input1, Vector input2, String input3, SpawnProperty input4) {
        this.pos1 = input1;
        this.pos2 = input2;
        this.worldName = input3;
        this.property = input4;
    }

    public Vector getPos1() {
        return this.pos1;
    }
    public Vector getPos2() {
        return this.pos2;
    }
    public String getWorldName() {
        return this.worldName;
    }
    public SpawnProperty getProperty() {
        return this.property;
    }
}
