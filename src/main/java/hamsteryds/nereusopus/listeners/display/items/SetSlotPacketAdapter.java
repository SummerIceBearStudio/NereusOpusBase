package hamsteryds.nereusopus.listeners.display.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.DisplayUtils;
import org.bukkit.inventory.ItemStack;

public class SetSlotPacketAdapter extends PacketAdapter {
    public SetSlotPacketAdapter() {
        super(NereusOpus.plugin, PacketType.Play.Server.SET_SLOT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ItemStack item = packet.getItemModifier().getValues().get(0);
        packet.getItemModifier().getValues().set(0, DisplayUtils.toDisplayMode(item));
    }
}
