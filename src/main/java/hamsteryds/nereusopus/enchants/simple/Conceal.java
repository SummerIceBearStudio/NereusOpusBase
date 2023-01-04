package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.io.File;

public class Conceal extends EventExecutor {
    public Conceal(File file) {
        super(file);
    }

    @Override
    public void beTargeted(int level, EntityTargetLivingEntityEvent event) {
        Entity entity = event.getEntity();
        if (event.getReason()== EntityTargetEvent.TargetReason.CLOSEST_ENTITY) {
            try{
                double distance=entity.getLocation().distance(event.getTarget().getLocation());
                double range=getValue("range",level);
                if(distance>=range)
                    event.setCancelled(true);
            }catch (Exception exception){
//                exception.printStackTrace();
            }
        }
    }
}
