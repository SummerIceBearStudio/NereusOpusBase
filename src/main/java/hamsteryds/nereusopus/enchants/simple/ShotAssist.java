package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.NBTUtils;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class ShotAssist extends EventExecutor {
    public ShotAssist(File file) {
        super(file);
    }

    @Override
    public void shootBow(int level, EntityShootBowEvent event) {
        double damageMultiplier = getValue("damage-multiplier", level);
        PersistentDataContainer pdc = event.getProjectile().getPersistentDataContainer();
        double current = (NBTUtils.has("shot_assist", pdc, PersistentDataType.DOUBLE) ?
                NBTUtils.read("shot_assist", pdc, PersistentDataType.DOUBLE) : 1) * damageMultiplier;
        NBTUtils.write("shot_assist", pdc, PersistentDataType.DOUBLE, current);
    }
}
