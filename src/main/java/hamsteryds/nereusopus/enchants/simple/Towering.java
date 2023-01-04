package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.io.File;

public class Towering extends EventExecutor {
    public Towering(File file) {
        super(file);
    }

    @Override
    public void damagedByEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity creature) {
            String text = getText("health-judging-line", "maxBlood", String.format("%.2f", creature.getMaxHealth()));
            double judgeHealth = MathUtils.calculate(text, "level", level);
            double highMultiplier = getValue("high-damage-multiplier", level);
            double lowMultiplier = getValue("low-damage-multiplier", level);
            if (creature.getHealth() > judgeHealth) {
                event.setDamage(event.getDamage() * highMultiplier);
            } else {
                event.setDamage(event.getDamage() * lowMultiplier);
            }
        }
    }
}
