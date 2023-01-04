package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Cranial extends EventExecutor implements Listener {
    public Cranial(File file) {
        super(file);
        Bukkit.getPluginManager().registerEvents(this, NereusOpus.plugin);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            Entity trident = event.getDamager();
            if (trident.getLocation().getY() < creature.getLocation().getY() + creature.getEyeHeight() - 0.22) {
                return;
            }
            event.setDamage(event.getDamage() * getValue("damage-multiplier", level));
        }
    }
}
