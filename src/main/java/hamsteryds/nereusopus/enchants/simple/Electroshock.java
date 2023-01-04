package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.PermissionUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Electroshock extends EventExecutor {
    public Electroshock(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof LivingEntity creature) {
            if (player.isBlocking()) {
                if (!PermissionUtils.hasDamagePermission(player, creature)) {
                    return;
                }
                creature.getWorld().strikeLightningEffect(creature.getLocation());
                creature.damage(getValue("damage", level));
            }
        }
    }
}
