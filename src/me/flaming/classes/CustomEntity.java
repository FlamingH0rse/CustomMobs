package me.flaming.classes;

import org.bukkit.entity.EntityType;

public class CustomEntity {
    private final String displayName;
    private final EntityType type;
    private final double health;
    private final double damage;
    private final double speed;
    private final EntityInventory entityInventory;

    private CustomEntity(EntityBuilder build) {
        this.displayName = build.displayName;
        this.type = build.type;
        this.health = build.health;
        this.damage = build.damage;
        this.speed = build.speed;
        this.entityInventory = build.entityInventory;
    }

    // Getters
    public String getDisplayName() {
        return this.displayName;
    }
    public EntityType getType() {
        return this.type;
    }
    public double getHealth() {
        return this.health;
    }
    public double getDamage() {
        return this.damage;
    }
    public double getSpeed() {
        return this.speed;
    }
    public EntityInventory getInv() {
        return this.entityInventory;
    }

    public static class EntityBuilder {
        private String displayName;
        private EntityType type;
        private double health;
        private double damage;
        private double speed;
        private EntityInventory entityInventory;

        public static EntityBuilder newEntity() {
            return new EntityBuilder();
        }

        public EntityBuilder setDisplayName(String input) {
            this.displayName = input;
            return this;
        }

        public EntityBuilder setEntityType(EntityType input) {
            this.type = input;
            return this;
        }

        public EntityBuilder setHealth(double input) {
            this.health = input;
            return this;
        }

        public EntityBuilder setDamage(double input) {
            this.damage = input;
            return this;
        }

        public EntityBuilder setSpeed(double input) {
            this.speed = input;
            return this;
        }

        public EntityBuilder setInventory(EntityInventory input) {
            this.entityInventory = input;
            return this;
        }

        public CustomEntity build() {
            return new CustomEntity(this);
        }
    }
}