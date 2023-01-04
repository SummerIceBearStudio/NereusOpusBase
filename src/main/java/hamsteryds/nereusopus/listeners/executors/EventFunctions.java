package hamsteryds.nereusopus.listeners.executors;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.EquipmentSlot;

@SuppressWarnings("unused")
public interface EventFunctions {
    default void trigger(int level, ActionType type, Event event, LivingEntity who) {
    }

    default void attackEntity(int level, EntityDamageByEntityEvent event) {
    }

    default void damagedByEntity(int level, EntityDamageByEntityEvent event) {
    }

    default void damagedByBlock(int level, EntityDamageByBlockEvent event) {
    }

    default void damaged(int level, EntityDamageEvent event) {
    }

    default void chat(int level, AsyncChatEvent event) {
    }

    default void advancementDone(int level, PlayerAdvancementDoneEvent event) {
    }

    default void armorstandManipulate(int level, PlayerArmorStandManipulateEvent event) {
    }

    default void bedEnter(int level, PlayerBedEnterEvent event) {
    }

    default void bedLeave(int level, PlayerBedLeaveEvent event) {
    }

    default void bucketEmpty(int level, PlayerBucketEmptyEvent event) {
    }

    default void bucketFill(int level, PlayerBucketFillEvent event) {
    }

    default void bucketEntity(int level, PlayerBucketEntityEvent event) {
    }

    default void dropItem(int level, PlayerDropItemEvent event) {
    }

    default void expChange(int level, PlayerExpChangeEvent event) {
    }

    default void fish(int level, PlayerFishEvent event) {
    }

    default void harvestBlock(int level, PlayerHarvestBlockEvent event) {
    }

    default void interactEntity(int level, PlayerInteractAtEntityEvent event) {
    }

    default void interactLeftBlock(int level, PlayerInteractEvent event) {
    }

    default void interactLeftAir(int level, PlayerInteractEvent event) {
    }

    default void interactLeft(int level, PlayerInteractEvent event) {
    }

    default void interactRightBlock(int level, PlayerInteractEvent event) {
    }

    default void interactRightAir(int level, PlayerInteractEvent event) {
    }

    default void interactRight(int level, PlayerInteractEvent event) {
    }

    default void itemBreak(int level, PlayerItemBreakEvent event) {
    }

    default void itemConsume(int level, PlayerItemConsumeEvent event) {
    }

    default void itemDamage(int level, PlayerItemDamageEvent event) {
    }

    default void itemHeld(int level, PlayerItemHeldEvent event) {
    }

    default void itemMend(int level, PlayerItemMendEvent event) {
    }

    default void levelChange(int level, PlayerLevelChangeEvent event) {
    }

    default void move(int level, PlayerMoveEvent event) {
    }

    default void pickUpArrow(int level, PlayerPickupArrowEvent event) {
    }

    default void pickUpItem(int level, PlayerAttemptPickupItemEvent event) {
    }

    default void portal(int level, PlayerPortalEvent event) {
    }

    default void recipeDiscover(int level, PlayerRecipeDiscoverEvent event) {
    }

    default void respawn(int level, PlayerRespawnEvent event) {
    }

    default void riptide(int level, PlayerRiptideEvent event) {
    }

    default void shearEntity(int level, PlayerShearEntityEvent event) {
    }

    default void swapHandItems(int level, PlayerSwapHandItemsEvent event) {
    }

    default void takeLecternBook(int level, PlayerTakeLecternBookEvent event) {
    }

    default void teleport(int level, PlayerTeleportEvent event) {
    }

    default void toggleSneak(int level, PlayerToggleSneakEvent event) {
    }

    default void toggleSprint(int level, PlayerToggleSprintEvent event) {
    }

    default void toggleFlight(int level, PlayerToggleFlightEvent event) {
    }

    default void unleashEntity(int level, PlayerUnleashEntityEvent event) {
    }

    default void blockBreak(int level, BlockBreakEvent event) {
    }

    default void blockDamageAbort(int level, BlockDamageEvent event) {
    }

    default void blockDamage(int level, BlockDamageEvent event) {
    }

    default void blockDispenseArmor(int level, BlockDispenseArmorEvent event) {
    }

    default void blockDispense(int level, BlockDispenseEvent event) {
    }

    default void blockDropItem(int level, BlockDropItemEvent event) {
    }

    default void blockFertilize(int level, BlockFertilizeEvent event) {
    }

    default void blockMultiPlace(int level, BlockMultiPlaceEvent event) {
    }

    default void blockPlace(int level, BlockPlaceEvent event) {
    }

    default void signChange(int level, SignChangeEvent event) {
    }

    default void notePlay(int level, NotePlayEvent event) {
    }

    default void enchantItem(int level, EnchantItemEvent event) {
    }

    default void prepareItemEnchant(int level, PrepareItemEnchantEvent event) {
    }

    default void lightningStrike(int level, LightningStrikeEvent event) {
    }

    default void tickTask(int level, EquipmentSlot slot, Player player, int stamp) {
    }

    default void shootBow(int level, EntityShootBowEvent event) {
    }

    default void death(int level, EntityDeathEvent event) {

    }

    default void entityJump(int level, EntityJumpEvent event) {

    }

    default void playerJump(int level, PlayerJumpEvent event) {

    }

    default void projectileLaunch(int level, ProjectileLaunchEvent event) {

    }

    default void projectileHitBlock(int level, ProjectileHitEvent event) {

    }

    default void projectileHitEntity(int level, ProjectileHitEvent event) {

    }

    default void elytraBoost(int level, PlayerElytraBoostEvent event) {

    }

    default void kill(int level, EntityDeathEvent event) {

    }

    default void hunger(int level, FoodLevelChangeEvent event) {

    }

    default void regainHealth(int level, EntityRegainHealthEvent event) {

    }

    default void pickUpExperience(int level, PlayerPickupExperienceEvent event) {

    }
    default void beTargeted(int level, EntityTargetLivingEntityEvent event) {

    }
}
