import me.flaming.classes.CustomEntity;
import org.bukkit.scheduler.BukkitRunnable;
import static me.flaming.EntityLoader.getLoadedMobs;

public class EntitySpawner extends BukkitRunnable {
    @Override
    public void run() {
        for (CustomEntity mob : getLoadedMobs().values()) {
            if (mob.getSpawnLocation().getProperty().isEnabled()) {



            }
        }
    }
}
