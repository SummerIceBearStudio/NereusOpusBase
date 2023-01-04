package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.io.File;

public class Dexterous extends EventExecutor {
    public Dexterous(File file) {
        super(file);
    }

    @Override
    public void itemHeld(int level, PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4 * (1.0 + getValue("cooldown-decreased", level) / 100.0));
    }
}
