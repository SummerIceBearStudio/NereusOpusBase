package hamsteryds.nereusopus.enchants.internal.enchants;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.CustomEnchantments;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentLimit;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentType;
import hamsteryds.nereusopus.enchants.internal.utils.AttainSource;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractEnchantment extends Enchantment {
    protected final File file;
    protected final YamlConfiguration config;
    protected final String enchantId;
    protected final EnchantmentType type;
    protected Enchantment enchantment;
    protected String displayName;
    protected String description;
    protected CustomRarity rarity;
    protected EnchantmentLimit limits;

    public AbstractEnchantment(File file) {
        super(file.getPath().contains("vanilla") ? NamespacedKey.minecraft(file.getName().replace(".yml", "")) :
                new NamespacedKey("minecraft", file.getName().replace(".yml", "")));
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
        this.enchantId = this.config.getString("name", getKey().getKey());
        this.type = EnchantmentUtils.getType(file.getName().replace(".yml", ""));

        //AUTO REGISTER
        if (type != EnchantmentType.VANILLA) {
            EnchantmentUtils.byName.remove(getName());
            EnchantmentUtils.byKey.remove(getKey());
            Enchantment.registerEnchantment(this);
        }
        loadBasic(getKey());
        //TODO
//        TranslationRegistry.create(Key.key("nereusopus")).register(id(),Locale.CHINA,
//                new MessageFormat().format(displayName));

        //DATA INPUT
        CustomEnchantments.BY_ID.put(enchantId, this);
        CustomEnchantments.BY_KEY.put(getKey(), this);
        CustomEnchantments.BY_DISPLAYNAME.put(StringUtils.removeFormat(displayName), this);
        CustomEnchantments.BY_RARITY.get(rarity).add(this);
        CustomEnchantments.BY_TYPE.get(type).add(this);
        for (Material item : limits.types()) {
            if (getItemTarget().includes(item)) {
                CustomEnchantments.BY_ITEM.get(item).add(this);
            }
        }
    }

    protected void loadBasic(NamespacedKey key) {
        enchantment = Enchantment.getByKey(key);
        displayName = config.getString("displayName");
        rarity = CustomRarity.fromId(config.getString("rarity"));
        description = config.getString("description");
        limits = new EnchantmentLimit(config.getConfigurationSection("limits"), true);
    }

    public void loadLimits() {
        limits = new EnchantmentLimit(config.getConfigurationSection("limits"), false);
    }

    public void adaptAttribute(String param, Object value) {
        if (config.contains(param)) {
            config.set(param, value);
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            config.save(file);
            loadBasic(getKey());
            regenGui();
            if (this instanceof CustomEnchantment custom) {
                custom.loadData();
            }
        } catch (IOException e) {
            NereusOpus.severe("✖ NereusOpus附魔保存出现错误: " + displayName);
        }
    }

    public void regenGui() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "nereusopus regen");
    }

    public String getFormattedName(String format) {
        return format.replace("{rarity}", rarity().displayName())
                .replace("{description}", description())
                .replace("{displayname}", rarity.color() + displayName())
                .replace("{name}", displayName())
                .replace("{id}", id());
    }

    public String description() {
        return description;
    }

    public EnchantmentLimit limits() {
        return limits;
    }

    public String id() {
        return enchantId;
    }

    public String displayName() {
        return displayName;
    }

    public CustomRarity rarity() {
        return rarity;
    }

    public EnchantmentType type() {
        return type;
    }

    @Override
    public @NotNull
    String getName() {
        return enchantId.toLowerCase(Locale.ROOT);
    }

    @Override
    public int getMaxLevel() {
        return enchantment.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public @NotNull
    EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return type == EnchantmentType.CURSE;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment enchantment) {
        return limits.conflictsWith(enchantment, 1);
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack itemStack) {
        return !limits.conflictsWith(enchantment, 1);
    }

    @Override
    public @NotNull
    Component displayName(int i) {
        return enchantment.displayName(i);
    }

    @Override
    public boolean isTradeable() {
        return rarity.canAttainFrom(AttainSource.VILLAGER);
    }

    @Override
    public boolean isDiscoverable() {
        return rarity.canAttainFrom(AttainSource.LOOT_CHEST);
    }

    @Override
    public @NotNull
    EnchantmentRarity getRarity() {
        return enchantment.getRarity();
    }

    @Override
    public float getDamageIncrease(int i, @NotNull EntityCategory entityCategory) {
        return enchantment.getDamageIncrease(i, entityCategory);
    }

    @Override
    public @NotNull
    Set<EquipmentSlot> getActiveSlots() {
        return limits.slots();
    }

    @Override
    public @NotNull String translationKey() {
        return "nareusopus_enchant_" + id();
    }
}
