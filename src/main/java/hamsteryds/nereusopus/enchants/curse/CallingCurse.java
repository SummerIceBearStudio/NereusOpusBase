package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

public class CallingCurse extends EventExecutor {
    public CallingCurse(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        double range = getValue("range", level);
        if (stamp % getValue("repeat-ticks", level) == 0) {
            for (Entity entity : player.getNearbyEntities(range, range, range)) {
                if (entity instanceof Mob mob) {
                    mob.setTarget(player);
                }
            }
        }
    }
}
