package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.io.File;

public class Hook extends EventExecutor {
    public Hook(File file) {
        super(file);
    }

    @Override
    public void fish(int level, PlayerFishEvent event) {
        FishHook hook = event.getHook();
        if (hook.getHookedEntity() instanceof LivingEntity creature) {
            Vector direction = event.getPlayer().getLocation().subtract(creature.getLocation()).toVector().normalize();
            direction = direction.multiply(getValue("velocity", level));
            WorldUtils.addVelocity(creature, direction, true);
        }
    }
}
