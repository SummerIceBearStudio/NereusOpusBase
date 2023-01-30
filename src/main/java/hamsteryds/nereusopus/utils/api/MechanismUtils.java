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

/**
 * 机制工具类
 */
public class MechanismUtils {
    /**
     * 玩家冷却时间戳
     */
    public static final HashMap<UUID, HashMap<String, Long>> usedStamps = new HashMap<>();
    /**
     * <p>方块权限缓存</p>
     * <ul>
     *     <li>{@link UUID} - 玩家UUID</li>
     *     <li>{@link String} - 坐标</li>
     *     <li>{@link Boolean} - 是否有权限</li>
     * </ul>
     */
    public static HashMap<UUID, HashMap<String, Boolean>> cache = new HashMap<>();

    /**
     * 检查技能冷却
     *
     * @param player 玩家
     * @param key    技能名字
     * @param cd     技能cd
     * @param info   是否向玩家发送冷却信息
     * @return boolean 技能是否已冷却
     */
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

    /**
     * 添加技能冷却
     *
     * @param player 玩家
     * @param key    技能名字
     */
    public static void addCooldown(Player player, String key) {
        UUID uuid = player.getUniqueId();
        if (!usedStamps.containsKey(uuid)) {
            usedStamps.put(uuid, new HashMap<>());
        }
        usedStamps.get(uuid).put(key, System.currentTimeMillis());
    }

    /**
     * 判断对于有触发几率的附魔是否能触发
     *
     * @param enchant 附魔
     * @param level   附魔等级
     * @return boolean 是否能触发
     */
    public static boolean checkPercent(CustomEnchantment enchant, int level) {
        return Math.random() * 100 <= enchant.getValue("chance", level, 100 + "");
    }

    /**
     * 判断对于随攻击冷却的附魔是否能触发
     *
     * @param enchant 附魔
     * @param entity  触发附魔的实体
     * @return boolean 是否能触发
     */
    public static boolean checkRequireFull(CustomEnchantment enchant, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return true;
        }
        if (enchant.getBool("require-full-charge", false)) {
            return player.getAttackCooldown() >= 1.0;
        }
        return true;
    }

    /**
     * 检查方块权限
     *
     * @param enchant  附魔
     * @param block    方块
     * @param creature 触发附魔的生物
     * @return boolean 是否有权限
     */
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

    /**
     * 检查攻击者是否在掉落过程中
     *
     * @param attacker 攻击者
     * @return boolean 是否在掉落过程中
     */
    public static boolean checkCritical(Entity attacker) {
        return attacker.getFallDistance() > 0 && !attacker.isOnGround();
    }

    /**
     * 检查附魔下蹲时是否触发
     *
     * @param enchant 附魔
     * @param entity   实体
     * @return boolean 是否能触发
     */
    public static boolean checkShiftIgnored(CustomEnchantment enchant, LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return true;
        }
        if (enchant.getBool("shift-ignored", false)) {
            return !player.isSneaking();
        }
        return true;
    }
}
