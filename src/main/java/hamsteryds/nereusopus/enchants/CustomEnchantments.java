package hamsteryds.nereusopus.enchants;

import hamsteryds.nereusopus.enchants.internal.data.CustomRarity;
import hamsteryds.nereusopus.enchants.internal.data.EnchantmentType;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.HashSet;

public class CustomEnchantments {
    public static HashMap<Material, HashSet<AbstractEnchantment>> BY_ITEM = new HashMap<>();
    public static HashMap<CustomRarity, HashSet<AbstractEnchantment>> BY_RARITY = new HashMap<>();
    public static HashMap<String, AbstractEnchantment> BY_ID = new HashMap<>(); //首字母大写或者原版key
    public static HashMap<String, AbstractEnchantment> BY_DISPLAYNAME = new HashMap<>(); //去除格式代码的自定义名称
    public static HashMap<EnchantmentType, HashSet<AbstractEnchantment>> BY_TYPE = new HashMap<>();
    public static HashMap<NamespacedKey, AbstractEnchantment> BY_KEY = new HashMap<>(); //key

    public static HashMap<String, EnchantmentType> types = new HashMap<>();
    public static HashMap<String, String> targets = new HashMap<>();
}
