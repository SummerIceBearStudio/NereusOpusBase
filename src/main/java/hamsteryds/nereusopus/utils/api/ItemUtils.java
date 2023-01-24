package hamsteryds.nereusopus.utils.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import hamsteryds.nereusopus.enchants.internal.enchants.AbstractEnchantment;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.utils.internal.Pair;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfo;
import hamsteryds.nereusopus.utils.internal.data.EnchantmentsInfoDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class ItemUtils {
    public static final NamespacedKey enchantKey;
    public static final NamespacedKey enchantsInfoKey;
    public static final NamespacedKey configParamKey;
    public static final NamespacedKey configValueKey;
    private static Field profileField;

    static {
        try {
            SkullMeta meta = (SkullMeta) new ItemStack(Material.PLAYER_HEAD).getItemMeta();
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        enchantKey = new NamespacedKey("summericebearstore", "enchant");
        enchantsInfoKey = new NamespacedKey("summericebearstore", "enchantsinfo");
        configParamKey = new NamespacedKey("summericebearstore", "configparam");
        configValueKey = new NamespacedKey("summericebearstore", "configvalue");
    }

    public static ItemStack make(Material type, String displayName, String... lore) {
        return make(type, displayName, Arrays.asList(lore));
    }

    public static ItemStack make(Material type, int amount, String displayName, String... lore) {
        ItemStack item = make(type, displayName, Arrays.asList(lore));
        item.setAmount(amount);
        return item;
    }

    public static ItemStack make(Material type, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        if (!displayName.equalsIgnoreCase("")) {
            meta.setDisplayName(displayName);
        }
        if (lore.size() != 0) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addDurability(ItemStack item, double percent) {
        if (percent < 1 && percent > 0) {
            item.setDurability((short) Math.max(item.getDurability() - item.getType().getMaxDurability() * percent, 0));
        } else {
            return addDurability(item, (int) percent);
        }
        return item;
    }

    public static ItemStack addDurability(ItemStack item, short point) {
        if (item == null || !isDamageable(item)) {
            return item;
        }
        if (item == null || !isDamageable(item)) {
            return item;
        }
        if (item.hasItemMeta() && item.getItemMeta().isUnbreakable()) {
            return item;
        }
        item.setDurability((short) (item.getDurability() + point));
        if (item.getDurability() >= item.getType().getMaxDurability()) {
            return null;
        }
        return item;
    }

    public static boolean isDamageable(ItemStack itemStack) {
        return itemStack.getType().getMaxDurability() > 0;
    }

    @SafeVarargs
    public static ItemStack makeEnchantedBook(Pair<Enchantment, Integer>... enchants) {
        ItemStack item = make(Material.ENCHANTED_BOOK, "");
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        for (Pair<Enchantment, Integer> pair : enchants) {
            meta.addStoredEnchant(pair.getFirst(), pair.getSecond(), true);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static boolean grindstoneable(ItemStack item) {
        if (item == null) {
            return true;
        }
        for (Enchantment enchant : getEnchants(item).keySet()) {
            if (enchant instanceof CustomEnchantment custom) {
                if (!custom.grindstoneable()) {
                    return false;
                }
            } else {
                if (enchant.isCursed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ItemStack setSkull(ItemStack item, String headBase64) {
        if (item.getItemMeta() instanceof SkullMeta meta) {
            GameProfile profile = new GameProfile(UUID.fromString("036fd219-4618-3c50-92ab-e687de3eb47a"), "HamsterYDS");
            profile.getProperties().put("textures", new Property("textures", headBase64));
            try {
                profileField.set(meta, profile);
                item.setItemMeta(meta);
            } catch (SecurityException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
        return item;
    }

    public static ItemStack fromString(String string) {
        String[] splited = string.replace("&", "§").split(";");
        int length = splited.length;
        return make(Material.valueOf(splited[0]), length >= 2 ? splited[1] : "", length >= 3 ? splited[2].split(",") : new String[0]);
    }

    public static Map<Enchantment, Integer> getEnchants(ItemStack item) {
        if (item == null) {
            return new HashMap<>();
        }
        if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
            return meta.getStoredEnchants();
        } else {
            return item.getEnchantments();
        }
    }

    public static void setEnchants(ItemStack item, Map<Enchantment, Integer> addedEnchants) {
        Map<Enchantment, Integer> enchants = getEnchants(item);
        if (item != null) {
            for (Enchantment enchantment : enchants.keySet()) {
                item.removeEnchantment(enchantment);
            }
            if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
                for (Enchantment enchant : enchants.keySet()) {
                    meta.removeStoredEnchant(enchant);
                }
                for (Enchantment enchant : addedEnchants.keySet()) {
                    meta.addStoredEnchant(enchant, addedEnchants.get(enchant), true);
                }
                item.setItemMeta(meta);
            } else {
                ItemMeta meta = item.getItemMeta();
                for (Enchantment enchant : enchants.keySet()) {
                    meta.removeEnchant(enchant);
                }
                for (Enchantment enchant : addedEnchants.keySet()) {
                    meta.addEnchant(enchant, addedEnchants.get(enchant), true);
                }
                item.setItemMeta(meta);
            }
        }
    }

    public static List<String> getLore(ItemStack item) {
        if (item != null) {
            if (item.getItemMeta() != null) {
                if (item.getItemMeta().getLore() != null) {
                    return item.getItemMeta().getLore();
                }
            }
        }
        return new ArrayList<>();
    }

    public static String getDisplayName(ItemStack item) {
        if (item != null) {
            if (item.getItemMeta() != null) {
                if (item.getItemMeta().getDisplayName() != null) {
                    return item.getItemMeta().getDisplayName();
                }
            }
        }
        return "";
    }

    public static int getEnchantLevel(ItemStack item, Class<? extends AbstractEnchantment> clazz) {
        return item.getEnchantmentLevel(EnchantmentUtils.fromID(clazz.getSimpleName()));
    }

    public static int getEnchantLevel(ItemStack item, String id) {
        return item.getEnchantmentLevel(EnchantmentUtils.fromID(id));
    }

    public static ItemStack addDurability(ItemStack item, int added) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (meta.isUnbreakable()) {
            return item;
        }
        if (meta instanceof Damageable damageable && item.getType().getMaxDurability() >= 30) {
            if (damageable.getDamage() < item.getType().getMaxDurability()) {
                damageable.setDamage(damageable.getDamage() + added);
                item.setItemMeta(damageable);
            } else {
                item.setType(Material.AIR);
            }
        }
        return item;
    }

    public static Material getMaterial(String name) {
        try {
            return Material.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            try {
                return Material.valueOf("LEGACY_" + name.toUpperCase(Locale.ROOT));
            } catch (Exception exceptionAgain) {
                return Material.STONE;
            }
        }
    }
}
