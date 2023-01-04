package hamsteryds.nereusopus.enchants.internal.data;

import hamsteryds.nereusopus.utils.api.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomTarget {
    public static HashMap<String, CustomTarget> customTargets = new HashMap<>();
    private final String targetId;
    private final String displayName;
    private final Set<Material> types;
    private final Set<EquipmentSlot> slots;
    private final int limits;

    public CustomTarget(String targetId, String displayName, List<Material> targets, List<EquipmentSlot> slots, int limits) {
        this.targetId = targetId;
        this.displayName = displayName;
        this.types = new HashSet<>(targets);
        this.slots = new HashSet<>(slots);
        this.limits = limits;
    }

    public static void registerTarget(String targetId, ConfigurationSection config) {
        CustomTarget target = new CustomTarget(targetId, config.getString("name"),
                ConfigUtils.getEnumList(config, "targets.yml", "types", Material.class),
                ConfigUtils.getEnumList(config, "targets.yml", "slots", EquipmentSlot.class),
                config.getInt("limits"));
        customTargets.put(targetId, target);
    }

    public static CustomTarget fromId(String target) {
        return customTargets.get(target);
    }

    public String id() {
        return targetId;
    }

    public String name() {
        return displayName;
    }

    public int limits() {
        return limits;
    }

    public Set<Material> types() {
        return types;
    }

    public Set<EquipmentSlot> slots() {
        return slots;
    }

    public boolean containsType(Material type) {
        return types.contains(type);
    }

    public boolean containsSlot(EquipmentSlot slot) {
        return slots.contains(slot);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
