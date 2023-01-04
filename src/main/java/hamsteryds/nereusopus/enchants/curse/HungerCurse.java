package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

public class HungerCurse extends EventExecutor {
    public HungerCurse(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        if (stamp % (int) getValue("repeat-ticks") == 0) {
            player.setFoodLevel(Math.max(player.getFoodLevel() - 1, 0));
        }
    }
}
