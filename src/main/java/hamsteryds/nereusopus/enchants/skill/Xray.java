package hamsteryds.nereusopus.enchants.skill;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.enchants.entries.SkillEnchantment;
import hamsteryds.nereusopus.utils.api.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Xray extends SkillEnchantment implements Listener {
    public static HashMap<Location, UUID> shulkers = new HashMap<>();
    public HashMap<Material, ChatColor> ores = new HashMap<>();

    public Xray(File file) {
        super(file);
        ConfigurationSection blocks = config.getConfigurationSection("params.blocks");
        for (String color : blocks.getKeys(false)) {
            for (String type : blocks.getStringList(color)) {
                try {
                    ores.put(Material.valueOf(type), ChatColor.valueOf(color));
                } catch (Exception exception) {
                    NereusOpus.severe("✖附魔Xray配置出现问题(不存在的方块类型:" + type + ") (已经自动忽略)");
                }
            }
        }
        Bukkit.getPluginManager().registerEvents(this, NereusOpus.plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakBlock(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            Location loc = event.getBlock().getLocation();
            if (Xray.shulkers.containsKey(loc)) {
                if (Bukkit.getEntity(Xray.shulkers.get(loc)) instanceof Shulker shulker) {
                    shulker.remove();
                }
            }
        }
    }

    @Override
    public void run(Player player, int level) {
        double range = getValue("range", level);

        List<UUID> shulkers = new ArrayList<>();

        for (double x = -range; x <= range; x++) {
            for (double y = -range; y <= range; y++) {
                for (double z = -range; z <= range; z++) {
                    Location loc = player.getLocation().add(x, y, z);
                    Block block = loc.getBlock();
                    ChatColor color = ores.get(block.getType());
                    if (color == null) {
                        continue;
                    }

                    Shulker shulker = (Shulker) loc.getWorld().spawnEntity(loc, EntityType.SHULKER);

                    shulker.setInvulnerable(true);
                    shulker.setSilent(true);
                    shulker.setAI(false);
                    shulker.setGravity(false);
                    shulker.setGlowing(true);
                    shulker.setInvisible(true);
                    shulker.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(100000, 0));
                    if (getBool("colored")) {
                        ColorUtils.getTeamByColor(color).addEntry(shulker.getUniqueId().toString());
                    }
                    shulkers.add(shulker.getUniqueId());
                    Xray.shulkers.put(block.getLocation(), shulker.getUniqueId());
                }
            }
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID uuid : shulkers) {
                    Entity entity = Bukkit.getEntity(uuid);
                    if (entity != null) {
                        entity.remove();
                    }
                }
            }
        }.runTaskLater(NereusOpus.plugin, (long) getValue("duration", level));
    }
}
