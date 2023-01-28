package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent;

import java.io.File;

public class LuckyCatch extends EventExecutor {
    public LuckyCatch(File file) {
        super(file);
    }

    @Override
    public void fish(int level, PlayerFishEvent event) {
        Entity caught = event.getCaught();
        if (!(caught instanceof Item caughtItem)) {
            return;
        }
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        caughtItem.getItemStack().setAmount(caughtItem.getItemStack().getAmount() * 2);
    }
}
