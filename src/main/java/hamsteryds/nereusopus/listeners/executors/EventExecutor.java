package hamsteryds.nereusopus.listeners.executors;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.enchants.internal.enchants.CustomEnchantment;
import hamsteryds.nereusopus.enchants.internal.utils.LimitSet;
import hamsteryds.nereusopus.listeners.executors.entries.CheckListeners;
import hamsteryds.nereusopus.utils.api.DebugUtils;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import hamsteryds.nereusopus.utils.api.MechanismUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventExecutor extends CustomEnchantment implements EventFunctions {
    private static final HashMap<ActionType, Method> executors = new HashMap<>();
    public static List<ActionType> types = List.of(ActionType.DAMAGED_BY_ENTITY,
            ActionType.BLOCK_BREAK);
    private static int stamp = 0;

    public EventExecutor(File file) {
        super(file);
    }

    public static void initialize() {
        try {
            for (Method method : EventFunctions.class.getDeclaredMethods()) {
                char[] chars = method.getName().toCharArray();
                StringBuilder actionName = new StringBuilder();
                for (char letter : chars) {
                    if (letter < 90 && letter > 64) {
                        actionName.append("_");
                        actionName.append(letter);
                    } else {
                        actionName.append(Character.toChars(letter - 32));
                    }
                }
                executors.put(ActionType.valueOf(actionName.toString()), method);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                stamp += 20;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    execute(player, ActionType.TICK_TASK, null, InventoryUtils.ALL);
                }
            }
        }.runTaskTimer(NereusOpus.plugin, 0L, 20L);
    }

    public static void execute(Entity tmp, ActionType type, Event event, EquipmentSlot... slots) {
        if (!(tmp instanceof LivingEntity entity)) {
            return;
        }
        HashMap<EquipmentSlot, ItemStack> equippedItems = InventoryUtils.getEquippedItems(entity);
        if (equippedItems.size() == 0) {
            return;
        }
        for (EquipmentSlot slot : slots) {
            ItemStack item = equippedItems.get(slot);
            execute(tmp, item, type, event, slot);
        }
    }

    public static void execute(Entity tmp, ItemStack item, ActionType type, Event event, EquipmentSlot slot) {
        if (item == null) {
            return;
        }
        if (item.getItemMeta() == null) {
            return;
        }
        if (CheckListeners.events.contains(event)) {
            return;
        }
        if (!(tmp instanceof LivingEntity entity)) {
            return;
        }
        if (tmp instanceof Player player && event != null) {
            if (types.contains(type) && !item.getType().toString().contains("HOE")) {
                if (!MechanismUtils.checkCooldown(player, type + ":" + slot.toString(),
                        0.15, false)) {
                    return;
                }
                MechanismUtils.addCooldown(player, type + ":" + slot);
            } else {

            }
        }

        if ((item.getType() == Material.BOW || item.getType() == Material.CROSSBOW)
                && event instanceof EntityDamageByEntityEvent evt) {
            if (!(evt.getDamager() instanceof AbstractArrow)) {
                return;
            }
        }
        Map<Enchantment, Integer> enchants = item.getEnchantments();
        for (Enchantment enchantment : enchants.keySet()) {
            if (enchantment instanceof EventExecutor executor) {
                int level = enchants.get(enchantment);
                //debug code 最好不要删除,有时能救命
//                System.out.println("Debug: " + EnchantmentUtils.isAvailable(executor, slot, item, LimitSet.USE, entity)
//                        + executor.enable()
//                        + MechanismUtils.checkPercent(executor, level)
//                        + MechanismUtils.checkRequireFull(executor, entity)
//                        + MechanismUtils.checkShiftIgnored(executor, entity));
                //debug code
                if (EnchantmentUtils.isAvailable(executor, slot, item, LimitSet.USE, entity)
                        && executor.enable()
                        && MechanismUtils.checkPercent(executor, level)
                        && MechanismUtils.checkRequireFull(executor, entity)
                        && MechanismUtils.checkShiftIgnored(executor, entity)) {
                    DebugUtils.debug(enchantment.toString(), entity);

//                    if(entity instanceof Player player) {
//                        if(executor.params().containsKey("cooldown")){
//                            double cooldown=executor.getValue("cooldown",level);
//                            if (!MechanismUtils.checkCooldown(player, enchantment.getName(), cooldown, false)) {
//                                continue;
//                            }
//                            MechanismUtils.addCooldown(player,enchantment.getName());
//                        }
//                    }

                    if (event != null) {
                        executor.call(level, type, entity, event);
                    } else {
                        executor.call(level, slot, (Player) entity, type);
                    }
                }
            }
        }
    }

    public void call(int level, ActionType type, LivingEntity entity, Event event) {
        try {
            executors.get(type).invoke(this, level, event);
            executors.get(ActionType.TRIGGER).invoke(this, level, type, event, entity);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public void call(int level, EquipmentSlot slot, Player player, ActionType type) {
        try {
            executors.get(type).invoke(this, level, slot, player, stamp);
            executors.get(ActionType.TRIGGER).invoke(this, level, type, null, player);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }
}
