package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Shields extends EventExecutor {
    public static HashMap<UUID,Long> stamps=new HashMap<>();
    public Shields(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        Entity entity=event.getEntity();
        if(entity instanceof LivingEntity) {
            double damageMultiplier=getValue("damage-multiplier",level);
            double duration=getValue("duration",level);
            if (stamps.containsKey(entity.getUniqueId())) {
                if (System.currentTimeMillis() - stamps.get(entity.getUniqueId()) >=duration*50) {
                    event.setDamage(event.getDamage()*damageMultiplier);
                }
            }
            stamps.put(entity.getUniqueId(),System.currentTimeMillis());
        }
    }
}
