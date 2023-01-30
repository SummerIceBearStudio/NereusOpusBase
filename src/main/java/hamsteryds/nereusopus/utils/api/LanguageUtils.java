package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.NereusOpus;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * <p>语言工具类</p>
 */
public class LanguageUtils {
    /**
     * 语言设置模块
     */
    public static YamlConfiguration language;
    /**
     * 附魔参数
     */
    public static Map<String, String> enchantParams;
    private static Map<String, String> papiHolders;

    /**
     * 初始化
     */
    public static void initialize() {
        language = ConfigUtils.autoUpdateConfigs("languages/", ConfigUtils.config.getString("language"));
        enchantParams = ConfigUtils.getKeyMap(language, "enchant_params");
        papiHolders = ConfigUtils.getKeyMap(language, "papi_holders");
    }

    /**
     * 获取物品
     *
     * @param path   路径
     * @param params 参数
     * @return 物品的 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/ItemStack.html">{@code ItemStack}</a>
     */
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

    /**
     * 获取语言
     *
     * @param path   路径
     * @param params 参数
     * @return {@link String} 语言
     */
    @SafeVarargs
    public static <E> String getLang(String path, E... params) {
        String lang = language.getString(path, "✖ NereusOpus语言文件错误 : " + path + " 不存在！");
        return StringUtils.replace(lang, params);
    }

    /**
     * 获得多个语言
     *
     * @param path   路径
     * @param params 参数个数
     * @return <p>{@link List}&lt;{@link String}&gt; 语言列表</p>
     */
    @SafeVarargs
    public static <E> List<String> getLangs(String path, E... params) {
        List<String> langs = language.getStringList(path);
        for (int i = 0; i < langs.size(); i++) {
            langs.set(i, StringUtils.replace(langs.get(i), params));
        }
        return langs;
    }

    /**
     * 解析Papi表达式
     *
     * @param expression 表达式
     * @return {@link String} 翻译结果
     */
    public static String translatePapiExpression(String expression) {
        for (String holder : papiHolders.keySet()) {
            expression = expression.replace("%" + holder + "%", papiHolders.get(holder));
        }
        return expression;
    }

    /**
     * 翻译附魔参数
     *
     * @param param      参数
     * @param expression 表达式
     * @param level      等级
     * @return {@link String} 翻译结果
     */
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
