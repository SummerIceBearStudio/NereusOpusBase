package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.enchants.internal.data.CustomTarget;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

public class Chopless extends EventExecutor {
    public Chopless(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity creature) {
            if (creature.getEquipment() != null) {
                if (CustomTarget.fromId("axes").containsType(creature.getEquipment().getItem(EquipmentSlot.HAND).getType())) {
                    event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
                }
            }
        }
    }
}
