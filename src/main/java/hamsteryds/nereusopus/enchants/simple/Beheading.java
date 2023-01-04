package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Map;

public class Beheading extends EventExecutor {
    public Map<EntityType, String> heads;

    public Beheading(File file) {
        super(file);
        heads = ConfigUtils.getMapFromList(config.getConfigurationSection("params"),
                "custom-heads",
                ":",
                EntityType.class,
                String.class);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        EntityType type = event.getEntity().getType();
        if (event.getEntity() instanceof LivingEntity creature) {
            if (event.getDamage() >= creature.getHealth()) {
                ItemStack item = type == EntityType.WITHER_SKELETON ?
                        ItemUtils.make(Material.WITHER_SKELETON_SKULL, "") :
                        type == EntityType.ZOMBIE ? ItemUtils.make(Material.ZOMBIE_HEAD, "") :
                        type == EntityType.SKELETON ? ItemUtils.make(Material.SKELETON_SKULL, "") :
                        type == EntityType.CREEPER ? ItemUtils.make(Material.CREEPER_HEAD, "") :
                        heads.containsKey(type) ? ItemUtils.setSkull(ItemUtils.make(Material.PLAYER_HEAD, "§b头颅"), heads.get(type)) : null;
                if (item != null) {
                    creature.getWorld().dropItem(creature.getLocation(), item);
                }
            }
        }
    }
}
