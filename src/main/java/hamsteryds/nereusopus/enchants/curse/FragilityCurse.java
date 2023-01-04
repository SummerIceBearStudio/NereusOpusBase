package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.io.File;

public class FragilityCurse extends EventExecutor {
    public FragilityCurse(File file) {
        super(file);
    }

    @Override
    public void itemDamage(int level, PlayerItemDamageEvent event) {
        event.setDamage((int) (event.getDamage() + getValue("durability-decrease-extra", level)));
    }
}
