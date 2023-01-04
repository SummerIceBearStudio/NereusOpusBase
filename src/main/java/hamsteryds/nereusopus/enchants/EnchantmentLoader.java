package hamsteryds.nereusopus.enchants;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.customize.CustomizeEnchantment;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.data.CustomTarget;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentType;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.VanillaEnchantment;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static hamsteryds.nereusopus.enchants.CustomEnchantments.*;

public class EnchantmentLoader {
    public static boolean enableEnchantUpdate = ConfigUtils.config.getBoolean("updater.enable");
    public static Set<String> updateContents = new HashSet<>(ConfigUtils.config.getStringList("updater.contents"));

    public static void loadEnchantments() {
        Trigger.initialize();
        Target.initialize();

        YamlConfiguration customRarity = ConfigUtils.autoUpdateConfigs("enchantments/", "rarities.yml", Set.of(""), false);
        for (String rarityId : customRarity.getKeys(false)) {
            CustomRarity.registerRarity(rarityId, customRarity.getConfigurationSection(rarityId));
            BY_RARITY.put(CustomRarity.fromId(rarityId), new HashSet<>());
        }
        YamlConfiguration customTarget = ConfigUtils.autoUpdateConfigs("enchantments/", "targets.yml");
        for (String targetId : customTarget.getKeys(false)) {
            CustomTarget.registerTarget(targetId, customTarget.getConfigurationSection(targetId));
            for (Material type : CustomTarget.fromId(targetId).types()) {
                if (!BY_ITEM.containsKey(type)) {
                    BY_ITEM.put(type, new HashSet<>());
                }
            }
        }
        BY_ITEM.put(Material.BOOK, new HashSet<>());
        for (EnchantmentType type : EnchantmentType.values()) {
            BY_TYPE.put(type, new HashSet<>());
        }

        for (Enchantment enchant : Enchantment.values()) {
            try {
                if (enchant.getKey().namespace().equalsIgnoreCase("minecraft")
                        && NereusOpus.plugin.getResource("enchantments/vanilla/" + enchant.getKey().getKey() + ".yml") != null) {
                    types.put(enchant.getKey().getKey(), EnchantmentType.VANILLA);
                    ConfigUtils.autoUpdateConfigs("enchantments/vanilla/", enchant.getKey().getKey() + ".yml");
                    new VanillaEnchantment(NereusOpus.getFolder("enchantments/vanilla/", enchant.getKey().getKey() + ".yml"));
                }
            } catch (Exception ignored) {
            }
        }

        try {
            Field field = Enchantment.class.getDeclaredField("acceptingNew");
            field.setAccessible(true);
            field.set(Boolean.class, true);

            NereusOpus.plugin.saveResource("enchantments/enchants.yml", true);
            YamlConfiguration enchants = YamlConfiguration.loadConfiguration(NereusOpus.getFolder("enchantments/", "enchants.yml"));
            YamlConfiguration current;
            for (String path : enchants.getKeys(false)) {
                for (String enchant : enchants.getStringList(path)) {
                    File file = NereusOpus.getFolder("/enchantments/" + path + "/", enchant + ".yml");
                    current = YamlConfiguration.loadConfiguration(file);
                    if (file.exists()) {
                        if (enableEnchantUpdate) {
                            ConfigUtils.autoUpdateConfigs("enchantments/" + path + "/", enchant + ".yml", updateContents, true);
                        }
                    } else {
                        ConfigUtils.autoUpdateConfigs("enchantments/" + path + "/", enchant + ".yml");
                    }
                    if (current.contains("type")) {
                        types.put(enchant, EnchantmentType.valueOf(current.getString("type")));
                    } else {
                        types.put(enchant, EnchantmentType.valueOf(path.toUpperCase(Locale.ROOT)));
                    }
                    Class.forName("hamsteryds.nereusopus.enchants." + path + "." + StringUtils.keyToName(enchant)).getConstructor(File.class).newInstance(file);
                }
            }

            File directory = new File(NereusOpus.plugin.getDataFolder() + "/enchantments/customize/");
            if (directory.exists()) {
                for (File file : getFiles(directory)) {
                    //附魔社区autoupdate
                    current = YamlConfiguration.loadConfiguration(file);
                    String enchant = file.getName().replace(".yml", "");
                    types.put(enchant, EnchantmentType.valueOf(current.getString("type")));
                    new CustomizeEnchantment(file);
                }
            } else {
                directory.mkdirs();
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException | IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }

        BY_ITEM.get(Material.BOOK).addAll(BY_KEY.values());

        for (Enchantment vanilla : EnchantmentUtils.getEnchants()) {
            AbstractEnchantment enchant = EnchantmentUtils.fromVanilla(vanilla);
            if (enchant != null) {
                enchant.loadLimits();
            }
        }

        PublicTasks.initialize();
        LevelFixer.initialize();
    }

    public static ArrayList<File> getFiles(File file) {
        ArrayList<File> fileList = new ArrayList<File>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File fileIndex : files) {
                    if (fileIndex.isDirectory()) {
                        fileList.addAll(getFiles(fileIndex));
                    } else {
                        fileList.add(fileIndex);
                    }
                }
        }
        return fileList;
    }
}
