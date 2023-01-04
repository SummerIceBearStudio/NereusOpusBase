package hamsteryds.nereusopus.listeners.mechanisms;

import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class VillagerListener implements Listener {
    private static final boolean enableTradeWithEnchants;
//    private static final NamespacedKey spawnReason = new NamespacedKey(NereusOpus.plugin, "spawnReason");

    static {
        enableTradeWithEnchants = ConfigUtils.config.getBoolean("fixers.villager.enabletradewithenchants");
    }

    @EventHandler
    public void onAcquire(VillagerAcquireTradeEvent event) {
        if (enableTradeWithEnchants) {
            MerchantRecipe recipe = event.getRecipe();
            ItemStack item = recipe.getResult();
            if (ItemUtils.getEnchants(item) == null) {
                event.setCancelled(true);
            }
            if (ItemUtils.getEnchants(item).size() > 0) {
                event.setCancelled(true);
            }
        }
    }

//    @EventHandler
//    public void onCreatureSpawn(CreatureSpawnEvent event) {
//        if (event.getEntityType() == EntityType.VILLAGER || event.getEntityType() == EntityType.WANDERING_TRADER) {
//            PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
//            container.set(spawnReason, PersistentDataType.STRING, event.getSpawnReason().toString());
//        }
//    }
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    public void onOpen(InventoryOpenEvent event) {
//        if (enableTradeWithEnchants && event.getInventory() instanceof MerchantInventory merchant) {
//            AbstractVillager villager = (Villager)merchant.getMerchant();
//            if (!Objects.equals(villager.getPersistentDataContainer().get(spawnReason, PersistentDataType.STRING), "NATURAL")) {
//                return;
//            }
//            List<MerchantRecipe> recipes = new ArrayList<>(merchant.getMerchant().getRecipes());
//            int size = recipes.size();
//            recipes.removeIf((MerchantRecipe recipe) -> ItemUtils.getEnchants(recipe.getResult()).size() > 0);
//            merchant.getMerchant().setRecipes(recipes);
//            if (recipes.size() != size) {
//                event.setCancelled(true);
//                event.getPlayer().openInventory(merchant);
//            }
//        }
//    }
}
