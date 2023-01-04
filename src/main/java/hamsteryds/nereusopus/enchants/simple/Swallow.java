package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Swallow extends EventExecutor {
    public Swallow(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        int food= (int) getValue("food",level);
        if(event.getDamager() instanceof Player player){
            player.setFoodLevel(player.getFoodLevel()+food);
        }
    }
}
