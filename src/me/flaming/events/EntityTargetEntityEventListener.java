package me.flaming.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityTargetEntityEventListener implements Listener {
    @EventHandler
    public void onEntityTargetEntity(EntityTargetLivingEntityEvent e) {
        // This can be used to disable certain vanilla functions (Like zombies targetting villagers etc)
    }
}
