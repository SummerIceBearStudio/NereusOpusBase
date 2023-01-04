package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.enchants.PublicTasks;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;

public class Slicing extends EventExecutor {
    public Slicing(File file) {
        super(file);
        PublicTasks.registerTaskEnchant(this, getClass());
    }

    public void run(Player player, int slot, int level) {
        if (slot == 38) {
            if (player.isGliding()) {
                double range = getValue("range", level);
                for (Entity entity : player.getNearbyEntities(range, range, range)) {
                    if (entity instanceof LivingEntity creature) {
                        if (!PermissionUtils.hasDamagePermission(player, creature)) {
                            return;
                        }
                        creature.damage(getValue("damage-splash", level));
                    }
                }
            }
        }
    }
}
