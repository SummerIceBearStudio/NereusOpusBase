package hamsteryds.nereusopus.supports;

import hamsteryds.nereusopus.utils.api.display.DisplayUtils;
import me.arasple.mc.trchat.module.internal.hook.HookPlugin;
import me.arasple.mc.trchat.module.internal.hook.type.HookDisplayItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * NereusOpusBase
 * hamsteryds.nereusopus.supports.HookNereusOpus
 *
 * @author Mical
 * @since 2023/1/7 6:02 PM
 */
public class HookNereusOpus extends HookDisplayItem {

    public static void initialize() {
        final HookNereusOpus ner = new HookNereusOpus();
        HookPlugin.INSTANCE.addHook(ner);
    }

    public static void unInitialize() {
        HookPlugin.INSTANCE.getRegistry().removeIf(hook -> hook.getName().equals("NereusOpus"));
    }

    @NotNull
    @Override
    public ItemStack displayItem(@NotNull final ItemStack itemStack, @Nullable final Player player) {
        if (!isHooked() || itemStack.getType().equals(Material.AIR)) {
            return itemStack;
        }
        try {
            return DisplayUtils.toDisplayMode(itemStack);
        } catch (Throwable ignored) {
            return itemStack;
        }
    }
}
