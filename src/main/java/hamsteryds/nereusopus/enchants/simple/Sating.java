package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.io.File;

public class Sating extends EventExecutor {
    public Sating(File file) {
        super(file);
    }

    @Override
    public void hunger(int level, FoodLevelChangeEvent event) {
        if (event.getFoodLevel() < event.getEntity().getFoodLevel()) {
            event.setCancelled(true);
        }
    }
}
