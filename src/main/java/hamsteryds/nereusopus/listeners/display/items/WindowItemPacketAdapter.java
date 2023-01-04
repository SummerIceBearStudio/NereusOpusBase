package hamsteryds.nereusopus.listeners.display.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.DisplayUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WindowItemPacketAdapter extends PacketAdapter {
    public WindowItemPacketAdapter() {
        super(NereusOpus.plugin, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        List<ItemStack> slots = packet.getItemListModifier().getValues().get(0);
        for (int i = 0; i < slots.size(); i++) {
            ItemStack item = slots.get(i);
            slots.set(i, DisplayUtils.toDisplayMode(item));
        }
        packet.getItemListModifier().getValues().set(0, slots);
    }
}
