package hamsteryds.nereusopus.listeners.display.guis;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.ConfigUtils;
import hamsteryds.nereusopus.utils.api.DisplayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

public class MerchantListPacketAdapter extends PacketAdapter {
    private static final boolean enableDisplayIngredients;
    private static final boolean enableDisplayResult;

    static {
        enableDisplayIngredients = ConfigUtils.config.getBoolean("fixers.villager.display.ingredients");
        enableDisplayResult = ConfigUtils.config.getBoolean("fixers.villager.display.result");
    }

    public MerchantListPacketAdapter() {
        super(NereusOpus.plugin, ListenerPriority.MONITOR, PacketType.Play.Server.OPEN_WINDOW_MERCHANT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        List<MerchantRecipe> merchants = packet.getMerchantRecipeLists().getValues().get(0);
        for (int i = 0; i < merchants.size(); i++) {
            MerchantRecipe recipe = merchants.get(i);

            List<ItemStack> ingredients = recipe.getIngredients();
            if (enableDisplayResult) {
                boolean flag = ingredients.get(0).getType() != Material.EMERALD;
                for (int j = 0; j < ingredients.size(); j++) {
                    ingredients.set(j, DisplayUtils.toDisplayMode(ingredients.get(j)));
                }
                MerchantRecipe newRecipe = new MerchantRecipe(DisplayUtils.toDisplayMode(recipe.getResult()), recipe.getMaxUses());
                boolean hasDiscount = recipe.getIngredients().get(0).getAmount() != recipe.getAdjustedIngredient1().getAmount();
                for (int j = 0; j < ingredients.size(); j++) {
                    ItemStack item = ingredients.get(j);
                    int amount = item.getAmount();
                    if (j == 0 && hasDiscount && flag) {
                        amount = (int) (amount + recipe.getSpecialPrice() + Math.floor(amount * recipe.getPriceMultiplier() * recipe.getDemand()));
                    }
                    item.setAmount(Math.max(amount, 1));
                    newRecipe.addIngredient(item);
                }
                newRecipe.setMaxUses(recipe.getMaxUses());
                newRecipe.setDemand(recipe.getDemand());
                newRecipe.setUses(recipe.getUses());
                newRecipe.setVillagerExperience(recipe.getVillagerExperience());
                if (flag) {
                    newRecipe.setPriceMultiplier(0);
                    newRecipe.setSpecialPrice(0);
                } else {
                    newRecipe.setPriceMultiplier(recipe.getPriceMultiplier());
                    newRecipe.setSpecialPrice(recipe.getSpecialPrice());
                }
                merchants.set(i, newRecipe);
            } else {
                if (enableDisplayIngredients) {
                    for (int j = 0; j < ingredients.size(); j++) {
                        ingredients.set(j, DisplayUtils.toDisplayMode(ingredients.get(j)));
                    }
                    recipe.setIngredients(ingredients);
                }
                merchants.set(i, recipe);
            }
        }
        packet.getMerchantRecipeLists().write(0, merchants);
    }
}
