package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class ConfigUtils {
    public static FileConfiguration config;

    public static void initialize() {
        Set<String> set = new HashSet<String>();
        set.add("limitcheck");
        config = autoUpdateConfigs("", "config.yml", set, true);
    }

    public static <T extends Enum<T>> List<T> getEnumList(ConfigurationSection section, String fileName, String path, Class<T> clazz, String... defaultValues) {
        List<String> values = section.getStringList(path);
        List<T> results = new ArrayList<>();
        for (String value : values) {
            try {
                results.add(Enum.valueOf(clazz, value.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException exception) {
                NereusOpus.severe("✖ NereusOpus配置出现错误 " + fileName + " : " + path + " - " + value + " 值错误，已自动将其忽略！");
            }
        }
        return results;
    }

    public static <T> List<T> getObjectList(ConfigurationSection section, String fileName, String path, Class<T> clazz, String... defaultValues) {
        List<String> values = section.getStringList(path);
        List<T> results = new ArrayList<>();
        for (String value : values) {
            try {
                Object obj = clazz.getDeclaredMethod("fromId", String.class).invoke(null, value);
                results.add((T) obj);
            } catch (IllegalArgumentException exception) {
                NereusOpus.severe("✖ NereusOpus配置出现错误 " + fileName + " : " + path + " - " + value + " 值错误，已自动将其忽略！");
            } catch (Exception ignored) {
            }
        }
        return results;
    }

    public static Map<String, String> getKeyMap(YamlConfiguration yaml, String sectionPath) {
        Map<String, String> result = new HashMap<>();
        ConfigurationSection section = yaml.getConfigurationSection(sectionPath);
        for (String path : section.getKeys(true)) {
            if (section.getString(path) == null) {
                continue;
            }
            result.put(path, section.getString(path));
        }
        return result;
    }

    public static <K, V> Map<K, V> getMapFromList(ConfigurationSection section, String path, String splitSymbol, Class<K> keyClazz, Class<V> valueClazz) {
        if (section == null) {
            return new HashMap<>();
        }
        List<String> list = section.getStringList(path);
        Map<K, V> result = new HashMap<>();
        for (String line : list) {
            result.put(getObjectFromString(line.split(splitSymbol)[0], keyClazz),
                    getObjectFromString(line.split(splitSymbol)[1], valueClazz));
        }
        return result;
    }

    public static <T> T getObjectFromString(String string, Class<T> clazz) {
        try {
            if (Enum.class.isAssignableFrom(clazz)) {
                Class<? extends Enum> enumClazz = (Class<? extends Enum>) clazz;
                return (T) Enum.valueOf(enumClazz, string.toUpperCase(Locale.ROOT));
            }
            if (Number.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) {
                Method parseMethod = clazz.getDeclaredMethod("parse" + clazz.getSimpleName(), String.class);
                return (T) parseMethod.invoke(null, string);
            }
            if (clazz.isAssignableFrom(String.class)) {
                return (T) string;
            }
            Method method = clazz.getDeclaredMethod("fromId", String.class);
            return (T) method.invoke(null, string);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static YamlConfiguration autoUpdateConfigs(String directory, String name) {
        return autoUpdateConfigs(directory, name, null, true);
    }

    public static YamlConfiguration autoUpdateConfigs(String directory, String name, Set<String> paths, boolean updateOrNot) {
        File file = NereusOpus.getFolder(directory, name);
        if (!file.exists()) {
            NereusOpus.plugin.saveResource(directory + name, true);
            return YamlConfiguration.loadConfiguration(file);
        }
        final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        try {
            NereusOpus.plugin.saveResource(directory + name, true);
        } catch (Exception exception) {
            NereusOpus.severe("✖ NereusOpus未找到资源文件 :" + directory + name);
        }
        YamlConfiguration newYaml = YamlConfiguration.loadConfiguration(file);
        if (paths == null) {
            for (String path : yaml.getKeys(true)) {
                newYaml.set(path, yaml.get(path));
            }
            try {
                newYaml.save(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return newYaml;
        } else {
            if (updateOrNot) {
                for (String path : paths) {
                    ConfigurationSection section = newYaml.getConfigurationSection(path);
                    if (section == null) {
                        yaml.set(path, newYaml.get(path));
                    } else {
                        yaml.set(path, null);
                        for (String child : section.getKeys(true)) {
                            yaml.set(path + "." + child, section.get(child));
                        }
                    }
                }
                for (String path : newYaml.getKeys(true)) {
                    if (newYaml.getConfigurationSection(path) == null) {
                        if (!yaml.contains(path)) {
                            yaml.set(path, newYaml.get(path));
                        }
                    }
                }
                try {
                    yaml.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                return yaml;
            } else {
                for (String path : yaml.getKeys(true)) {
                    boolean flag = true;
                    for (String banned : paths) {
                        if (path.startsWith(banned)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        newYaml.set(path, yaml.get(path));
                    }
                }
                for (String path : paths) {
                    ConfigurationSection section = yaml.getConfigurationSection(path);
                    if (section == null) {
                        newYaml.set(path, yaml.get(path));
                    } else {
                        for (String child : section.getKeys(true)) {
                            newYaml.set(path + "." + child, section.get(child));
                        }
                    }
                }
                try {
                    newYaml.save(file);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                return newYaml;
            }
        }
    }

    public static void printInfo() {
        File file = NereusOpus.getFolder("", "附魔大全.yml");
        if (file.exists()) {
            file.delete();
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        int tot = 0;
        for (AbstractEnchantment enchant : EnchantmentUtils.getEnchants()) {
            if (enchant instanceof CustomEnchantment custom) {
                yaml.set(tot + ".type", StringUtils.removeFormat(enchant.type().displayName));
                yaml.set(tot + ".rarity", StringUtils.removeFormat(enchant.rarity().displayName()));
                yaml.set(tot + ".name", StringUtils.removeFormat(enchant.displayName()));
                yaml.set(tot + ".max", custom.maxLevel());
                yaml.set(tot + ".id", enchant.id());
                yaml.set(tot + ".conflict", StringUtils.removeFormat(enchant.limits().conflictsToString()));
                yaml.set(tot + ".target", enchant.limits().targetsToString());
                yaml.set(tot + ".description", enchant.description());
                yaml.set(tot + ".enable", custom.enable());
                yaml.set(tot + ".grindstoneable", custom.grindstoneable());
                String paramCombined = "";
                for (String param : custom.params().keySet()) {
                    String value = custom.params().get(param);
                    if (LanguageUtils.enchantParams.containsKey(param)) {
                        String tmp;
                        try {
                            tmp = StringUtils.replace(LanguageUtils.translateEnchantParam(param, value, custom.maxLevel()));
                        } catch (Exception exception) {
                            tmp = LanguageUtils.getLang("enchant_params." + param,
                                    "{value}", "(" + value + ")"
                                    , "level", custom.maxLevel(),
                                    "damage", "伤害",
                                    "maxBlood", "最大血量",
                                    "amount", "数量",
                                    "dist", "距离",
                                    "*", "×");
                        }
                        paramCombined += tmp + "/";
                    }
                }
                yaml.set(tot + ".params", StringUtils.removeFormat(paramCombined));
            } else {
                int max = enchant.getMaxLevel();
                yaml.set(tot + ".type", StringUtils.removeFormat(enchant.type().displayName));
                yaml.set(tot + ".rarity", StringUtils.removeFormat(enchant.rarity().displayName()));
                yaml.set(tot + ".name", StringUtils.removeFormat(enchant.displayName()));
                yaml.set(tot + ".max", enchant.getMaxLevel());
                yaml.set(tot + ".id", enchant.id());
                yaml.set(tot + ".conflict", StringUtils.removeFormat(enchant.limits().conflictsToString()));
                yaml.set(tot + ".target", enchant.limits().targetsToString());
                yaml.set(tot + ".description", enchant.description());
                yaml.set(tot + ".enable", true);
                yaml.set(tot + ".grindstoneable", true);
                yaml.set(tot + ".params", "");
            }
            tot++;
        }
        try {
            yaml.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
