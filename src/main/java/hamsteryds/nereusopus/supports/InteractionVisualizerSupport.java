package hamsteryds.nereusopus.supports;

import com.loohp.interactionvisualizer.blocks.EnchantmentTableDisplay;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.EnchantmentUtils;
import org.bukkit.Bukkit;

import java.util.Objects;

public class InteractionVisualizerSupport {
    public static void initialize() {
        try {
            Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("InteractionVisualizer")).getName();
            EnchantmentUtils.getEnchants().forEach((abstractEnchantment -> {
                EnchantmentTableDisplay.getCustomDefinedEnchantmentNames().put(abstractEnchantment.getKey().asString(), abstractEnchantment.displayName());
            }));
        } catch (NullPointerException exception) {
            NereusOpus.infoConsole("   未检测到InteractionVisualizer插件，停止匹配！");
        }
    }
}
