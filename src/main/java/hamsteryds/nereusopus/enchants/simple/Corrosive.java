package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

public class Corrosive extends EventExecutor {
    public Corrosive(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            EntityEquipment equip = creature.getEquipment();
            if (equip == null) {
                return;
            }
            for (EquipmentSlot slot : InventoryUtils.getEquippedItems(creature).keySet()) {
                equip.setItem(slot,
                        ItemUtils.addDurability(equip.getItem(slot),
                                (short) getValue("durability-decrease", level)));
            }
        }
    }
}
