package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerFishEvent;

import java.io.File;

public class Harpoon extends EventExecutor {
    public Harpoon(File file) {
        super(file);
    }

    @Override
    public void fish(int level, PlayerFishEvent event) {
        if (event.getCaught() instanceof LivingEntity creature) {
            if (!PermissionUtils.hasDamagePermission(event.getPlayer(), creature)) {
                return;
            }
            creature.damage(getValue("damage", level), event.getPlayer());
        }
    }
}
