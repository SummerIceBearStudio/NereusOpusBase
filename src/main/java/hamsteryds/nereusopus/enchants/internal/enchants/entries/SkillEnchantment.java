package hamsteryds.nereusopus.enchants.internal.enchants.entries;

import hamsteryds.nereusopus.enchants.simple.Silence;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.DebugUtils;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.io.File;
import java.util.HashMap;

public abstract class SkillEnchantment extends EventExecutor {
    public static boolean cdInfoEnabled;
    public static String cdForm;
    public static String cdMessage;
    public static String action;
    public static boolean shiftNeeded;
    public static boolean shiftIgnored;
    public static HashMap<String, Double> rates = new HashMap<>();

    static {
        YamlConfiguration config = ConfigUtils.autoUpdateConfigs("enchantments/", "skills.yml");
        cdInfoEnabled = config.getBoolean("cd-info.enable");
        cdForm = config.getString("cd-info.form");
        cdMessage = config.getString("cd-info.message");
        action=config.getString("trigger.action");
        shiftNeeded = config.getBoolean("trigger.shift-needed");
        shiftIgnored = config.getBoolean("trigger.shift-ignored");
        for (String line : config.getStringList("rate")) {
            rates.put(line.split(":")[0], Double.parseDouble(line.split(":")[1]));
        }
    }

    public SkillEnchantment(File file) {
        super(file);
    }

    @Override
    public void interactRight(int level, PlayerInteractEvent event) {
        trigger(level,event.getPlayer(),"RIGHT_CLICK");
    }

    @Override
    public void interactLeft(int level, PlayerInteractEvent event) {
        trigger(level,event.getPlayer(),"LEFT_CLICK");
    }

    @Override
    public void swapHandItems(int level, PlayerSwapHandItemsEvent event) {
        trigger(level,event.getPlayer(),"SWAP");
    }

    public void trigger(int level, Player player, String type){
        if(!type.contains(action)) {
            return;
        }
        if(!player.isSneaking() && shiftNeeded)
            return;
        if (player.isSneaking() && shiftIgnored) {
            return;
        }
        if (Silence.stamps.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis()) {
            player.sendTitle("§7", "§c无法释放主动技能附魔，您被短时间沉默了！");
            return;
        }
        DebugUtils.debug("检查冷却",player);
        if (MechanismUtils.checkCooldown(player, displayName, getValue("cooldown", level), true)) {
            DebugUtils.debug("通过冷却",player);
            MechanismUtils.addCooldown(player, displayName);
            player.playSound(player.getLocation(), Sound.valueOf(getText("sound")), 100, 10);
            player.spawnParticle(Particle.valueOf(getText("particle.type")),
                    player.getLocation().add(0, 2, 0), (int) getValue("particle.amount", level));
            run(player, level);
        }
    }

    public void run(Player player, int level) {

    }
}
