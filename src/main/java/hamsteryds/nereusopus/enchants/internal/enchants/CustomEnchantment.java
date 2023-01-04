package hamsteryds.nereusopus.enchants.internal.enchants;

import hamsteryds.nereusopus.utils.api.MathUtils;
import hamsteryds.nereusopus.utils.api.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashMap;

public class CustomEnchantment extends AbstractEnchantment {
    private HashMap<String, String> params;
    private HashMap<String, String> paramHolders;
    private boolean enable;
    private boolean grindstoneable;
    private int maxLevel;

    public CustomEnchantment(File file) {
        super(file);
        loadData();
    }

    public void loadData() {
        enable = config.getBoolean("enabled");
        grindstoneable = config.getBoolean("grindstoneable");
        maxLevel = config.getInt("maxLevel");
        ConfigurationSection paramSection = config.getConfigurationSection("params");
        params = new HashMap<>();
        paramHolders = new HashMap<>();
        if (paramSection != null) {
            for (String path : paramSection.getKeys(true)) {
                if (paramSection.getList(path) == null && paramSection.getConfigurationSection(path) == null) {
                    params.put(path, String.valueOf(paramSection.get(path)));
                    paramHolders.put("%" + path + "%", String.valueOf(paramSection.get(path)));
                }
            }
        }
    }

    public boolean enable() {
        return enable;
    }

    public boolean grindstoneable() {
        return grindstoneable;
    }

    public int maxLevel() {
        return maxLevel;
    }

    public HashMap<String, String> params() {
        return params;
    }

    public HashMap<String, String> paramHolders() {
        return paramHolders;
    }

    public double getValue(String path, String... defaultValue) {
        if (params.containsKey(path)) {
            return MathUtils.calculate(params.get(path));
        } else {
            return MathUtils.calculate(defaultValue[0]);
        }
    }

    public double getValue(String path, int level, String... defaultValue) {
        if (params.containsKey(path)) {
            return MathUtils.calculate(params.get(path), "level", level);
        } else {
            return MathUtils.calculate(defaultValue[0]);
        }
    }

    public String getText(String path, String... replacers) {
        return StringUtils.replace(params.get(path), replacers)
                .replace("{enchant}", getFormattedName("{displayname}"));
    }

    public boolean getBool(String path, boolean... defaultValue) {
        if (params.containsKey(path)) {
            return Boolean.parseBoolean(params.get(path));
        } else {
            return defaultValue[0];
        }
    }

    public int getInt(String path, int... defaultValue) {
        if (params.containsKey(path)) {
            return Integer.parseInt(params.get(path));
        } else {
            return defaultValue[0];
        }
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }
}
