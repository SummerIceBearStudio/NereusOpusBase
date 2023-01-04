package hamsteryds.nereusopus.listeners.display.guis;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import hamsteryds.nereusopus.NereusOpus;

public class EnchantingTablePacket extends PacketAdapter {
    public EnchantingTablePacket() {
        super(NereusOpus.plugin, ListenerPriority.MONITOR, PacketType.Play.Server.WINDOW_DATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        StructureModifier<Integer> shorts = packet.getIntegers();
        int property = shorts.read(1);
        if (property >= 4 && property <= 6) {
            shorts.write(2, -1);
        }
    }
}
