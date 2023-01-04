package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.io.File;

public class Rejuvenation extends EventExecutor {
    public Rejuvenation(File file) {
        super(file);
    }

    @Override
    public void regainHealth(int level, EntityRegainHealthEvent event) {
        event.setAmount(event.getAmount() * getValue("heal-multiplier", level));
    }
}
