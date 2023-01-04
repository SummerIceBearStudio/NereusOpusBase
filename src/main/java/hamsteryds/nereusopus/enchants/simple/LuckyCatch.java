package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerFishEvent;

import java.io.File;
import java.util.List;

public class LuckyCatch extends EventExecutor {
    public LuckyCatch(File file) {
        super(file);
    }

    public static List<Material> types=List.of(
            Material.SALMON,
            Material.TROPICAL_FISH,
            Material.COD,
            Material.PUFFERFISH
    );

    @Override
    public void fish(int level, PlayerFishEvent event) {
        Entity caught = event.getCaught();
        if (!(caught instanceof Item caughtItem)) {
            return;
        }
        if(types.contains(caughtItem.getItemStack().getType())) {
            caughtItem.getItemStack().setAmount(caughtItem.getItemStack().getAmount() * 2);
        }
    }
}
