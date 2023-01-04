package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Extract extends EventExecutor {
    public Extract(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity creature) {
            double damage = event.getDamage();
            double heal = MathUtils.calculate(getText("heal"), "level", level, "damage", damage);
            creature.setHealth(Math.min(creature.getHealth() + heal, creature.getMaxHealth()));
        }
        if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof LivingEntity creature) {
                double damage = event.getDamage();
                double heal = MathUtils.calculate(getText("heal"), "level", level + "", "damage", damage + "");
                creature.setHealth(Math.min(creature.getHealth() + heal, creature.getMaxHealth()));
            }
        }
    }
}
