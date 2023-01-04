package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.block.BlockDamageEvent;

import java.io.File;

public class BreaklessnessCurse extends EventExecutor {
    public BreaklessnessCurse(File file) {
        super(file);
    }

    @Override
    public void blockDamage(int level, BlockDamageEvent event) {
        event.setCancelled(true);
    }
}
