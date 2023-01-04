package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.UUID;

public class MechanismUtils {
    public static final HashMap<UUID, HashMap<String, Long>> usedStamps = new HashMap<>();
    public static HashMap<UUID, HashMap<String, Boolean>> cache = new HashMap<>();

    public static boolean checkCooldown(Player player, String key, double cd, boolean info) {
        UUID uuid = player.getUniqueId();
        double rate = 1.0;
        for (String permission : SkillEnchantment.rates.keySet()) {
            if (player.hasPermission(permission)) {
                rate = Math.min(rate, SkillEnchantment.rates.get(permission));
            }
        }
        if (usedStamps.containsKey(uuid)) {
            if (usedStamps.get(uuid).containsKey(key)) {
                long dist = System.currentTimeMillis() - usedStamps.get(uuid).get(key) - (long) (cd * rate * 1000L);
                if (dist >= 0) {
                    return true;
                } else {
                    if (info) {
                        DebugUtils.debug("发送冷却信息",player);
                        String message = SkillEnchantment.cdMessage.replace("{dist}", -dist / 1000 + "").replace("{key}", key);
                        if (SkillEnchantment.cdForm.equals("actionbar")) {
                            InformationUtils.sendActionBar(player, message);
                        } else if (SkillEnchantment.cdForm.equals("message")) {
                            InformationUtils.sendMsg(player, message);
                        } else if (SkillEnchantment.cdForm.equals("title")) {
                            InformationUtils.sendTitle(player, message, "");
                        } else if (SkillEnchantment.cdForm.equals("subtitle")) {
                            InformationUtils.sendTitle(player, "", message);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void addCooldown(Player player, String key) {
        UUID uuid = player.getUniqueId();
        if (!usedStamps.containsKey(uuid)) {
            usedStamps.put(uuid, new HashMap<>());
        }
        usedStamps.get(uuid).put(key, System.currentTimeMillis());
    }

    public static boolean checkPercent(CustomEnchantment enchant, int level) {
        return Math.random() * 100 <= enchant.getValue("chance", level, 100 + "");
    }

    public static boolean checkRequireFull(CustomEnchantment enchant, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return true;
        }
        if (enchant.getBool("require-full-charge", false)) {
            return player.getAttackCooldown() >= 1.0;
        }
        return true;
    }

    public static boolean checkPermission(CustomEnchantment enchant, Block block, LivingEntity creature, boolean... flag) {
        if (creature instanceof Player player) {
            if (enchant.getBool("permission-check", true)) {

                Location loc = block.getLocation();
                String cachedBlock = loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
                if (!cache.containsKey(player.getUniqueId())) {
                    cache.put(player.getUniqueId(), new HashMap<>());
                } else {
                    if (cache.get(player.getUniqueId()).getOrDefault(cachedBlock, false)) {
                        return true;
                    }
                }

                BlockBreakEvent evt = new BlockBreakEvent(block, player);
                CheckListeners.addCheckEvent(evt);
                Bukkit.getPluginManager().callEvent(evt);
                boolean result = !CheckListeners.events.contains(evt);
                cache.get(player.getUniqueId()).put(cachedBlock, result);
                return result;
            }
        }
        return true;
    }

    public static boolean checkCritical(Entity attacker) {
        return attacker.getFallDistance() > 0 && !attacker.isOnGround();
    }

    public static boolean checkShiftIgnored(EventExecutor executor, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return true;
        }
        if (executor.getBool("shift-ignored", false)) {
            return !player.isSneaking();
        }
        return true;
    }
}
