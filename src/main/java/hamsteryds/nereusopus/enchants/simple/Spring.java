package hamsteryds.nereusopus.enchants.simple;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;

public class Spring extends EventExecutor {
    public Spring(File file) {
        super(file);
    }

    @Override
    public void entityJump(int level, EntityJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Vector directionXZ = entity.getEyeLocation().getDirection().normalize();
        Vector directionY = new Vector(0, 1, 0);
        WorldUtils.addVelocity(entity,directionXZ.multiply(getValue("velocity-xz", level))
                .add(directionY.multiply(getValue("velocity-y", level))),false);
    }

    @Override
    public void playerJump(int level, PlayerJumpEvent event) {
        Player player = event.getPlayer();
        Vector directionXZ = player.getEyeLocation().getDirection().normalize();
        Vector directionY = new Vector(0, 1, 0);
        WorldUtils.addVelocity(player,directionXZ.multiply(getValue("velocity-xz", level))
                .add(directionY.multiply(getValue("velocity-y", level))),false);
    }
}
