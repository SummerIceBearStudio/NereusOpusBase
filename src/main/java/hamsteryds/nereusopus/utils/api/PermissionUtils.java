package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

/**
 * <p>权限工具类</p>
 *
 */
public class PermissionUtils implements Listener {
    /**
     * 是否有伤害权限
     *
     * @param damager 伤害来源
     * @param entity  被伤害实体
     * @return boolean - 是否有伤害权限
     */
    public static boolean hasDamagePermission(Entity damager, Entity entity) {
        if (neverCheck.contains(entity.getType())) return false;
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, entity, EntityDamageEvent.DamageCause.CUSTOM, 0.1);
        CheckListeners.addCheckEvent(event);
        Bukkit.getPluginManager().callEvent(event);
        return !CheckListeners.events.contains(event);
    }

    public static List<EntityType> neverCheck = List.of(
            EntityType.ITEM_FRAME,
            EntityType.GLOW_ITEM_FRAME,
            EntityType.DROPPED_ITEM,
            EntityType.DRAGON_FIREBALL,
            EntityType.WITHER_SKULL,
            EntityType.ARMOR_STAND
    );
}
