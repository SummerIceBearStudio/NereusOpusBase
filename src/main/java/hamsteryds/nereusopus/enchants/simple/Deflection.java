package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Deflection extends EventExecutor {
    public Deflection(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player &&
                event.getDamager() instanceof LivingEntity) {
            LivingEntity creature = (LivingEntity) event.getEntity();
            if (player.isBlocking()) {
                if (!PermissionUtils.hasDamagePermission(player, creature)) {
                    return;
                }
                creature.damage(event.getDamage() * getValue("percent", level) / 100.0);
                player.setCooldown(Material.SHIELD, (int) (getValue("cooldown", level) * 20));
            }
        }
    }
}
