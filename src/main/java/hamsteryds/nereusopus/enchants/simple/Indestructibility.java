package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.io.File;

public class Indestructibility extends EventExecutor {
    public Indestructibility(File file) {
        super(file);
    }

    @Override
    public void itemDamage(int level, PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }
}
