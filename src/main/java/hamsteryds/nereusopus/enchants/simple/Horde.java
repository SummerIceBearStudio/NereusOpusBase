package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Horde extends EventExecutor {
    public Horde(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        double range = getValue("range", level);
        int amount = 0;
        for(Entity entity:event.getEntity().getNearbyEntities(range, range, range)){
            if(entity instanceof LivingEntity && !(entity instanceof ArmorStand)){
                amount++;
            }
        }
        event.setDamage(event.getDamage() * MathUtils.calculate(getText("damage-multiplier"), "level", level, "amount", amount));
    }
}
