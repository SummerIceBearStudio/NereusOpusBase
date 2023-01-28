package hamsteryds.nereusopus.listeners.display.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.display.DisplayUtils;
import org.bukkit.inventory.ItemStack;

public class SetCreativeSlotPacketAdapter extends PacketAdapter {
    public SetCreativeSlotPacketAdapter() {
        super(NereusOpus.plugin, PacketType.Play.Client.SET_CREATIVE_SLOT);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ItemStack item = packet.getItemModifier().getValues().get(0);
        packet.getItemModifier().getValues().set(0, DisplayUtils.toRevertMode(item));
    }
}
