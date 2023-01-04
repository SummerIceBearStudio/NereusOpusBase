package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Map;

public class Grit extends EventExecutor {
    public Grit(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof LivingEntity creature) {
            Map<EquipmentSlot, ItemStack> equipped = InventoryUtils.getEquippedItems(creature);
            for (EquipmentSlot slot : equipped.keySet()) {
                creature.getEquipment().setItem(slot, ItemUtils.addDurability(equipped.get(slot), getValue("durability-decrease", level)));
            }
        }
    }
}
