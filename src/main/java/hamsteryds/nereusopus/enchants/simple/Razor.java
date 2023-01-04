package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Razor extends EventExecutor {
    public Razor(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        double multiplier = getValue("damage-multiplier", level);
        if (getBool("decrease-if-cooldown") && event.getDamager() instanceof Player player) {
            multiplier = 1 + (multiplier - 1) * player.getAttackCooldown();
        }
        if (getBool("disable-on-players") && event.getEntity() instanceof Player) {
            return;
        }
        event.setDamage(event.getDamage() * multiplier);
    }
}
