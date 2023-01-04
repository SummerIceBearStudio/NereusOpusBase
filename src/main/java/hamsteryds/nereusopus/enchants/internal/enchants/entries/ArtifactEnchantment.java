package hamsteryds.nereusopus.enchants.internal.enchants.entries;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.WorldUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public abstract class ArtifactEnchantment extends EventExecutor {
    public static List<Double> range;
    public static List<Double> height;
    public static List<String> triggers;
    public static HashMap<EquipmentSlot, Pair<Double,Double>> customParticleData=new HashMap<>();
    public static HashMap<EquipmentSlot, String> customParticleType=new HashMap<>();

    static {
        YamlConfiguration config = ConfigUtils.autoUpdateConfigs("enchantments/", "artifacts.yml");
        range = config.getDoubleList("range");
        height = config.getDoubleList("height");
        triggers = config.getStringList("triggers");
        ConfigurationSection section=config.getConfigurationSection("custom");
        for(String path:section.getKeys(false)){
            String type=section.getString(path+".type");
            double range=section.getDouble(path+".range",0);
            double height=section.getDouble(path+".height",0);
            customParticleType.put(EquipmentSlot.valueOf(path),type);
            customParticleData.put(EquipmentSlot.valueOf(path),new Pair<>(range,height));
        }
    }

    public ArtifactEnchantment(File file) {
        super(file);
    }

    @Override
    public void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
        if(params().getOrDefault("custom","false").equals("true")){
            spawnCustomParticle(player.getLocation(),slot);
            return;
        }
        if (player.isGliding() && slot == EquipmentSlot.CHEST) {
            spawnSimpleParticle(player.getLocation());
        }
        if (slot == EquipmentSlot.FEET) {
            spawnCircleParticle(player.getLocation().add(0, 0.2, 0), 2);
        }
    }

    @Override
    public void blockBreak(int level, BlockBreakEvent event) {
        Block block = event.getBlock();
        if (triggers.contains(block.getType().getKey().getKey())) {
            spawnSimpleParticle(block.getLocation().add(0.5, 0.5, 0.5));
        }
    }

    @Override
    public void attackEntity(int level, EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            if (player.getAttackCooldown() < 0.9f) {
                return;
            }
        }
        if (event.getEntity() instanceof LivingEntity creature) {
            int size = creature instanceof Slime ? Math.min(((Slime)creature).getSize(), 3) : 2;
            size = creature instanceof Ageable ? ((Ageable)creature).isAdult() ? 2 : 1 : size;
            size = creature instanceof Ghast ? 3 : size;
            spawnDNAParticles(event.getEntity().getLocation().add(0, 1, 0), size);
        }
    }

    public void spawnDNAParticles(Location loc, int size) {
        WorldUtils.spawnRNAParticles(getParticle(), loc, (int) getValue("amount"), getOptions(), height.get(size - 1), range.get(size - 1));
    }

    public void spawnSimpleParticle(Location loc) {
        WorldUtils.spawnSimpleParticle(getParticle(), loc, (int) getValue("amount"), getOptions());
    }

    public void spawnCircleParticle(Location loc, int size) {
        WorldUtils.spawnCircleParticles(getParticle(), loc, (int) getValue("amount"), getOptions(), range.get(size - 1));
    }

    public void spawnCustomParticle(Location loc, EquipmentSlot slot){
        String type=customParticleType.get(slot);
        Pair<Double,Double> data=customParticleData.get(slot);
        double height=switch (slot){
            case HAND, OFF_HAND -> 1.2;
            case FEET -> 0.1;
            case LEGS -> 0.6;
            case CHEST ->1.15;
            case HEAD -> 1.8;
        };
        loc=loc.clone().add(0,height,0);
        switch (type){
            case "RNA"->WorldUtils.spawnRNAParticles(getParticle(), loc, (int) getValue("amount"), getOptions(), data.getSecond(),data.getFirst(),10,2);
            case "CIRCLE"-> WorldUtils.spawnCircleParticles(getParticle(), loc.add(0,data.getSecond(),0), (int) getValue("amount"), getOptions(), data.getFirst());
            case "SIMPLE"-> WorldUtils.spawnSimpleParticle(getParticle(), loc, (int) getValue("amount"), getOptions());
        }
    }

    public Particle getParticle() {
        return Particle.ASH;
    }

    public Particle.DustOptions getOptions() {
        return null;
    }
}
