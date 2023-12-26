package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.CustomMobItem;
import me.flaming.classes.EntityInventory;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.utils.utils.colorStr;
import static me.flaming.utils.utils.getRandomValue;
import static me.flaming.CustomMobsCore.getLoadedMobs;
import static me.flaming.CustomMobsCore.getLoadedWorlds;

public class EntityUtils {
    // You can also ignore the value this returns which is fine
    @NotNull
    public String spawnMob(@NotNull Location location, @NotNull String mobInternalName) {
        CustomEntity mob = getLoadedMobs().get(mobInternalName);

        if (mob == null) {
            return colorStr("&cThe specified mob does not exist in the current context");
        }

        if (location.getWorld() == null) {
            return colorStr("&cCurrent location is not in a valid world");
        }

        Entity entity = location.getWorld().spawnEntity(location, mob.getType());
        // I don't think this will ever happen as it will be handled internally anyways
        if (!(entity instanceof LivingEntity living)) {
            entity.remove();
            return colorStr("&cType needs to be a living entity");
        }
        NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");

        // Set general Information about the mob
        living.getPersistentDataContainer().set(key, PersistentDataType.STRING, mobInternalName);
        living.setCustomName(colorStr(mob.getDisplayName()));

        // set Entity attributes
        setEntityAttributes(mob, living);

        // Set extra properties (may be added in config.yml) alongside with defaultAI (like zombies chasing villagers)
        if (living instanceof Ageable) {
            ((Ageable) living).setAdult();
        }

        return colorStr("&aSuccessfully spawned the mob!");
    }

    // This will also return null if the entity provided is not a valid mob
    @Nullable
    public Entity getEntityReference(EntityType mobType) {
        World randomWorld = getRandomValue(getLoadedWorlds());
        Location location = new Location(randomWorld, 0, 0, 0);
        Entity entity = null;

        if (randomWorld != null && mobType.getEntityClass() != null) {
            try {
                entity = randomWorld.createEntity(location, mobType.getEntityClass());
            } catch (Exception ignored) {
                // Server version does not support createEntity (Possibly due to paper 1.20.2 not supporting it)
                // or a lower version than <1.20.2 has been used
                // A debug message may be added here later on to inform the server owner
                // that a workaround has been used instead
                entity = randomWorld.spawnEntity(location, mobType, false);
                entity.remove();
            }
        }
        return entity;
    }

    private void setEntityAttributes(@NotNull CustomEntity mob, @NotNull LivingEntity living) {
        AttributeInstance healthAttribute = living.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance attackDamageAttribute = living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeInstance movementSpeedAttribute = living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

        // Stats (hope its pass by reference)
        if (healthAttribute != null) {
            healthAttribute.setBaseValue(mob.getHealth());
            living.setHealth(mob.getHealth());
        }

        if (attackDamageAttribute != null) {
            attackDamageAttribute.setBaseValue(mob.getDamage());
        }

        if (movementSpeedAttribute != null) {
            movementSpeedAttribute.setBaseValue(mob.getSpeed());
        }

        if (mob.getInv() != null) {
            setEntityEquipment(mob, living);
        }
    }

    private void setEntityEquipment(@NotNull CustomEntity mob, @NotNull LivingEntity living) {
        UnsafeValues unsafe = Bukkit.getUnsafe();
        EntityInventory mobInv = mob.getInv();

        // Currently the code is very repetitive
        CustomMobItem mainHandItem = mobInv.getMainHand();
        CustomMobItem leftHandItem = mobInv.getLeftHand();
        CustomMobItem headItem = mobInv.getHead();
        CustomMobItem bodyItem = mobInv.getBody();
        CustomMobItem legsItem = mobInv.getLegs();
        CustomMobItem footItem = mobInv.getFoot();

        EntityEquipment entityEquipment = living.getEquipment();

        if (entityEquipment != null) {
            ItemStack mainHand = null;
            ItemStack leftHand = null;
            ItemStack head;
            ItemStack body;
            ItemStack legs;
            ItemStack foot;

            if (mainHandItem != null && mainHandItem.getItem() != null) {
                mainHand = new ItemStack(mainHandItem.getItem().getKey());
                mainHand = unsafe.modifyItemStack(mainHand, mainHandItem.getItem().getValue());
            }

            if (leftHandItem != null && leftHandItem.getItem() != null) {
                leftHand = new ItemStack(leftHandItem.getItem().getKey());
                leftHand = unsafe.modifyItemStack(leftHand, leftHandItem.getItem().getValue());
            }

            if (headItem != null && headItem.getItem() != null) {
                head = new ItemStack(headItem.getItem().getKey());
                head = unsafe.modifyItemStack(head, headItem.getItem().getValue());
                entityEquipment.setHelmet(head, false);
                entityEquipment.setHelmetDropChance(0);
            }

            if (bodyItem != null && bodyItem.getItem() != null) {
                body = new ItemStack(bodyItem.getItem().getKey());
                body = unsafe.modifyItemStack(body, bodyItem.getItem().getValue());
                entityEquipment.setChestplate(body, false);
                entityEquipment.setChestplateDropChance(0);
            }

            if (legsItem != null && legsItem.getItem() != null) {
                legs = new ItemStack(legsItem.getItem().getKey());
                legs = unsafe.modifyItemStack(legs, legsItem.getItem().getValue());
                entityEquipment.setLeggings(legs, false);
                entityEquipment.setLeggingsDropChance(0);
            }

            if (footItem != null && footItem.getItem() != null) {
                foot = new ItemStack(footItem.getItem().getKey());
                foot = unsafe.modifyItemStack(foot, footItem.getItem().getValue());
                entityEquipment.setBoots(foot, false);
                entityEquipment.setBootsDropChance(0);
            }

            entityEquipment.setItemInMainHand(mainHand);
            entityEquipment.setItemInOffHand(leftHand);
        }
    }
}
