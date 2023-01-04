package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Collection;

public class FlashBack extends SkillEnchantment {
    public FlashBack(File file) {
        super(file);
    }

    @Override
    public void run(Player player, int level) {
        Location loc = player.getLocation();
        double health = player.getHealth();
        int foodLevel = player.getFoodLevel();
        Collection<PotionEffect> buffs = player.getActivePotionEffects();
        int duration = (int) getValue("duration", level);
        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    return;
                }
                player.teleport(loc);
                player.setHealth(health);
                player.setFoodLevel(foodLevel);
                player.getActivePotionEffects().forEach(
                        (PotionEffect effect) -> player.removePotionEffect(effect.getType()));
                player.addPotionEffects(buffs);
                player.sendTitle("§7", "§a回溯成功！");
            }
        }.runTaskLater(NereusOpus.plugin, duration);
    }
}
