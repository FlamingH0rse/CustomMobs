package me.flaming;

import me.flaming.classes.CustomEntity;
import me.flaming.classes.EntityInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
        if(!(entity instanceof LivingEntity)) {
            entity.remove();
            return colorStr("&cType needs to be a living entity");
        }
        LivingEntity living = (LivingEntity) entity;
        NamespacedKey key = new NamespacedKey(getPlugin(), "JDnD-weDJ-KDe-DSaw");

        // General Information about the mob
        living.getPersistentDataContainer().set(key, PersistentDataType.STRING, mobInternalName);
        living.setCustomName(mob.getDisplayName());

        // Stats
        living.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mob.getHealth());
        living.setHealth(mob.getHealth());
        living.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mob.getDamage());
        living.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(mob.getSpeed());

        if (mob.getInv() != null) {
            // lol
            UnsafeValues unsafe = Bukkit.getUnsafe();
            EntityInventory mobInv = mob.getInv();
            // Currently the code is very repetitive

            // Create initial ItemStack
            ItemStack mainHand = new ItemStack(mob.getInv().getMainHand().getItem().getKey());
            ItemStack leftHand = new ItemStack(mob.getInv().getLeftHand().getItem().getKey());
            ItemStack head = new ItemStack(mob.getInv().getHead().getItem().getKey());
            ItemStack body = new ItemStack(mob.getInv().getBody().getItem().getKey());
            ItemStack legs = new ItemStack(mob.getInv().getLegs().getItem().getKey());
            ItemStack foot = new ItemStack(mob.getInv().getFoot().getItem().getKey());

            // Then set their ItemMeta (Still it is unsure if this will work)
            mainHand = unsafe.modifyItemStack(mainHand, mobInv.getMainHand().getItem().getValue());
            leftHand = unsafe.modifyItemStack(leftHand, mobInv.getLeftHand().getItem().getValue());
            head = unsafe.modifyItemStack(head, mobInv.getHead().getItem().getValue());
            body = unsafe.modifyItemStack(body, mobInv.getBody().getItem().getValue());
            legs = unsafe.modifyItemStack(legs, mobInv.getLegs().getItem().getValue());
            foot = unsafe.modifyItemStack(foot, mobInv.getFoot().getItem().getValue());

            ItemStack[] armorContents = {head, body, legs, foot};
            living.getEquipment().setArmorContents(armorContents);
            living.getEquipment().setItemInMainHand(mainHand);
            living.getEquipment().setItemInOffHand(leftHand);
        }

        return colorStr("&aSuccessfully spawned the mob!");
    }
}
