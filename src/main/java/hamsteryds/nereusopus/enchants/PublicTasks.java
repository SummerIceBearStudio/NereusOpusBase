package hamsteryds.nereusopus.enchants;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.enchants.simple.Streamlining;
import hamsteryds.nereusopus.enchants.simple.Thrive;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PublicTasks {
    public static HashMap<CustomEnchantment, Method> taskingEnchants = new HashMap<>();
    public static HashMap<String, CustomEnchantment> taskingEnchantsName = new HashMap<String, CustomEnchantment>();

    public static void registerTaskEnchant(CustomEnchantment enchant, Class clazz) {
        try {
            taskingEnchants.put(enchant, clazz.getDeclaredMethod("run", Player.class, int.class, int.class));
            taskingEnchantsName.put(enchant.getName(), enchant);
        } catch (NoSuchMethodException ignored) {
        }
    }

    public static void initialize() {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerInventory inv = player.getInventory();
                    for (int i = 0; i < inv.getSize(); i++) {
                        ItemStack item = inv.getItem(i);
                        if (item == null) {
                            continue;
                        }
                        Map<Enchantment, Integer> enchants = item.getEnchantments();
                        for (Enchantment enchant : enchants.keySet()) {
                            if (!taskingEnchantsName.containsKey(enchant.getName())) {
                                continue;
                            }
                            CustomEnchantment customEnchant = taskingEnchantsName.get(enchant.getName());
                            if (!customEnchant.enable()) {
                                continue;
                            }
                            int level = enchants.get(enchant);
                            if (level >= 1 && shouldRun(customEnchant, level, counter)) {
                                try {
                                    taskingEnchants.get(customEnchant).invoke(enchant, player, i, level);
                                } catch (IllegalAccessException | InvocationTargetException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }

                    int maxThrive = 0, maxStreamlining = 0;

                    Thrive thrive = (Thrive) EnchantmentUtils.fromID("thrive");
                    Streamlining streamlining = (Streamlining) EnchantmentUtils.fromID("streamlining");
                    for (EquipmentSlot slot : InventoryUtils.ARMORS) {
                        ItemStack item = inv.getItem(slot);
                        if (item == null) {
                            continue;
                        }
                        if (item.getType() == Material.AIR) {
                            continue;
                        }

                        Map<Enchantment,Integer> enchants=item.getEnchantments();
                        if(enchants==null)
                            continue;
                        if(enchants.containsKey(EnchantmentUtils.fromDisplayName("Thrive")))
                            maxThrive += Math.max(0, enchants.get(EnchantmentUtils.fromDisplayName("Thrive")));
                        if(enchants.containsKey(EnchantmentUtils.fromDisplayName("Streamlining")))
                            maxStreamlining += Math.max(0, enchants.get(EnchantmentUtils.fromDisplayName("Streamlining")));
                    }

                    assert thrive != null;
                    if (thrive.enable()) {
                        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                        assert maxHealth != null;
                        maxHealth.getModifiers().forEach(
                                (AttributeModifier modifier) -> {
                                    if (modifier.getName().equalsIgnoreCase("thrive")) {
                                        maxHealth.removeModifier(modifier);
                                    }
                                });
                        if (maxThrive > 0) {
                            AttributeModifier modifier = new AttributeModifier("thrive",
                                    thrive.getValue("max-health-increase", maxThrive),
                                    AttributeModifier.Operation.ADD_NUMBER);
                            maxHealth.addModifier(modifier);
                        }
                    }

                    assert streamlining != null;
                    if (streamlining.enable()) {
                        AttributeInstance moveSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
                        assert moveSpeed != null;
                        moveSpeed.getModifiers().forEach(
                                (AttributeModifier modifier) -> {
                                    if (modifier.getName().equalsIgnoreCase("streamlining")) {
                                        moveSpeed.removeModifier(modifier);
                                    }
                                });
                        if (maxStreamlining > 0) {
                            AttributeModifier modifier = new AttributeModifier("streamlining",
                                    streamlining.getValue("speed", maxStreamlining),
                                    AttributeModifier.Operation.ADD_NUMBER);
                            moveSpeed.addModifier(modifier);
                        }
                    }
                }
                counter++;
            }
        }.runTaskTimer(NereusOpus.plugin, 0L, 20L);
    }

    public static boolean shouldRun(CustomEnchantment enchant, int level, int counter) {
        return counter * 20 % (int) enchant.getValue("repeat-ticks", level, "20") == 0;
    }
}