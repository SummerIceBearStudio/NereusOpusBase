package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;

public class Voltage extends EventExecutor {
    public Voltage(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            EntityEquipment equip = creature.getEquipment();
            int amount = 0;
            if (equip != null) {
                for (EquipmentSlot slot : InventoryUtils.ARMORS) {
                    Material type = equip.getItem(slot).getType();
                    if (type.toString().contains("IRON") ||
                            type.toString().contains("GOLD")) {
                        amount++;
                    }
                }
            }
            event.setDamage(event.getDamage() * MathUtils.calculate(getText("damage-multiplier"), "level", level, "amount", amount));
        }
    }
}
