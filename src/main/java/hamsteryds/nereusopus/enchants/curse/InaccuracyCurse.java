package hamsteryds.nereusopus.enchants.curse;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.io.File;

public class InaccuracyCurse extends EventExecutor {
    public InaccuracyCurse(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        event.getEntity().setFireTicks(200);
    }
}
