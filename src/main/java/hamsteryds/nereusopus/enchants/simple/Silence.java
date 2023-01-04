package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class Silence extends EventExecutor {
    public static HashMap<UUID, Long> stamps = new HashMap<>();

    public Silence(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            stamps.put(player.getUniqueId(), (long) (System.currentTimeMillis() + getValue("duration", level) * 50));
            player.sendTitle("§7", "§e您被对方沉默了！");
        }
    }
}
