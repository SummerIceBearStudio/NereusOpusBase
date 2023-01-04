package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Sycophant extends EventExecutor {
    public Sycophant(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isBlocking()) {
                player.setHealth(Math.min(player.getHealth() + event.getDamage() * getValue("percent", level) / 100.0, player.getMaxHealth()));
                player.setCooldown(Material.SHIELD, (int) (getValue("cooldown", level) * 20));
            }
        }
    }
}
