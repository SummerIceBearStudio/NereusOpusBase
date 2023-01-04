package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.io.File;

public class Stamina extends EventExecutor {
    public Stamina(File file) {
        super(file);
    }

    @Override
    public void hunger(int level, FoodLevelChangeEvent event) {
        if (event.getFoodLevel() < event.getEntity().getFoodLevel() && event.getEntity() instanceof Player player) {
            if (player.isSprinting()) {
                event.setCancelled(true);
            }
        }
    }
}
