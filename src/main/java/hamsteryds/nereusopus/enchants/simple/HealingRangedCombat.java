package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;

public class HealingRangedCombat extends EventExecutor {
    public HealingRangedCombat(File file) {
        super(file);
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        double heal=getValue("heal",level);
        Entity entity=event.getEntity();
        if(entity instanceof LivingEntity creature){
            event.setDamage(0);
            creature.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 3, 1));
            creature.setHealth(Math.min(creature.getHealth()+heal,creature.getMaxHealth()));
        }
    }
}
