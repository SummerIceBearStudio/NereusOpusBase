package hamsteryds.nereusopus.listeners.executors.entries;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.InventoryUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(PlayerItemDamageEvent event) {
        EventExecutor.execute(event.getPlayer(), ActionType.ITEM_DAMAGE, event, InventoryUtils.HANDS);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        EventExecutor.execute(player, ActionType.INTERACT_ENTITY, event, InventoryUtils.HANDS);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        EventExecutor.execute(player, ActionType.FISH, event, InventoryUtils.HANDS);
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        EventExecutor.execute(player, ActionType.INTERACT_RIGHT,
                new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, null, null, null), InventoryUtils.HANDS);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (ConfigUtils.config.getBoolean("fixers.bloodfixer")) {
            player.resetMaxHealth();
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers().forEach(
                    (AttributeModifier modifier) -> {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
                    });
        }

        if (ConfigUtils.config.getBoolean("fixers.speedfixer")) {
            player.setWalkSpeed(0.2f);
            player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers().forEach(
                    (AttributeModifier modifier) -> {
                        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(modifier);
                    });
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert maxHealth != null;
        maxHealth.getModifiers().forEach(
                (AttributeModifier modifier) -> {
                    if (modifier.getName().equalsIgnoreCase("thrive")) {
                        maxHealth.removeModifier(modifier);
                    }
                });

        AttributeInstance moveSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        assert moveSpeed != null;
        moveSpeed.getModifiers().forEach(
                (AttributeModifier modifier) -> {
                    if (modifier.getName().equalsIgnoreCase("streamlining")) {
                        moveSpeed.removeModifier(modifier);
                    }
                });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            EventExecutor.execute(player, ActionType.INTERACT_LEFT_BLOCK, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_LEFT, event, InventoryUtils.HANDS);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT_BLOCK, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT, event, InventoryUtils.HANDS);
        } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
            EventExecutor.execute(player, ActionType.INTERACT_LEFT_AIR, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_LEFT, event, InventoryUtils.HANDS);
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT_AIR, event, InventoryUtils.HANDS);
            EventExecutor.execute(player, ActionType.INTERACT_RIGHT, event, InventoryUtils.HANDS);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJump(PlayerJumpEvent event) {
        EventExecutor.execute(event.getPlayer(), ActionType.PLAYER_JUMP, event, InventoryUtils.ALL);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onElytraBoost(PlayerElytraBoostEvent event) {
        EventExecutor.execute(event.getPlayer(), ActionType.ELYTRA_BOOST, event, InventoryUtils.ARMORS);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHunger(FoodLevelChangeEvent event) {
        if (event.getFoodLevel() > event.getEntity().getFoodLevel()) {
            return;
        }
        EventExecutor.execute(event.getEntity(), ActionType.HUNGER, event, InventoryUtils.ALL);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPickUpExp(PlayerPickupExperienceEvent event) {
        EventExecutor.execute(event.getPlayer(), ActionType.PICK_UP_EXPERIENCE, event, InventoryUtils.ALL);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onHeldItem(PlayerItemHeldEvent event) {
        //Dexterous Revert
        Player player = event.getPlayer();
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        EventExecutor.execute(player, player.getInventory().getItem(event.getNewSlot()), ActionType.ITEM_HELD, event, EquipmentSlot.HAND);
    }
}
