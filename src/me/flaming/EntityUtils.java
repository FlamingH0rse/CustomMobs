package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.CustomMobItem;
import me.flaming.classes.EntityInventory;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import static me.flaming.CustomMobsCore.getPlugin;
import static me.flaming.utils.utils.colorStr;

public class EntityUtils {
    // You can also ignore the value this returns which is fine
    public static String SpawnMob(@NotNull Location location, @NotNull String mobInternalName) {
        CustomEntity mob = EntitySpawnLogic.GetMobs().get(mobInternalName);

        if (mob == null) {
            return colorStr("&cThe specified mob does not exist in the current context");
        }

        Entity entity = location.getWorld().spawnEntity(location, mob.getType());
        // I don't think this will ever happen as it will be handled internally anyways
        if (!(entity instanceof LivingEntity)) {
            entity.remove();
            return colorStr("&cType needs to be a living entity");
        }
        LivingEntity living = (LivingEntity) entity;
        NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");

        // General Information about the mob
        living.getPersistentDataContainer().set(key, PersistentDataType.STRING, mobInternalName);
        living.setCustomName(colorStr(mob.getDisplayName()));

        // Stats
        living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mob.getHealth());
        living.setHealth(mob.getHealth());
        living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mob.getDamage());
        living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mob.getSpeed());

        getPlugin().getLogger().info("im here");
        if (mob.getInv() != null) {
            getPlugin().getLogger().info("Mob has Inv setting it now!");
            // lol
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

            // Extra properties (may be added in config.yml) alongside with defaultAI (like zombies chasing villagers)
            if (living instanceof Ageable) {
                ((Ageable) living).setAdult();
            }
        }

        return colorStr("&aSuccessfully spawned the mob!");
    }

    public static void customMobDrop() {

    }

}
