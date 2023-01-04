package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Weakening extends EventExecutor {
    public static HashMap<UUID, Long> lastAttackStamp = new HashMap<>();

    public Weakening(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LivingEntity creature) {
            if (lastAttackStamp.containsKey(creature.getUniqueId())) {
                long stamp = lastAttackStamp.get(creature.getUniqueId());
                if (System.currentTimeMillis() - stamp <= getValue("duration", level) * 50) {
                    event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
                }
            }
            lastAttackStamp.put(creature.getUniqueId(), System.currentTimeMillis());
        }
    }
}
