package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class Disappear extends EventExecutor {
    public Disappear(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        if (player.getHealth() / player.getMaxHealth() <= getValue("min-health-percent", level)) {
            player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100, 0));
        }
    }
}
