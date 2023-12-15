package me.flaming.classes;

public class EntityInventory {
    private final CustomMobItem mainHand;
    private final CustomMobItem leftHand;
    private final CustomMobItem head;
    private final CustomMobItem body;
    private final CustomMobItem legs;
    private final CustomMobItem foot;

    private EntityInventory(InventoryBuilder builder) {
        this.mainHand = builder.mainHand;
        this.leftHand = builder.leftHand;
        this.head = builder.head;
        this.body = builder.body;
        this.legs = builder.legs;
        this.foot = builder.foot;
    }

    public CustomMobItem getMainHand() {
        return this.mainHand;
    }
    public CustomMobItem getLeftHand() {
        return this.leftHand;
    }
    public CustomMobItem getHead() {
        return this.head;
    }
    public CustomMobItem getBody() {
        return this.body;
    }
    public CustomMobItem getLegs() {
        return this.legs;
    }
    public CustomMobItem getBoots() {
        return this.foot;
    }

    public static class InventoryBuilder {
        private CustomMobItem mainHand;
        private CustomMobItem leftHand;
        private CustomMobItem head;
        private CustomMobItem body;
        private CustomMobItem legs;
        private CustomMobItem foot;

        public static InventoryBuilder newInventory() {
            return new InventoryBuilder();
        }

        public InventoryBuilder setMainHand(CustomMobItem input) {
            this.mainHand = input;
            return this;
        }
        public InventoryBuilder setLeftHand(CustomMobItem input) {
            this.leftHand = input;
            return this;
        }
        public InventoryBuilder setHead(CustomMobItem input) {
            this.head = input;
            return this;
        }
        public InventoryBuilder setBody(CustomMobItem input) {
            this.body = input;
            return this;
        }
        public InventoryBuilder setLegs(CustomMobItem input) {
            this.legs = input;
            return this;
        }
        public InventoryBuilder setFoot(CustomMobItem input) {
            this.foot = input;
            return this;
        }

        public EntityInventory build() {
            return new EntityInventory(this);
        }
    }
}
