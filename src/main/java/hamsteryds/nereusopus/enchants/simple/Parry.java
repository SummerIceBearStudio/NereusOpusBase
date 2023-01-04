package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

public class Parry extends EventExecutor {
    public Parry(File file) {
        super(file);
    }

    @Override
    public void damaged(int level, EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            if (creature.getEquipment() != null) {
                if (creature.getEquipment().getItem(EquipmentSlot.HAND)
                        .getType().toString().contains("SWORD")) {
                    event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
                }
            }
        }
    }
}
