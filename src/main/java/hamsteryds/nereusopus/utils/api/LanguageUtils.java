package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.NereusOpus;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class LanguageUtils {
    public static YamlConfiguration language;
    public static Map<String, String> enchantParams;
    private static Map<String, String> papiHolders;

    public static void initialize() {
        language = ConfigUtils.autoUpdateConfigs("languages/", ConfigUtils.config.getString("language"));
        enchantParams = ConfigUtils.getKeyMap(language, "enchant_params");
        papiHolders = ConfigUtils.getKeyMap(language, "papi_holders");
    }

    @SafeVarargs
    public static <E> ItemStack getItem(String path, E... params) {
        Material type;
        try {
            type = Material.valueOf(getLang(path + ".type"));
        } catch (Exception exception) {
            NereusOpus.severe("✖ NereusOpus语言文件错误 : " + path + ".type 不存在，已自动取用默认值(STONE)！");
            return new ItemStack(Material.STONE);
        }
        String name = getLang(path + ".name", params);
        List<String> lore = getLangs(path + ".lore", params);
        return ItemUtils.make(type, name, lore);
    }

    @SafeVarargs
    public static <E> String getLang(String path, E... params) {
        String lang = language.getString(path, "✖ NereusOpus语言文件错误 : " + path + " 不存在！");
        return StringUtils.replace(lang, params);
    }

    @SafeVarargs
    public static <E> List<String> getLangs(String path, E... params) {
        List<String> langs = language.getStringList(path);
        for (int i = 0; i < langs.size(); i++) {
            langs.set(i, StringUtils.replace(langs.get(i), params));
        }
        return langs;
    }

    public static String translatePapiExpression(String expression) {
        for (String holder : papiHolders.keySet()) {
            expression = expression.replace("%" + holder + "%", papiHolders.get(holder));
        }
        return expression;
    }

    public static String translateEnchantParam(String param, String expression, int... level) {
        String result = enchantParams.get(param);
        if (level.length > 0) {
            expression = String.valueOf(MathUtils.calculate(expression, "level", level[0]));
        } else {
            expression = "{" + expression.replace("level", "N") + "}";
        }
        return result.replace("{value}", expression);
    }
}
